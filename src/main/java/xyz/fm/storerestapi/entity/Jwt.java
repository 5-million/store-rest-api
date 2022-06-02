package xyz.fm.storerestapi.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Jwt extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    private String email;
    private String accessToken;
    private String refreshToken;

    //== business ==//
    public void update(Jwt newJwt) {
        this.accessToken = newJwt.getAccessToken();
        this.refreshToken = newJwt.getRefreshToken();
    }
}
