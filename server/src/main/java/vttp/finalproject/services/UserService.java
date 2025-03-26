package vttp.finalproject.services;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vttp.finalproject.models.User;
import vttp.finalproject.repo.UserRepo;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public Optional<User> getUserByEmail(String email) throws SQLException {
        return userRepo.getUserByEmail(email);
    }

    @Transactional
    public String createUser(User user, boolean usingOAuth2) {
        String userId  = UUID.randomUUID().toString().substring(0, 8);
        user.setUserId(userId);

        userRepo.createUser(user, usingOAuth2);
        userRepo.createUserProfile(user);
        
        return userId;
    }

    public User validateUser(User user) throws SQLException {
        // check if user exist
        Optional<User> optUser = getUserByEmail(user.getEmail());

        if(optUser.isEmpty())
            return null;

        User userValidator = optUser.get();

        // System.out.println("user: " + user);
        // System.out.println("userV: " + userValidator);
        // System.out.println("user pw: " + user.getPassword());
        // System.out.println("userV pw: " + userValidator.getPassword());
        
        // check password match
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        if(encoder.matches(user.getPassword(), userValidator.getPassword()))    // encoder.matches(raw, encrypted)
            return userValidator;

        return null;
    }
}
