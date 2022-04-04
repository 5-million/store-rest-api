package xyz.fm.storerestapi.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EncryptUtil {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encode(String plainTextPassword) {
        return passwordEncoder.encode(plainTextPassword);
    }

    public static boolean match(String plainTextPassword, String encodedPassword) {
        return passwordEncoder.matches(plainTextPassword, encodedPassword);
    }
}
