package xyz.fm.storerestapi.dto.user.consumer;

import xyz.fm.storerestapi.entity.user.consumer.Consumer;

public class ConsumerLoginResponse {

    private Long consumerId;
    private String email;
    private String name;
    private Integer addedCartCount;

    public ConsumerLoginResponse(Consumer consumer) {
        this.consumerId = consumer.getId();
        this.email = consumer.getEmail();
        this.name = consumer.getName();
        this.addedCartCount = 0;
    }

    public ConsumerLoginResponse(Long consumerId, String email, String name, Integer addedCartCount) {
        this.consumerId = consumerId;
        this.email = email;
        this.name = name;
        this.addedCartCount = addedCartCount;
    }

    public Long getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Long consumerId) {
        this.consumerId = consumerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAddedCartCount() {
        return addedCartCount;
    }

    public void setAddedCartCount(Integer addedCartCount) {
        this.addedCartCount = addedCartCount;
    }
}
