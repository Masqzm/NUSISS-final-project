package vttp.finalproject.controllers;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import vttp.finalproject.models.User;
import vttp.finalproject.services.OAuth2Service;
import vttp.finalproject.services.UserService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")     // for development
public class UserController {
    @Autowired
    private UserService userSvc;
    @Autowired
    private OAuth2Service oauth2Svc;

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestBody String form) {
        System.out.println(form);
        // Check if user exists
        try {
            User user = User.jsonToUser(form, true);
            Optional<User> optUser = userSvc.getUserByEmail(user.getEmail());

            // User exists
            if(!optUser.isEmpty()) {
                String response = Json.createObjectBuilder()
                                .add("message", "invalid")
                                .build().toString();  
                                
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            userSvc.createUser(user);
            
            // Send back User obj for successful registration (so clientside can use)
            String response = user.toJson();
            
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            String response = Json.createObjectBuilder()
                            .add("message", ex.getMessage())
                            .build().toString();  
      
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody String form) {        
        // Check if user exists

        return ResponseEntity.ok("{}"); 
    }
    
    // Google sign-in ref: https://medium.com/@sallu-salman/implementing-sign-in-with-google-in-spring-boot-application-5f05a34905a8
    // Auth flow:
    // 1. Client (user) continue with "Google sign-in"
    // 2. Google sends OAuth2 code here
    // 3. UserService makes request to get OAuth2 token & user details
    // For Google to send OAuth2 code to (step #1)
    @GetMapping(path = "/oauth2/code")
    public String getOAuth2Code(
        @RequestParam String code, @RequestParam String scope, 
        @RequestParam String authuser, @RequestParam String prompt) {
        System.out.println("OAuth2");
        System.out.println(">>> code: " + code);
        System.out.println(">>> scope: " + scope);
        System.out.println(">>> authuser: " + authuser);
        System.out.println(">>> prompt: " + prompt);

        return oauth2Svc.getGoogleOAuthAccessToken(code);
    }

    // For testing debug only. To show OAuth2 details of user
    // TODO: remove on deployment
    @GetMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
}
