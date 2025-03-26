package vttp.finalproject.utils;

import java.util.List;

import jakarta.json.JsonObject;

public class Constants {
    public static final String GOOGLE_PLACES_TEXTSEARCH_URL = "https://places.googleapis.com/v1/places:searchText";
    public static final String GOOGLE_PLACES_PHOTO_URI = "https://places.googleapis.com/v1/NAME/media?key=API_KEY&PARAMETERS";

    // "Constants" to be loaded from file    
    public static String GOOGLE_PLACES_TEXTSEARCH_FIELDS;
    public static JsonObject GOOGLE_PLACES_TEXTSEARCH_LOC_JSON;    
    public static List<String> RSVP_TOPICS_LIST;
}
