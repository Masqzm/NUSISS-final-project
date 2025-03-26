package vttp.finalproject.services;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.finalproject.models.Restaurant;
import vttp.finalproject.repo.RestaurantRepo;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepo restaurantRepo;

    public Restaurant getRestaurantById(String id) {
        Document doc = restaurantRepo.getRestaurantById(id);

        if(doc == null)
            return null;

        return Restaurant.jsonToRestaurant(doc.toJson());
    }
}
