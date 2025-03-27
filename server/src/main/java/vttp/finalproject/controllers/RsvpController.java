package vttp.finalproject.controllers;

import java.io.StringReader;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp.finalproject.models.Rsvp;
import vttp.finalproject.services.RsvpService;
import vttp.finalproject.utils.Constants;

@RestController
@RequestMapping("/api")
public class RsvpController {
    @Autowired
    private RsvpService rsvpSvc;
    
    @GetMapping(path = "/rsvpTopics", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRsvpTopics() {
        JsonArrayBuilder jArr = Json.createArrayBuilder();

        for (String topic : Constants.RSVP_TOPICS_LIST) 
            jArr.add(topic);
            
        return ResponseEntity.ok(jArr.build().toString()); 
    }
    @GetMapping(path = "/rsvps", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRsvps() {
        List<Document> docs = rsvpSvc.getAllRsvps();

        if(docs == null) {
            String response = Json.createObjectBuilder()
                            .add("message", "No rsvps found!")
                            .build().toString();  
      
            return ResponseEntity.status(404).body(response);
        }

        JsonArrayBuilder jArr = Json.createArrayBuilder();

        for(Document d : docs) {
            //System.out.println(d.toJson());
            
            JsonObject jsonObject = Json.createReader(new StringReader(d.toJson())).readObject();
            jArr.add(jsonObject);

            //jArr.add(d.toJson());
        }

        return ResponseEntity.ok().body(jArr.build().toString()); 
    }

    @PostMapping("/rsvp")
    public ResponseEntity<String> postRsvp(@RequestBody Rsvp rsvp) {
        //System.out.println(rsvp.toString());

        try {
            rsvpSvc.insertRsvp(rsvp);
        } catch (Exception ex) {
            ex.printStackTrace();
            String response = Json.createObjectBuilder()
                            .add("message", ex.getMessage())
                            .build().toString();  
      
            return ResponseEntity.status(500).body(response);
        }
            
        return ResponseEntity.ok("{}"); 
    }
}