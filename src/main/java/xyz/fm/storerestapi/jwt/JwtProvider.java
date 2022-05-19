package xyz.fm.storerestapi.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import xyz.fm.storerestapi.entity.Jwt;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.value.invalid.jwt.CustomJwtException;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    public static final String AUTH_CLAIM_KEY = "auth";

    private final String issuer;
    private final Key accessTokenKey;
    private final Key refreshTokenKey;
    private final long accessTokenExp = 1000 * 60 * 60; // 1hour
    private final long refreshTokenExp = 1000 * 60 * 60 * 24 * 15; // 15days

    public JwtProvider(
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.secret.accessToken}") String accessTokenSecret,
            @Value("${jwt.secret.refreshToken}") String refreshTokenSecret) {
        this.issuer = issuer;
        this.accessTokenKey = Keys.hmacShaKeyFor(accessTokenSecret.getBytes());
        this.refreshTokenKey = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes());
    }

    public Jwt issueToken(Authentication authentication) {
        String accessToken = generateToken(authentication, accessTokenKey, accessTokenExp);
        String refreshToken = generateToken(authentication, refreshTokenKey, refreshTokenExp);

        return new Jwt.Builder(authentication.getName(), accessToken, refreshToken).build();
    }

    private String generateToken(Authentication authentication, Key tokenKey, long expiration) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = new Date().getTime();

        return Jwts.builder()
                .setHeader(buildHeader())
                .setIssuer(issuer)
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(now))
                .claim(AUTH_CLAIM_KEY, authorities)
                .setExpiration(new Date(now + expiration))
                .signWith(SignatureAlgorithm.HS256, tokenKey)
                .compact();
    }

    public Authentication getAuthentication(String token, TokenType tokenType) {
        Claims claims = parseClaims(token, tokenType);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTH_CLAIM_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal,"", authorities);
    }

    private Map<String, Object> buildHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        return header;
    }

    public void validateToken(String token, TokenType tokenType) {
        Key tokenKey = tokenType == TokenType.JWT_ACCESS_TOKEN ? accessTokenKey : refreshTokenKey;

        try {
            Jwts.parserBuilder().setSigningKey(tokenKey).build().parseClaimsJws(token);
        } catch (JwtException e) {
            ErrorCode errorCode;

            if (e instanceof SignatureException || e instanceof MalformedJwtException) errorCode = ErrorCode.INVALID_SIGN_JWT;
            else if (e instanceof ExpiredJwtException) errorCode = ErrorCode.EXPIRED_JWT;
            else if (e instanceof UnsupportedJwtException) errorCode = ErrorCode.UNSUPPORTED_JWT;
            else errorCode = ErrorCode.INVALID_JWT;

            throw new CustomJwtException(errorCode);
        }
    }

    private Claims parseClaims(String token, TokenType tokenType) {
        Key key;
        if (tokenType == TokenType.JWT_ACCESS_TOKEN) key = accessTokenKey;
        else key = refreshTokenKey;

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
