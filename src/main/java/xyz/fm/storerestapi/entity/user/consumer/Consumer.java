package xyz.fm.storerestapi.entity.user.consumer;

import xyz.fm.storerestapi.entity.user.BaseUserEntity;

import javax.persistence.*;

@Entity
@Table(name = "STORE_CONSUMER")
public class Consumer extends BaseUserEntity {

    @Id @GeneratedValue
    @Column(name = "consumer_id")
    private Long id;

    private Long reserves = 0L;

    protected Consumer() {/* empty */}

    private Consumer(Builder builder) {
        super(builder.email, builder.name, builder.password, builder.phoneNumber);
        this.id = builder.id;
        this.reserves = builder.reserves;
    }

    public Long getId() {
        return id;
    }

    public Long getReserves() {
        return reserves;
    }

    public static class Builder {
        private Long id;
        private final String email;
        private final String name;
        private final String password;
        private final String phoneNumber;
        private Long reserves = 0L;

        public Builder(String email, String name, String password, String phoneNumber) {
            this.email = email;
            this.name = name;
            this.password = password;
            this.phoneNumber = phoneNumber;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder reserves(Long reserves) {
            this.reserves = reserves;
            return this;
        }

        public Consumer build() {
            return new Consumer(this);
        }
    }
}
