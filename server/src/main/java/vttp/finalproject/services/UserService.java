package vttp.finalproject.services;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
    public String createUser(User user) {
        String userId  = UUID.randomUUID().toString().substring(0, 8);
        user.setUserId(userId);

        userRepo.createUser(user);
        userRepo.createUserProfile(user);
        
        return userId;
    }
}
