package vttp.finalproject.repo;

import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp.finalproject.models.User;

@Repository
public class UserRepo {
    @Autowired
    private JdbcTemplate template;

    public static final String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";

    public static final String INSERT_USER = "INSERT INTO users(user_id, email, password) VALUES (?, ?, ?)";
    public static final String INSERT_USERPROFILE = "INSERT INTO user_profiles(user_id, username) VALUES (?, ?)";
    
    public static final String INSERT_USER_GOOGLE = "INSERT INTO users(user_id, email, oauth2_id) VALUES (?, ?, ?)";

    public Optional<User> getUserByEmail(String email) throws SQLException {
        try {
            User user = template.queryForObject(GET_USER_BY_EMAIL, 
                        BeanPropertyRowMapper.newInstance(User.class), email);
            
            return Optional.of(user);
        }
        catch(DataAccessException ex) {
            return Optional.empty();
        }
    }

    public void createUser(User user) {
        Object[] data = new Object[] {
                        user.getUserId(),
                        user.getEmail(),
                        user.getPassword()
                    };

        template.update(INSERT_USER, data);
    }
    public void createUserWGoogleAuth(User user, String oauth2Id) {
        Object[] data = new Object[] {
                        user.getUserId(),
                        user.getEmail(),
                        oauth2Id
                    };

        template.update(INSERT_USER_GOOGLE, data);
    }
    public void createUserProfile(User user) {
        Object[] data = new Object[] {
                        user.getUserId(),
                        user.getUsername()
                    };

        template.update(INSERT_USERPROFILE, data);
    }
}
