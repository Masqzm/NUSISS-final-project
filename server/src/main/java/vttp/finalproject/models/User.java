package vttp.finalproject.models;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import vttp.finalproject.utils.Utils;

public class User {
    private String userId;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 5, max = 32, message = "Username must be 3-16 characters long")
    @Pattern(regexp="^[a-zA-Z0-9_]+$", message="Only alphanumeric characters and underscores are allowed")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please enter a vaild email")
    private String email;

    private List<String> rsvpIds = new ArrayList<>();   // to keep track of upcoming jio events user is attending
    //private List<Restaurant> favRestaurants;          // add-on feature

    public User() {}
    public User(String username, String password, String email, List<String> rsvpIds) {
        this.username = username;
        this.password = password;
        this.email = email;

        this.rsvpIds = rsvpIds;
    }

    public static User jsonToUser(String json, boolean encryptPassword) {
        if (json == null)
            return null;
        
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject j = reader.readObject();

        List<String> ids = new ArrayList<>(); 

        if(j.containsKey("rsvpIds"))
            for(int i = 0; i < j.getJsonArray("rsvpIds").size(); i++) 
                ids.add(j.getJsonArray("rsvpIds").getString(i));

        // Encrypt password if its not already been encrypted
        String hashedPassword = encryptPassword ? Utils.encryptPassword(j.getString("password")) : j.getString("password");

        User user = new User(j.getString("username"),
                            hashedPassword,
                            j.getString("email"),
                            ids);

        return user;
    }

    public String toJson() {
        JsonArrayBuilder jArrBuilder = Json.createArrayBuilder();

        // If rsvpIds is null, empty JSON array will be built (arr = [])
        if(rsvpIds != null && !rsvpIds.isEmpty()) {
            for(String rsvpID : rsvpIds) 
                jArrBuilder.add(rsvpID);
        }

        if(userId == null) 
            userId = "";

        JsonObject job = Json.createObjectBuilder()
                        .add("userId", userId)
                        .add("username", username)
                        .add("email", email)
                        .add("rsvpIds", jArrBuilder.build())
                        .build();

        return job.toString();
    }
    
    @Override
    public String toString() {
        return toJson();
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public List<String> getRsvpIds() {
        return rsvpIds;
    }
    public void setRsvpIds(List<String> rsvpIds) {
        this.rsvpIds = rsvpIds;
    }
}
