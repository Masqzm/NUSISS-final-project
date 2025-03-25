package vttp.finalproject.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity() // To view all the filters that a request passes through, set debug=true
public class SecurityConfig {

    // @Autowired
    // BearerTokenAuthFilter bearerTokenAuthFilter;

    // to whitelist the /api/oauth2Code path to pass through the filter
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http.authorizeHttpRequests(req -> req
            .requestMatchers("/api/oauth2/code").permitAll() // Allow OAuth2 login callback
            .anyRequest().permitAll()   // Allow all other request
        )
        // Redirect on successful login
        .oauth2Login(oauth2login -> { oauth2login
            .successHandler((request, response, authentication) -> response.sendRedirect("http://localhost:8080"))
            .defaultSuccessUrl("http://localhost:8080", true);
        })
        .formLogin(form -> form.disable())
        .csrf().disable()
        .cors()
        .and()
        //.formLogin(Customizer.withDefaults())
        // .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // .authenticationProvider(authenticationProvider)
        // .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        // .csrf(AbstractHttpConfigurer::disable)
        // .addFilterAfter(bearerTokenAuthFilter, BasicAuthenticationFilter.class)
        .build();
    }
}