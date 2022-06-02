package xyz.fm.storerestapi.entity.user.consumer;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.fm.storerestapi.entity.user.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Consumer extends User {

    @Id @GeneratedValue
    @Column(name = "consumer_id")
    private Long id;

    @Embedded
    private AdReceive adReceive;

    @Builder
    private Consumer(Email email, String name, Phone phone, Password password, Long id, AdReceive adReceive) {
        super(email, name, phone, Role.ROLE_CONSUMER, password);
        this.id = id;
        this.adReceive =
                adReceive == null ? new AdReceive(true, false, true) : adReceive;
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
}
