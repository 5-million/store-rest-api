package xyz.fm.storerestapi.dto.user.consumer;

import xyz.fm.storerestapi.entity.user.consumer.Consumer;

public class ConsumerInfo {

    private final Long consumerId;
    private final String email;
    private final String name;
    private final String phoneNumber;
    private final ConsumerJoinRequest.AdsReceive adsReceive;

    public ConsumerInfo(Consumer consumer) {
        this.consumerId = consumer.getId();
        this.email = consumer.getEmail();
        this.name = consumer.getName();
        this.phoneNumber = consumer.getPhoneNumber();
        this.adsReceive = new ConsumerJoinRequest.AdsReceive(
                consumer.getAdsReceive().getToEmail(),
                consumer.getAdsReceive().getToSMSAndMMS(),
                consumer.getAdsReceive().getToAppPush()
        );
    }

    public Long getConsumerId() {
        return consumerId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ConsumerJoinRequest.AdsReceive getAdsReceive() {
        return adsReceive;
    }
}
