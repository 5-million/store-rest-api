package xyz.fm.storerestapi.service.user.consumer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fm.storerestapi.dto.user.LoginRequest;
import xyz.fm.storerestapi.dto.user.UserJoinRequest;
import xyz.fm.storerestapi.dto.user.WithdrawalRequest;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.repository.user.consumer.ConsumerRepository;
import xyz.fm.storerestapi.service.user.UserService;

@Service
@Transactional(readOnly = true)
public class ConsumerService implements UserService<Consumer> {

    private final ConsumerRepository consumerRepository;

    public ConsumerService(ConsumerRepository consumerRepository) {
        this.consumerRepository = consumerRepository;
    }

    @Override
    public Boolean isExistEmail(String email) {
        return consumerRepository.existsByEmail(email);
    }

    @Override
    public Boolean isExistPhoneNumber(String phoneNumber) {
        return consumerRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public Consumer join(UserJoinRequest request) {
        return null;
    }

    @Override
    public Consumer login(LoginRequest request) {
        return null;
    }

    @Override
    public Consumer modify(UserJoinRequest request) {
        return null;
    }

    @Override
    public int withdrawal(WithdrawalRequest request) {
        return 0;
    }
}
