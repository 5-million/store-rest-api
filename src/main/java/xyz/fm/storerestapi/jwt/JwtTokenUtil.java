package xyz.fm.storerestapi.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.fm.storerestapi.entity.user.BaseUserEntity;

import javax.servlet.http.Cookie;

@Component
public class JwtTokenUtil {

    // spring security, jwt 구현 시 수정

    public static final String JWT_KEY = "STORE_AUTH_TOKEN";
    public static final String JWT_VALUE_PREFIX = "Bearer ";

    @Value("${jwt.expired}")
    private Integer expired;

    public Cookie generateTokenCookie(BaseUserEntity user) {
        Cookie cookie = new Cookie(JWT_KEY, JWT_VALUE_PREFIX + generateToken(user));
        cookie.setMaxAge(expired);
        return cookie;
    }

    public String generateToken(BaseUserEntity user) {
        return user.getEmail();
    }

    public String getEmailFromToken(String token) {
        return token.split(" ")[1];
    }

    public Integer getExpired() {
        return expired;
    }
}
