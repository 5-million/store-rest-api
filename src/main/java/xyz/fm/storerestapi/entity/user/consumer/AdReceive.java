package xyz.fm.storerestapi.entity.user.consumer;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
public class AdReceive {

    @Column(name = "ad_receive_to_email")
    private Boolean toEmail;

    @Column(name = "ad_receive_to_message")
    private Boolean toMessage;

    @Column(name = "ad_receive_to_app_push")
    private Boolean toAppPush;

    protected AdReceive() {/* empty */}

    public AdReceive(Boolean toEmail, Boolean toMessage, Boolean toAppPush) {
        this.toEmail = toEmail;
        this.toMessage = toMessage;
        this.toAppPush = toAppPush;
    }

    public Boolean getToEmail() {
        return toEmail;
    }

    public Boolean getToMessage() {
        return toMessage;
    }

    public Boolean getToAppPush() {
        return toAppPush;
    }

    @Override
    public String toString() {
        return "AdReceive(toEmail: " + toEmail
                + ", toMessage: " + toMessage
                + ", toAppPush: " + toAppPush
                + ")";
    }
}
