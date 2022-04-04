package xyz.fm.storerestapi.repository.user.consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import xyz.fm.storerestapi.entity.user.consumer.AdsReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ConsumerRepositoryTest {

    @Autowired private EntityManager em;
    @Autowired private ConsumerRepository consumerRepository;

    private final Consumer testConsumer = new Consumer.Builder(
            "abc@test.com", "name", "password", "01012345678",
            new AdsReceive(true, true, true)
    ).reserves(20000L).build();

    @Test
    @DisplayName("save success")
    public void save_success() {
        consumerRepository.save(testConsumer);

        Consumer findConsumer = consumerRepository.findById(testConsumer.getId()).get();
        AdsReceive findAdsReceive = em.find(AdsReceive.class, testConsumer.getAdsReceive().getId());

        assertThat(findConsumer).isEqualTo(testConsumer);
        assertThat(findConsumer.getAdsReceive()).isEqualTo(findAdsReceive);
    }

    @Test
    @DisplayName("existsByEmail: email is null")
    public void existsByEmail_emailIsNull() {
        consumerRepository.save(testConsumer);

        boolean result = consumerRepository.existsByEmail(null);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("existsByEmail: email is blank")
    public void existsByEmail_emailIsBlank() {
        consumerRepository.save(testConsumer);

        boolean result = consumerRepository.existsByEmail("");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("existsByEmail: exist consumer")
    public void existsByEmail_existConsumer() {
        consumerRepository.save(testConsumer);

        boolean result = consumerRepository.existsByEmail(testConsumer.getEmail());

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("existsByPhoneNumber: PhoneNumber is null")
    public void existsByPhoneNumber_phoneNumberIsNull() {
        consumerRepository.save(testConsumer);

        boolean result = consumerRepository.existsByPhoneNumber(null);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("existsByPhoneNumber: PhoneNumber is blank")
    public void existsByPhoneNumber_phoneNumberIsBlank() {
        consumerRepository.save(testConsumer);

        boolean result = consumerRepository.existsByPhoneNumber("");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("existsByPhoneNumber: exist consumer")
    public void existsByPhoneNumber_existConsumer() {
        consumerRepository.save(testConsumer);

        boolean result = consumerRepository.existsByPhoneNumber(testConsumer.getPhoneNumber());

        assertThat(result).isTrue();
    }
}