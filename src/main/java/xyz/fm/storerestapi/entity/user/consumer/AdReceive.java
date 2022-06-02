package xyz.fm.storerestapi.entity.user.consumer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AdReceive {

    @Column(name = "ad_receive_to_email")
    private Boolean toEmail;

    @Column(name = "ad_receive_to_message")
    private Boolean toMessage;

    @Column(name = "ad_receive_to_app_push")
    private Boolean toAppPush;

    @Override
    public String toString() {
        return "AdReceive(toEmail: " + toEmail
                + ", toMessage: " + toMessage
                + ", toAppPush: " + toAppPush
                + ")";
    }
}
