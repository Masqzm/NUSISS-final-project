package vttp.finalproject.models;

import static vttp.finalproject.utils.Constants.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Restaurant {
    private String id;
    private String name;
    private String address;
    private String googleMapsURI;
    private String googlePhotosURI; 
    private String websiteURI;

    private double startPrice;      // lowest price
    private double endPrice;        // highest price
    private String priceRange;
    
    private double rating;
    private int userRatingCount;

    //private boolean hasDineIn;

    private List<String> openingHours;  // list of opening hours per day
    //private List<String> cuisinesList;      // ADD-ON FEATURE, NOT PRIORITY (compare against data/fnb_types.txt)
    //private List<Review> reviewsList;       // ADD ON FEATURE, NOT PRIORITY

    private List<String> rsvpIds;             // stores all rsvp ids (jio event IDs) users have made

    public Restaurant() {}
    public Restaurant(String id, String name, String address, String googleMapsURI, String googlePhotosURI,
            String websiteURI, double startPrice, double endPrice, String priceRange, double rating,
            int userRatingCount, List<String> openingHours,
            List<String> rsvpIds) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.googleMapsURI = googleMapsURI;
        this.googlePhotosURI = googlePhotosURI;
        this.websiteURI = websiteURI;
        this.startPrice = startPrice;
        this.endPrice = endPrice;
        this.priceRange = priceRange;
        this.rating = rating;
        this.userRatingCount = userRatingCount;
        this.openingHours = openingHours;
        this.rsvpIds = rsvpIds;
    }

    // To parse search results response from Places API
    public static List<Restaurant> jsonToRestaurantList(String json, String googleAPIkey) {
        if(json == null || json.trim().equals("{}"))
            return null;
        
        List<Restaurant> restaurantsList = new ArrayList<>();

        JsonReader reader = Json.createReader(new StringReader(json));
        JsonArray jsonRestArr = reader.readObject().getJsonArray("places");
        
        for(int i = 0; i < jsonRestArr.size(); i++) {
            JsonObject j = jsonRestArr.getJsonObject(i);
            
            // Check and discard (skip) restaurants without DineIn option available
            if(j.get("dineIn") == null || !j.getBoolean("dineIn"))
                continue;

            // Also skip restaurants not within SG (or doesn't have SG postal code)
            if(!j.getString("formattedAddress").contains("Singapore"))
                continue;
            
            Restaurant rest = new Restaurant();

            rest.setId(j.getString("googleMapsUri").split("cid=")[1]);
            rest.setName(j.getJsonObject("displayName").getString("text"));
            rest.setAddress(j.getString("formattedAddress"));

            if(j.containsKey("rating"))
                rest.setRating(j.getJsonNumber("rating").doubleValue());
            if(j.containsKey("userRatingCount"))
                rest.setUserRatingCount(j.getInt("userRatingCount"));

            rest.setGoogleMapsURI(j.getString("googleMapsUri"));

            if(j.containsKey("photos")) {
                JsonObject jFirstPhoto = j.getJsonArray("photos").getJsonObject(0);

                String parameters = "maxHeightPx=" + jFirstPhoto.getInt("heightPx") + "&maxWidthPx=" + jFirstPhoto.getInt("widthPx");
                
                String uri = GOOGLE_PLACES_PHOTO_URI.replace("NAME", jFirstPhoto.getString("name"))
                            .replace("API_KEY", googleAPIkey)
                            .replace("PARAMETERS", parameters);

                rest.setGooglePhotosURI(uri);
            }
            
            if(j.containsKey("websiteUri"))
                rest.setWebsiteURI(j.getString("websiteUri"));

            if(j.containsKey("priceRange")) {
                JsonObject jPriceRange = j.getJsonObject("priceRange");

                rest.setStartPrice(Double.parseDouble(jPriceRange.getJsonObject("startPrice").getString("units")));
                rest.setEndPrice(Double.parseDouble(jPriceRange.getJsonObject("endPrice").getString("units")));

                String priceRangeStr = "$" + rest.getStartPrice() + "-" + rest.getEndPrice();
                rest.setPriceRange(priceRangeStr);
            }

            if( j.containsKey("regularOpeningHours") &&
                j.getJsonObject("regularOpeningHours").containsKey("weekdayDescriptions")) {
                JsonArray jOpeningHrsArr = j.getJsonObject("regularOpeningHours").getJsonArray("weekdayDescriptions");
                List<String> openingHrs = new ArrayList<>();

                for(int k = 0; k < jOpeningHrsArr.size(); k++) {
                    // Add opening hour with a bit of reformatting
                    // String oh = jOpeningHrsArr.getString(k);
                    // openingHrs.add(oh.replace("y:", "y/n"));

                    openingHrs.add(jOpeningHrsArr.getString(k));
                }
                
                rest.setOpeningHours(openingHrs);
            }

            restaurantsList.add(rest);
        }

        return restaurantsList;
    }

    // To parse restaurant json saved
    public static Restaurant jsonToRestaurant(String json) {
        //System.out.println(json);
        if(json == null)
            return null;

        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject j = reader.readObject();

        JsonArray jArrOH = j.getJsonArray("openingHours");
        JsonArray jArrRsvpIDs = j.getJsonArray("rsvpIds");
        
        List<String> openingHrs = new ArrayList<>();
        List<String> rsvpIDs = new ArrayList<>();
        
        for(int i = 0; i < jArrOH.size(); i++) 
            openingHrs.add(jArrOH.getString(i));
        
        for(int i = 0; i < jArrRsvpIDs.size(); i++) 
            rsvpIDs.add(jArrRsvpIDs.getString(i));

        Restaurant rest = new Restaurant(j.getString("id"),
                                j.getString("name"),
                                j.getString("address"),
                                j.getString("googleMapsURI"),
                                j.getString("googlePhotosURI"),
                                j.getString("websiteURI"),
                                j.getJsonNumber("startPrice").doubleValue(),
                                j.getJsonNumber("endPrice").doubleValue(),
                                j.getString("priceRange"),
                                j.getJsonNumber("rating").doubleValue(),
                                j.getInt("userRatingCount"),
                                openingHrs, rsvpIDs);
                                
        return rest;
    }
    public JsonObject toJson() {
        JsonArrayBuilder jArrBOpeningHrs = Json.createArrayBuilder();
        JsonArrayBuilder jArrBRsvpIDs = Json.createArrayBuilder();
        
        for (String openingHrs : openingHours)
            jArrBOpeningHrs.add(openingHrs);
        
        // If rsvpIds is null, empty JSON array will be built (arr = [])
        if(rsvpIds != null && !rsvpIds.isEmpty()) {
            for (String rsvpID : rsvpIds)
                jArrBRsvpIDs.add(rsvpID);
        } 

        if(this.id == null)
            this.id = "";
        if(this.googlePhotosURI == null)
            this.googlePhotosURI = "";
        if(this.websiteURI == null)
            this.websiteURI = "";

        JsonObject json = Json.createObjectBuilder()
                        .add("id", this.id)
                        .add("name", this.name)
                        .add("address", this.address)
                        .add("googleMapsURI", this.googleMapsURI)
                        .add("googlePhotosURI", this.googlePhotosURI)
                        .add("websiteURI", this.websiteURI)
                        .add("startPrice", this.startPrice)
                        .add("endPrice", this.endPrice)
                        .add("priceRange", this.priceRange)
                        .add("rating", this.rating)
                        .add("userRatingCount", this.userRatingCount)
                        .add("openingHours", jArrBOpeningHrs.build())
                        .add("rsvpIds", jArrBRsvpIDs.build())
                        .build(); 

        return json;
    }
    
    @Override
    public String toString() {
        return toJson().toString();
    }
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getGoogleMapsURI() {
        return googleMapsURI;
    }
    public void setGoogleMapsURI(String googleMapsURI) {
        this.googleMapsURI = googleMapsURI;
    }
    public String getGooglePhotosURI() {
        return googlePhotosURI;
    }
    public void setGooglePhotosURI(String googlePhotosURI) {
        this.googlePhotosURI = googlePhotosURI;
    }
    public String getWebsiteURI() {
        return websiteURI;
    }
    public void setWebsiteURI(String websiteURI) {
        this.websiteURI = websiteURI;
    }
    public double getStartPrice() {
        return startPrice;
    }
    public void setStartPrice(double startPrice) {
        this.startPrice = startPrice;
    }
    public double getEndPrice() {
        return endPrice;
    }
    public void setEndPrice(double endPrice) {
        this.endPrice = endPrice;
    }
    public String getPriceRange() {
        return priceRange;
    }
    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public int getUserRatingCount() {
        return userRatingCount;
    }
    public void setUserRatingCount(int userRatingCount) {
        this.userRatingCount = userRatingCount;
    }
    public List<String> getOpeningHours() {
        return openingHours;
    }
    public void setOpeningHours(List<String> openingHours) {
        this.openingHours = openingHours;
    }
    // public List<String> getCuisinesList() {
    //     return cuisinesList;
    // }
    // public void setCuisinesList(List<String> cuisinesList) {
    //     this.cuisinesList = cuisinesList;
    // }
    // public List<Review> getReviewsList() {
    //     return reviewsList;
    // }
    // public void setReviewsList(List<Review> reviewsList) {
    //     this.reviewsList = reviewsList;
    // }
    public List<String> getRsvpIds() {
        return rsvpIds;
    }
    public void setRsvpIds(List<String> rsvpIds) {
        this.rsvpIds = rsvpIds;
    }
}
