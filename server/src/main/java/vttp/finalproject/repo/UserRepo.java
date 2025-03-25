package vttp.finalproject.repo;

import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vttp.finalproject.models.User;

@Repository
public class UserRepo {
    @Autowired
    private JdbcTemplate template;

    public static final String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    public static final String GET_USERPROFILE_BY_ID = "SELECT * FROM user_profiles WHERE user_id = ?";
    public static final String GET_USERRSVPS_BY_ID = "SELECT * FROM user_rsvps WHERE user_id = ?";

    public static final String INSERT_USER = "INSERT INTO users(user_id, email, password) VALUES (?, ?, ?)";
    public static final String INSERT_USERPROFILE = "INSERT INTO user_profiles(user_id, username) VALUES (?, ?)";
    
    public static final String INSERT_USER_GOOGLE = "INSERT INTO users(user_id, email, oauth2_id) VALUES (?, ?, ?)";

    public Optional<User> getUserByEmail(String email) throws SQLException {
        try {
            // User user = template.queryForObject(GET_USER_BY_EMAIL, 
            //             BeanPropertyRowMapper.newInstance(User.class), email);
            SqlRowSet rsUsers = template.queryForRowSet(GET_USER_BY_EMAIL, email);

            if (!rsUsers.next())
                return Optional.empty();

            User user = new User();
            user.setUserId(rsUsers.getString("user_id"));
            user.setEmail(rsUsers.getString("email"));
            user.setPassword(rsUsers.getString("password"));
                
            SqlRowSet rsUserProfiles = template.queryForRowSet(GET_USERPROFILE_BY_ID, user.getUserId());
            
            if (!rsUserProfiles.next())    // safety net, wont happen 
                return Optional.empty();
                
            user.setUsername(rsUserProfiles.getString("username"));
            
            SqlRowSet rsUserRsvps = template.queryForRowSet(GET_USERRSVPS_BY_ID, user.getUserId());
            
            while (rsUserRsvps.next()) 
                user.getRsvpIds().add(rsUserRsvps.getString("rsvp_id"));
            
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
