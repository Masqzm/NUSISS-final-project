package vttp.finalproject.repo;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import vttp.finalproject.models.Rsvp;

@Repository
public class RsvpRepo {
    @Autowired
    private MongoTemplate template;

    // db.rsvps.insert(<doc>)
    public void insertRsvp(Rsvp rsvp) {
        Document doc = new Document();
        doc.put("_id", rsvp.getId());
        doc.put("id", rsvp.getId());
        doc.put("posterId", rsvp.getPosterId());
        doc.put("posterName", rsvp.getPosterName());
        doc.put("restaurantId", rsvp.getRestaurantId());
        doc.put("timestamp", rsvp.getTimestamp());
        doc.put("capacity", rsvp.getCapacity());
        doc.put("rsvpingForPromo", rsvp.isRsvpingForPromo());
        doc.put("topics", rsvp.getTopics());
        doc.put("attendeesEmails", rsvp.getAttendeesEmails());

        template.insert(doc, "rsvps");
    }

    public List<Document> getAllRsvps() {
        //List<Document> docs = 
        return template.findAll(Document.class, "rsvps");
    }

    // public List<Rsvp> findRsvpsByUserId(String name, int limit, int offset) {
    // }
}
