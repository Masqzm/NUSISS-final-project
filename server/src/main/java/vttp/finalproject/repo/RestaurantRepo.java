package vttp.finalproject.repo;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import vttp.finalproject.models.Restaurant;

@Repository
public class RestaurantRepo {
    @Autowired
    private MongoTemplate template;

    // db.restaurants.update(
    //     { _id: doc._id },        // filter by _id
    //     { $setOnInsert: doc },   // insert full doc 
    //     { upsert: true }         // insert if no document with _id exists
    // );
    public void insertRestaurants(List<Restaurant> restaurants) {
        for (Restaurant rest : restaurants) {
            Document doc = Document.parse(rest.toString());
            doc.put("_id", rest.getId());

            Query query = new Query().addCriteria(Criteria.where("_id").is(rest.getId()));
            
            Update update = new Update();       // setOnInsert: doc
            doc.forEach(update::setOnInsert);   // add all fields

            template.upsert(query, update, "restaurants");
        }
    }

    // db.restaurants.find({
    //     _id: <id>
    // })
    public Document getRestaurantById(String id) {
        return template.findById(id, Document.class, "restaurants");
    }
}
