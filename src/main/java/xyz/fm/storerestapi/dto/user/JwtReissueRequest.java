package xyz.fm.storerestapi.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtReissueRequest {

    private String accessToken;
    private String refreshToken;
}
