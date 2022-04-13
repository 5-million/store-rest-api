package xyz.fm.storerestapi.dto.user.consumer;

import xyz.fm.storerestapi.dto.user.CommonUserJoinElement;

public class ConsumerJoinRequest extends CommonUserJoinElement {

    private AdsReceive adsReceive;

    public ConsumerJoinRequest() {/* empty */}

    public ConsumerJoinRequest(String email, String name, String password, String confirmPassword, String phoneNumber, AdsReceive adsReceive) {
        super(email, name, password, confirmPassword, phoneNumber);
        this.adsReceive = adsReceive;
    }

    public AdsReceive getAdsReceive() {
        return adsReceive;
    }

    public static class AdsReceive {
        Boolean toEmail;
        Boolean toSMSAndMMS;
        Boolean toAppPush;

        public AdsReceive() {/* empty */}

        public AdsReceive(Boolean toEmail, Boolean toSMSAndMMS, Boolean toAppPush) {
            this.toEmail = toEmail;
            this.toSMSAndMMS = toSMSAndMMS;
            this.toAppPush = toAppPush;
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
    }
}
