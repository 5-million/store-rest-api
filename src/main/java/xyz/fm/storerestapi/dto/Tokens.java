package xyz.fm.storerestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.fm.storerestapi.entity.Jwt;

@Getter
@AllArgsConstructor
public class Tokens {

    private String accessToken;
    private String refreshToken;

    public static Tokens of(Jwt jwt) {
        return new Tokens(jwt.getAccessToken(), jwt.getRefreshToken());
    }
}
