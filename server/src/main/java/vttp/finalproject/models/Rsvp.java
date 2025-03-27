package vttp.finalproject.models;

import java.io.StringReader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Rsvp {
    private String id;
    private String posterId;
    private String posterName;
    private String restaurantId;
    private Long timestamp;
    private int capacity;
    private boolean rsvpingForPromo;

    private List<String> topics = new ArrayList<>();
    private List<String> attendeesEmails = new ArrayList<>();

    public static Rsvp jsonToRsvp(String json) {
        if (json == null)
            return null;
        
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject j = reader.readObject();

        List<String> topicsList = new ArrayList<>(); 
        List<String> attendeesEmailsList = new ArrayList<>(); 

        for(int i = 0; i < j.getJsonArray("topics").size(); i++) 
            topicsList.add(j.getJsonArray("topics").getString(i));

        for(int i = 0; i < j.getJsonArray("attendeesEmails").size(); i++) 
        attendeesEmailsList.add(j.getJsonArray("attendeesEmails").getString(i));

        //Restaurant rest = Restaurant.jsonToRestaurant(j.getString("restaurant"));

        Rsvp rsvp = new Rsvp();

        rsvp.setId(j.getString("id"));
        rsvp.setPosterId(j.getString("posterId"));
        rsvp.setPosterName(j.getString("posterName"));
        rsvp.setRestaurantId(j.getString("restaurantId"));
        rsvp.setTimestamp(j.getJsonNumber("timestamp").longValue());
        rsvp.setCapacity(j.getInt("capacity"));
        rsvp.setRsvpingForPromo(j.getBoolean("rsvpingForPromo"));
        rsvp.setAttendeesEmails(attendeesEmailsList);
        rsvp.setTopics(topicsList);

        return rsvp;
    }

    public String toJson() {
        JsonArrayBuilder jArrBuilderTopics = Json.createArrayBuilder();
        JsonArrayBuilder jArrBuilderAttendees = Json.createArrayBuilder();

        for(String topic : topics) 
            jArrBuilderTopics.add(topic);
        
        for(String attendee : attendeesEmails) 
            jArrBuilderAttendees.add(attendee);

        JsonObject job = Json.createObjectBuilder()
                        .add("id", id)
                        .add("posterId", posterId)
                        .add("posterName", posterName)
                        .add("restaurantID", restaurantId)
                        .add("timestamp", timestamp)
                        .add("capacity", capacity)
                        .add("rsvpingForPromo", rsvpingForPromo)
                        .add("topics", jArrBuilderTopics.build())
                        .add("attendeesEmails", jArrBuilderAttendees.build())
                        .build();

        return job.toString();
    }

    @Override
    public String toString() {
        return toJson();
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPosterId() {
        return posterId;
    }
    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }
    public String getPosterName() {
        return posterName;
    }
    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }
    public String getRestaurantId() {
        return restaurantId;
    }
    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }
    public Long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public boolean isRsvpingForPromo() {
        return rsvpingForPromo;
    }
    public void setRsvpingForPromo(boolean rsvpingForPromo) {
        this.rsvpingForPromo = rsvpingForPromo;
    }
    public List<String> getTopics() {
        return topics;
    }
    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
    public List<String> getAttendeesEmails() {
        return attendeesEmails;
    }
    public void setAttendeesEmails(List<String> attendeesEmails) {
        this.attendeesEmails = attendeesEmails;
    }
}
