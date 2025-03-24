package vttp.finalproject.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp.finalproject.models.Restaurant;
import vttp.finalproject.services.SearchService;

@RestController
@RequestMapping("/api")
public class SearchController {
    @Autowired
    private SearchService searchRestSvc;

    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> searchRestaurants(@RequestParam String q) {
        try {
            Optional<List<Restaurant>> optResults = searchRestSvc.searchRestaurants(q);

            if(optResults.isEmpty())
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("[]"); 
            
            List<JsonObject> resultObjs = optResults.get().stream()
                                        .map(Restaurant::toJson)
                                        .toList();
            
            return ResponseEntity.ok(Json.createArrayBuilder(resultObjs).build().toString()); 
            
        } catch (Exception ex) {
            // Server (endpoint) error 
            String response = Json.createObjectBuilder()
                            .add("message", ex.getMessage())
                            .build().toString();  

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
