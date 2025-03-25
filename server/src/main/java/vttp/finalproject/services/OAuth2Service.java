package vttp.finalproject.services;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class OAuth2Service {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleOAuth2ClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleOAuth2ClientSecret;
    private String googleOAuth2Url = "https://oauth2.googleapis.com/token";
    private String googleOAuth2RedirectUri = "http://localhost:8080/api/oauth2/code";  
    private String googleUserInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";

    // Make a POST call to Google to request access token
    public String getGoogleOAuthAccessToken(String code) {        
        // Create form
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("redirect_uri", googleOAuth2RedirectUri);
        form.add("client_id", googleOAuth2ClientId);
        form.add("client_secret", googleOAuth2ClientSecret);
        form.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile");
        form.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
        form.add("scope", "openid");
        form.add("grant_type", "authorization_code");

        // Create req
        RequestEntity<MultiValueMap<String, String>> req = RequestEntity
                    .post(googleOAuth2Url)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .accept(MediaType.APPLICATION_JSON)     
                    .body(form, MultiValueMap.class);

        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<String> resp = template.exchange(req, String.class);
            String payload = resp.getBody();
            
            System.out.printf("Payload from Google: %s\n", payload);

            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject json = reader.readObject();

            String accessToken = json.getString("access_token");

            System.out.println("Access token received: " + accessToken);

            getProfileDetailsGoogle(accessToken);

            return json.getString("access_token");
        } catch(Exception ex) {
            ex.printStackTrace();

            return ex.getMessage(); // TODO: throw error instead
        }
    }

    private void getProfileDetailsGoogle(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);
        
        RequestEntity<Void> req = RequestEntity
                                .get(googleUserInfoUrl)
                                .accept(MediaType.APPLICATION_JSON)
                                .headers(httpHeaders)
                                .build();

        RestTemplate template = new RestTemplate();
 
        try {
            ResponseEntity<String> resp = template.exchange(req, String.class);
            String payload = resp.getBody();
            
            System.out.printf("User info payload from Google: %s\n", payload);

            // TODO: Process user info (register if needed, return User obj etc.)
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
