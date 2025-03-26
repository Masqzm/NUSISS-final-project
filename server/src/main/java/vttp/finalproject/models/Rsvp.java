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
    private String posterName;
    private String restaurantID;

    @NotNull(message = "*Please select a date")
    @Future(message = "*Please enter a valid future date")
    private LocalDate date;

    @NotNull(message = "*Please select a time")
    private LocalTime time;

    private int capacity;
    private boolean rsvpingForPromo;

    @Size(min = 1, message = "*Please select at least one topic")
    private List<String> topics;
    private List<String> attendeesNameList;   // inclusive of poster

    public Rsvp() {}
    public Rsvp(String id, String posterName, String restaurantID, LocalDate date, LocalTime time,
            int capacity, boolean rsvpingForPromo, List<String> topics, List<String> attendeesNameList) {
        this.id = id;
        this.posterName = posterName;
        this.restaurantID = restaurantID;
        this.date = date;
        this.time = time;
        this.capacity = capacity;
        this.rsvpingForPromo = rsvpingForPromo;
        this.topics = topics;
        this.attendeesNameList = attendeesNameList;
    }

    public static Rsvp jsonTorsvp(String json) {
        if (json == null)
            return null;
        
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject j = reader.readObject();

        List<String> topics = new ArrayList<>(); 
        List<String> attendeesNameList = new ArrayList<>(); 

        for(int i = 0; i < j.getJsonArray("topics").size(); i++) 
            topics.add(j.getJsonArray("topics").getString(i));

        for(int i = 0; i < j.getJsonArray("attendees").size(); i++) 
            attendeesNameList.add(j.getJsonArray("attendees").getString(i));

        //Restaurant rest = Restaurant.jsonToRestaurant(j.getString("restaurant"));

        // Convert unix (long) back to date & time
        Long unixTimestamp = j.getJsonNumber("unixTimestamp").longValue();
        ZonedDateTime zonedDateTime = Instant.ofEpochSecond(unixTimestamp).atZone(ZoneOffset.UTC);
        LocalDate date = zonedDateTime.toLocalDate();
        LocalTime time = zonedDateTime.toLocalTime();

        Rsvp rsvp = new Rsvp(j.getString("id"), 
                        j.getString("posterName"),
                        j.getString("restaurantID"), 
                        date, time,
                        j.getInt("capacity"),
                        j.getBoolean("rsvpingForPromo"),
                        topics, attendeesNameList);

        return rsvp;
    }

    public String toJson() {
        JsonArrayBuilder jArrBuilderTopics = Json.createArrayBuilder();
        JsonArrayBuilder jArrBuilderAttendees = Json.createArrayBuilder();

        for(String topic : topics) 
            jArrBuilderTopics.add(topic);
        
        for(String attendee : attendeesNameList) 
            jArrBuilderAttendees.add(attendee);

        JsonObject job = Json.createObjectBuilder()
                        .add("id", id)
                        .add("posterName", posterName)
                        .add("restaurantID", restaurantID)
                        .add("unixTimestamp", convertToUnixTimestamp(date, time))
                        .add("capacity", capacity)
                        .add("rsvpingForPromo", rsvpingForPromo)
                        .add("topics", jArrBuilderTopics.build())
                        .add("attendees", jArrBuilderAttendees.build())
                        .build();

        return job.toString();
    }

    // Combine date & time to convert to long unix timestamp
    public static long convertToUnixTimestamp(LocalDate date, LocalTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        
        return dateTime.toEpochSecond(ZoneOffset.UTC);
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
    public String getPosterName() {
        return posterName;
    }
    public void setPosterName(String posterName) {
        this.posterName = posterName;
    }
    public String getRestaurantID() {
        return restaurantID;
    }
    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public LocalTime getTime() {
        return time;
    }
    public void setTime(LocalTime time) {
        this.time = time;
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
    public List<String> getAttendeesNameList() {
        return attendeesNameList;
    }
    public void setAttendeesNameList(List<String> attendeesNameList) {
        this.attendeesNameList = attendeesNameList;
    }
}
