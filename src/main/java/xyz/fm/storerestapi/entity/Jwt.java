package xyz.fm.storerestapi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Jwt extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    private String email;
    private String accessToken;
    private String refreshToken;

    protected Jwt() {/* empty */}

    private Jwt(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.accessToken = builder.accessToken;
        this.refreshToken = builder.refreshToken;
    }

    //== business ==//
    public void update(Jwt newJwt) {
        this.accessToken = newJwt.getAccessToken();
        this.refreshToken = newJwt.getRefreshToken();
    }

    //== getter ==//
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    //== builder ==//
    public static class Builder {
        private Long id;
        private final String email;
        private final String accessToken;
        private final String refreshToken;

        public Builder(String email, String accessToken, String refreshToken) {
            this.email = email;
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Jwt build() {
            return new Jwt(this);
        }
    }
}
