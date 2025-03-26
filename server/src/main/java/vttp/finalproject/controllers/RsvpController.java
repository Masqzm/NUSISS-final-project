package vttp.finalproject.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import vttp.finalproject.utils.Constants;

@RestController
@RequestMapping("/api")
public class RsvpController {
    @GetMapping(path = "/rsvpTopics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRsvpTopics() {

        JsonArrayBuilder jArr = Json.createArrayBuilder();

        for (String topic : Constants.RSVP_TOPICS_LIST) 
            jArr.add(topic);
            
        return ResponseEntity.ok(jArr.build().toString()); 
    }
}