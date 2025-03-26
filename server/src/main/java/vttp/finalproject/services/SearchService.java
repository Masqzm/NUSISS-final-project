package vttp.finalproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp.finalproject.models.Restaurant;
import vttp.finalproject.repo.RestaurantRepo;

import static vttp.finalproject.utils.Constants.*;

import java.util.List;
import java.util.Optional;

@Service
public class SearchService {
    @Autowired
    private RestaurantRepo restaurantRepo;
    
    @Value("${google.api.key}") 
    private String googleAPIkey;

    public Optional<List<Restaurant>> searchRestaurants(String keyword) throws Exception {
        // Create Json payload
        JsonObject json = Json.createObjectBuilder()
                    .add("textQuery", keyword)
                    .add("includedType", "restaurant")
                    .add("locationRestriction", GOOGLE_PLACES_TEXTSEARCH_LOC_JSON)
                    .build();

        // Create req
        RequestEntity<String> req = RequestEntity
                    .post(GOOGLE_PLACES_TEXTSEARCH_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)  
                    .header("X-Goog-Api-Key", googleAPIkey)  
                    .header("X-Goog-FieldMask", GOOGLE_PLACES_TEXTSEARCH_FIELDS)  
                    .body(json.toString(), String.class);

        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<String> resp = template.exchange(req, String.class);
            String payload = resp.getBody();

            // Convert response to list of Restaurant objs
            List<Restaurant> restaurants = Restaurant.jsonToRestaurantList(payload, googleAPIkey);
            
            // cache restaurant search results
            if(restaurants != null)
                restaurantRepo.insertRestaurants(restaurants);
            
            return Optional.ofNullable(restaurants);
        } catch(Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
