package xyz.fm.storerestapi.dto;

import xyz.fm.storerestapi.entity.Jwt;

public class Tokens {

    private String accessToken;
    private String refreshToken;

    public Tokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static Tokens of(Jwt jwt) {
        return new Tokens(jwt.getAccessToken(), jwt.getRefreshToken());
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
