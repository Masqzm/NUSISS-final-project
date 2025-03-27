package vttp.finalproject.services;

import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.finalproject.models.Rsvp;
import vttp.finalproject.repo.RsvpRepo;

@Service
public class RsvpService {
    @Autowired
    private RsvpRepo rsvpRepo;

    public void insertRsvp(Rsvp rsvp) {
        String rsvpId  = UUID.randomUUID().toString().substring(0, 8);
        rsvp.setId(rsvpId);

        rsvpRepo.insertRsvp(rsvp);
    }

    public List<Document> getAllRsvps() {
        return rsvpRepo.getAllRsvps();
    }
}
