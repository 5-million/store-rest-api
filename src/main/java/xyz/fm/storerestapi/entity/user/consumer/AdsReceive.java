package xyz.fm.storerestapi.entity.user.consumer;

import xyz.fm.storerestapi.entity.BaseTimeEntity;

import javax.persistence.*;

@Entity
public class AdsReceive extends BaseTimeEntity {

    @Id @GeneratedValue
    Long id;

    private Boolean toEmail = true;

    @Column(name = "to_sms_and_mms")
    private Boolean toSMSAndMMS = true;

    private Boolean toAppPush = true;

    @OneToOne(mappedBy = "adsReceive")
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    public AdsReceive() {/* empty */}

    public AdsReceive(Boolean toEmail, Boolean toSMSAndMMS, Boolean toAppPush) {
        this.toEmail = toEmail;
        this.toSMSAndMMS = toSMSAndMMS;
        this.toAppPush = toAppPush;
    }

    public Long getId() {
        return id;
    }

    public Boolean getToEmail() {
        return toEmail;
    }

    public Boolean getToSMSAndMMS() {
        return toSMSAndMMS;
    }

    public Boolean getToAppPush() {
        return toAppPush;
    }

    public Consumer getConsumer() {
        return consumer;
    }
}
