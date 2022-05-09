package xyz.fm.storerestapi.dto.user.consumer;

import xyz.fm.storerestapi.entity.user.consumer.AdReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;

public class ConsumerInfo {

    private String email;
    private String name;
    private String phone;
    private AdReceive adReceive;

    public static ConsumerInfo of(Consumer consumer) {
        ConsumerInfo consumerInfo = new ConsumerInfo();
        consumerInfo.email = consumer.getEmail().toString();
        consumerInfo.name = consumer.getName();
        consumerInfo.phone = consumer.getPhone().toString();
        consumerInfo.adReceive = consumer.getAdReceive();

        return consumerInfo;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public AdReceive getAdReceive() {
        return adReceive;
    }
}
