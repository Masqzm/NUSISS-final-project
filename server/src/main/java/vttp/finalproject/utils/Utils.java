package vttp.finalproject.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Utils {
    public static String encryptPassword(String password) {
        return (new BCryptPasswordEncoder()).encode(password);
    }
}