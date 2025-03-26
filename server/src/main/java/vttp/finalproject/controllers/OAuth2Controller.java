package vttp.finalproject.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vttp.finalproject.models.User;
import vttp.finalproject.services.OAuth2Service;

@RestController
@RequestMapping("/api/oauth2")
public class OAuth2Controller {
    @Autowired
    private OAuth2Service oAuth2Svc;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleOAuth2ClientId;
    @Value("${client.base.url}")
    private String clientBaseUrl;
    
    // Google sign-in ref: https://medium.com/@sallu-salman/implementing-sign-in-with-google-in-spring-boot-application-5f05a34905a8
    // Auth flow:
    // 1. Client (user) continue with "Google sign-in", calls /api/oauth2/google to backend (here)
    // 2. Redirects from here to Google client which sends OAuth2 code to /api/oauth2/callback (googleOAuth2RedirectUri)
    // 3. Exchange code for accessToken in OAuth2Service - make REST call to Google to get user profile and temp store it in HttpSession
    // 4. Force client to redirect to homepage where it then calls /api/oauth2/user to get user obj (if available)
    
    // googleOAuth2RedirectUri: For Google sign-in to redirect & send OAuth2 code to backend
    // Scope: to access user's data from google (https://www.googleapis.com/auth/userinfo.email, https://www.googleapis.com/auth/userinfo.profile, openid)
    @GetMapping("/google")
    public void googleLoginRedirect(HttpServletResponse response) throws IOException {
        String GOOGLE_AUTH_URI = "https://accounts.google.com/o/oauth2/v2/auth?redirect_uri="
                                                + OAuth2Service.googleOAuth2RedirectUri
                                                + "&response_type=code&client_id="
                                                + googleOAuth2ClientId 
                                                + "&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+openid&access_type=offline";
                                                
        response.sendRedirect(GOOGLE_AUTH_URI);
    }

    @GetMapping("/callback")    // to be called by frontend as well to retrieve User payload
    public void handleGoogleCallback(
        @RequestParam String code, @RequestParam String scope, 
        @RequestParam String authuser, @RequestParam String prompt, 
        HttpSession sess, HttpServletResponse response) throws IOException {
        
        User user = null;
        
        try {
            user = oAuth2Svc.getGoogleOAuthAccessToken(code);
        } catch(Exception ex) {
            ex.printStackTrace();

            // set error message in sess for frontend to callback (/api/oauth2/user), to retrieve
            sess.setAttribute("message", ex.getMessage());
      
            response.sendRedirect(clientBaseUrl + "/#/login");
        }

        if (user == null) {
            sess.setAttribute("message", "unknown server error");
      
            response.sendRedirect(clientBaseUrl + "/#/login"); 
        }
        // set user in sess for frontend to callback (/api/oauth2/user), to retrieve
        sess.setAttribute("userJSON", user.toJson());
            
        //System.out.println("JSON set: " + user.toJson());
        response.sendRedirect(clientBaseUrl);
    }

    @GetMapping("/user")    // to be called by frontend as well to retrieve User payload
    public ResponseEntity<String> getUser(HttpSession sess) {
        String errorMsg = (String) sess.getAttribute("message");
        String userJson = (String) sess.getAttribute("userJSON");

        //System.out.println("called by frontend\nUserJSON: " + userJson);
        //System.out.println("errorMsg: " + errorMsg);
            
        sess.invalidate();  // clear error msgs and temp storage of user json

        if(errorMsg != null){
            String response = Json.createObjectBuilder()
                            .add("message", errorMsg)
                            .build().toString();  

            return ResponseEntity.status(500).body(response);
        } 
                    
        if(userJson != null) 
            return ResponseEntity.ok().body(userJson);

        String response = Json.createObjectBuilder()
                        .add("message", "unknown server error")
                        .build().toString();  

        return ResponseEntity.status(500).body(response);        
    }

    // For testing debug only. To show OAuth2 details of user    
    // @GetMapping("/user")
    // public Principal user(Principal user) {
    //     return user;
    // }
}
