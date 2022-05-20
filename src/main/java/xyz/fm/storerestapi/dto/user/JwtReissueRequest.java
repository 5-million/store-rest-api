package xyz.fm.storerestapi.dto.user;

public class JwtReissueRequest {

    private String accessToken;
    private String refreshToken;

    public JwtReissueRequest() {/* empty */}

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
