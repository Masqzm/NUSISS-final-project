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

public class User {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 5, max = 32, message = "Username must be 3-16 characters long")
    @Pattern(regexp="^[a-zA-Z0-9_]+$", message="Only alphanumeric characters and underscores are allowed")
    private String name;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please enter a vaild email")
    private String email;

    private List<String> rsvpIds;    // to keep track of upcoming jio events user is attending
    //private List<Restaurant> favRestaurants;      // add-on feature

    public User() {}
    public User(String name, String password, String email, List<String> rsvpIds) {
        this.name = name;
        this.password = password;
        this.email = email;

        this.rsvpIds = rsvpIds;
    }

    public static User jsonToUser(String json) {
        if (json == null)
            return null;
        
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject j = reader.readObject();

        List<String> ids = new ArrayList<>(); 

        for(int i = 0; i < j.getJsonArray("rsvpIds").size(); i++) 
            ids.add(j.getJsonArray("rsvpIds").getString(i));

        User user = new User(j.getString("name"),
                        j.getString("password"),
                        j.getString("email"),
                        ids);

        return user;
    }

    public String toJson() {
        JsonArrayBuilder jArrBuilder = Json.createArrayBuilder();

        // If rsvpIds is null, empty JSON array will be built (arr = [])
        if(rsvpIds != null && !rsvpIds.isEmpty()) {
            for(String jioID : rsvpIds) 
                jArrBuilder.add(jioID);
        }

        JsonObject job = Json.createObjectBuilder()
                        .add("name", name)
                        .add("password", password)
                        .add("email", email)
                        .add("jioIDs", jArrBuilder.build())
                        .build();

        return job.toString();
    }
    
    @Override
    public String toString() {
        return toJson();
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
