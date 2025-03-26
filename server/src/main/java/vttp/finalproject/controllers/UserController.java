package vttp.finalproject.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import vttp.finalproject.models.User;
import vttp.finalproject.services.OAuth2Service;
import vttp.finalproject.services.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userSvc;

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestBody String form) {
        //System.out.println(form);
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

            userSvc.createUser(user, false);
            
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
        System.out.println(form);
        // Check if user exists
        try {
            User user = User.jsonToUser(form, false);   // dont encrypt password as we need to check it later
            user = userSvc.validateUser(user);  // will make user null if user given credentials fails

            if(user == null) {
                String response = Json.createObjectBuilder()
                            .add("message", "invalid")
                            .build().toString();  

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
            // User credentials passes 
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
}
