package vttp.finalproject.services;

import java.io.StringReader;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.finalproject.models.User;

@Service
public class OAuth2Service {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleOAuth2ClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleOAuth2ClientSecret;
    private String googleOAuth2Url = "https://oauth2.googleapis.com/token";
    private String googleUserInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
    
    // For Google sign-in to redirect & send OAuth2 code to backend
    public static String googleOAuth2RedirectUri = "http://localhost:8080/api/oauth2/callback";  

    @Autowired
    private UserService userSvc;

    // Make a POST call to Google to request access token, to be exchanged for google user profile
    public User getGoogleOAuthAccessToken(String code) throws Exception {        
        // Create form
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("redirect_uri", googleOAuth2RedirectUri);
        form.add("client_id", googleOAuth2ClientId);
        form.add("client_secret", googleOAuth2ClientSecret);
        form.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile");
        form.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
        form.add("scope", "openid");
        form.add("grant_type", "authorization_code");

        // Create req
        RequestEntity<MultiValueMap<String, String>> req = RequestEntity
                    .post(googleOAuth2Url)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)     
                    .body(form, MultiValueMap.class);

        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<String> resp = template.exchange(req, String.class);
            String payload = resp.getBody();
            
            //System.out.printf("Payload from Google: %s\n", payload);

            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject json = reader.readObject();

            String accessToken = json.getString("access_token");

            //System.out.println("Access token received: " + accessToken);

            return getUserDetailsGoogle(accessToken);
        } catch(Exception ex) {
            ex.printStackTrace();

            throw ex;
        }
    }

    private User getUserDetailsGoogle(String accessToken) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);
        
        RequestEntity<Void> req = RequestEntity
                                .get(googleUserInfoUrl)
                                .accept(MediaType.APPLICATION_JSON)
                                .headers(httpHeaders)
                                .build();

        RestTemplate template = new RestTemplate();
 
        try {
            ResponseEntity<String> resp = template.exchange(req, String.class);
            String payload = resp.getBody();
            
            //System.out.printf("User info payload from Google: %s\n", payload);
            // Important details from payload:
            // "id": "122345678",
            // "email": "test@gmail.com",
            // "name": "Test",
            // "picture": "https://lh3.googleusercontent.com/a/code"

            // Process user info (register if needed, return User obj on successful registration/login)
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject json = reader.readObject();

            User user = new User();
            user.setEmail(json.getString("email"));
            user.setUsername(json.getString("name"));
            user.setProviderId(json.getString("id"));

            // Register user if havent done so yet
            Optional<User> optUser = userSvc.getUserByEmail(user.getEmail());
            String userId = "";

            if(optUser.isEmpty()) {
                userId = userSvc.createUser(user, true);
                user.setUserId(userId);
            } else 
                user.setUserId(optUser.get().getUserId());
            
            return user;

        } catch(Exception ex) {
            ex.printStackTrace();

            throw ex;
        }
    }
}
