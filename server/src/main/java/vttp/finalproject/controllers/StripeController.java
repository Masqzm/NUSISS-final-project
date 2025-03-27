package vttp.finalproject.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.json.Json;

@RestController
@RequestMapping("/api/payment")
public class StripeController {
    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${client.base.url}")
    private String clientBaseUrl;

    @PostMapping("/checkout")
    public ResponseEntity<String> createCheckoutSession(@RequestBody Map<String, Long> payload) throws StripeException {        
        Stripe.apiKey = stripeSecretKey;
        long amount = payload.get("cost_cents") * 100;

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(clientBaseUrl + "/#/about?donateSuccess=true&sessionId={CHECKOUT_SESSION_ID}")    // CHECKOUT_SESSION_ID will be replaced by Stripe
                .setCancelUrl(clientBaseUrl + "/#/about?donateSuccess=false")
                .addLineItem(SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("sgd")
                                        .setUnitAmount(amount)
                                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName("Donation")
                                                        .build())
                                        .build())
                            .build())
                .build();

        // Start Stripe sess
        Session session = Session.create(params);

        String response = Json.createObjectBuilder()
                        .add("sessionId", session.getId())
                        .build().toString();  

        return ResponseEntity.ok().body(response);
    }
}
