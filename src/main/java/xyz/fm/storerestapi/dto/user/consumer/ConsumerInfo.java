package xyz.fm.storerestapi.dto.user.consumer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.fm.storerestapi.entity.user.consumer.AdReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;

@Getter
@NoArgsConstructor
@AllArgsConstructor
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
}
