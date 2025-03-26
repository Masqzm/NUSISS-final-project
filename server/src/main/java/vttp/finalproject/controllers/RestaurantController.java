package vttp.finalproject.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jakarta.json.Json;
import jakarta.servlet.http.HttpSession;
import vttp.finalproject.models.Restaurant;
import vttp.finalproject.services.RestaurantService;

@RestController
@RequestMapping("/api")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantSvc;

    @GetMapping(path = "/restaurant/{id}")
    public ResponseEntity<String> getRestaurant(@PathVariable String id) {
        Restaurant rest;
        
        try {
            rest = restaurantSvc.getRestaurantById(id); 
            
            if(rest == null) {
                String response = Json.createObjectBuilder()
                                .add("message", "Restaurant not found!")
                                .build().toString();  
          
                return ResponseEntity.status(404).body(response);
            }

            return ResponseEntity.ok().body(rest.toJson().toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            String response = Json.createObjectBuilder()
                            .add("message", ex.getMessage())
                            .build().toString();  
      
            return ResponseEntity.status(500).body(response);
        }
    }

    // @GetMapping(path = "/restaurant/{id}")
    // public ResponseEntity<String> getRestaurantInfo(@PathVariable String id) {
    //     Restaurant rest = searchSvc.getRestaurantByID(id); 
        
    //     // if restaurant cannot be found in db
    //     if(rest == null) {
    //         mav.setStatus(HttpStatusCode.valueOf(404));
    //         mav.addObject("id", id);
    //         mav.setViewName("error404"); 

    //         return mav;
    //     }

    //     // To pass restaurant info if users post a Jio
    //     sess.setAttribute(Constants.SESS_ATTR_JIO_RESTAURANT, rest);
        
    //     // Form error handling (passed thru sessions as users are redirected back here)
    //     BindingResult bindings = (BindingResult) sess.getAttribute(Constants.SESS_ATTR_JIO_FORM_ERR);
    //     Jio jio = (Jio) sess.getAttribute(Constants.SESS_ATTR_JIO_FORM);
        
    //     if(bindings != null && bindings.hasErrors()) {
    //         if(bindings.getFieldError("date") != null)
    //             mav.addObject("dateErrors", bindings.getFieldError("date").getDefaultMessage());
    //         if(bindings.getFieldError("time") != null)
    //             mav.addObject("timeErrors", bindings.getFieldError("time").getDefaultMessage());
    //         if(bindings.getFieldError("topics") != null)
    //             mav.addObject("topicsErrors", bindings.getFieldError("topics").getDefaultMessage());

    //         // Remove errors from sess
    //         sess.removeAttribute(Constants.SESS_ATTR_JIO_FORM_ERR);
    //     }

    //     // Bind jios to restaurant-info, if available
    //     if(!rest.getJioIDList().isEmpty()) {
    //         List<Jio> jios = new ArrayList<>();
    //         for (String jioID : rest.getJioIDList()) {
    //             Jio checkJio = jioSvc.getJioByID(jioID);

    //             // null jios are from deletion of jios
    //             if(checkJio != null)
    //                 jios.add(checkJio);
    //         }
            
    //         // Sort list by each Jio's unix timestamp
    //         jios.sort((jio1, jio2) -> {
    //             long unix1 = Jio.convertToUnixTimestamp(jio1.getDate(), jio1.getTime());
    //             long unix2 = Jio.convertToUnixTimestamp(jio2.getDate(), jio2.getTime());
                
    //             return Long.compare(unix1, unix2);  
    //         });

    //         mav.addObject("jioList", jios);
    //     }
                
    //     mav.addObject("restaurant", searchSvc.getRestaurantByID(id));
    //     mav.addObject("jio", (jio == null ? new Jio() : jio));
    //     mav.addObject("topicSuggestions", Constants.JIO_TOPICS_LIST);
    //     mav.addObject("postSuccess", postSuccess);
    //     mav.setViewName("restaurant-info"); 

    //     return mav;
    // }

}
