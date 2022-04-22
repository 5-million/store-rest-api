package xyz.fm.storerestapi.entity.shipping;

import xyz.fm.storerestapi.entity.BaseEntity;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;

import javax.persistence.*;

@Entity
@Table(name = "store_shipping_address")
public class ShippingAddress extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "shipping_address_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    @Embedded
    private Address address;

    private Boolean defaultAddress;

    protected ShippingAddress() {/* empty */}

    private ShippingAddress(Builder builder) {
        this.id = builder.id;
        this.consumer = builder.consumer;
        this.address = builder.address;
        this.defaultAddress = builder.defaultAddress;
    }

    public Long getId() {
        return id;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public Address getAddress() {
        return address;
    }

    public Boolean isDefaultAddress() {
        return defaultAddress;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ShippingAddress(");
        sb.append("id: " + id);
        sb.append(", address: " + address.toString());
        sb.append(")");
        return sb.toString();
    }

    //== business ==//
    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    //== Builder ==//
    public static class Builder {
        private Long id;
        private Consumer consumer;
        private final Address address;
        private Boolean defaultAddress = false;

        public Builder(Address address) {
            this.address = address;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder consumer(Consumer consumer) {
            this.consumer = consumer;
            return this;
        }

        public Builder defaultAddress(boolean defaultAddress) {
            this.defaultAddress = defaultAddress;
            return this;
        }

        public ShippingAddress build() {
            return new ShippingAddress(this);
        }
    }
}
