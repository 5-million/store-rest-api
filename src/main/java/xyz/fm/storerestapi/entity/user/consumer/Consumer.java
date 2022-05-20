package xyz.fm.storerestapi.entity.user.consumer;

import xyz.fm.storerestapi.entity.user.*;

import javax.persistence.*;

@Entity
public class Consumer extends User {

    @Id @GeneratedValue
    @Column(name = "consumer_id")
    private Long id;

    @Embedded
    private AdReceive adReceive;

    protected Consumer() {/* empty */}

    private Consumer(Builder builder) {
        super(builder.email, builder.name, builder.phone, builder.password, Role.ROLE_CONSUMER);
        id = builder.id;
        adReceive = builder.adReceive;
    }

    public Long getId() {
        return id;
    }

    public AdReceive getAdReceive() {
        return adReceive;
    }

    @Override
    public String toString() {
        return "Consumer(" + "id: " + id +
                ", email: " + getEmail() +
                ", name: " + getName() +
                ", phone: " + getPhone() +
                ", password: " + getPassword() +
                ", createdDate: " + getCreatedDate() +
                ", lastModifiedDate: " + getLastModifiedDate() +
                ", " + adReceive.toString() +
                ")";
    }

    //== builder ==//
    public static class Builder {
        private Long id;
        private final Email email;
        private final String name;
        private final Phone phone;
        private final Password password;
        private AdReceive adReceive = new AdReceive(true, true, true);

        public Builder(Email email, String name, Phone phone, Password password) {
            this.email = email;
            this.name = name;
            this.phone = phone;
            this.password = password;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder adReceive(AdReceive adReceive) {
            this.adReceive = adReceive;
            return this;
        }

        public Consumer build() {
            return new Consumer(this);
        }
    }
}
