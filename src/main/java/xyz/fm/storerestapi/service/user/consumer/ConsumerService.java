package xyz.fm.storerestapi.service.user.consumer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fm.storerestapi.dto.user.LoginRequest;
import xyz.fm.storerestapi.dto.user.UserJoinRequest;
import xyz.fm.storerestapi.dto.user.WithdrawalRequest;
import xyz.fm.storerestapi.dto.user.consumer.ConsumerJoinRequest;
import xyz.fm.storerestapi.entity.user.consumer.AdsReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.DuplicationException;
import xyz.fm.storerestapi.error.exception.InvalidPasswordException;
import xyz.fm.storerestapi.error.exception.TypeMismatchException;
import xyz.fm.storerestapi.repository.user.consumer.ConsumerRepository;
import xyz.fm.storerestapi.service.user.UserService;
import xyz.fm.storerestapi.util.EncryptUtil;

import javax.persistence.PersistenceException;

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
        if (!(request instanceof ConsumerJoinRequest)) {
            throw new TypeMismatchException(Error.TYPE_MISMATCH, ErrorDetail.NOT_CONSUMER_JOIN_REQUEST, true);
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidPasswordException(Error.INVALID_PASSWORD, ErrorDetail.PWD_NOT_EQUAL_TO_CONFIRM_PWD);
        }

        try {
            String encodedPassword = EncryptUtil.encode(request.getPassword());
            ConsumerJoinRequest.AdsReceive adsReceive = ((ConsumerJoinRequest) request).getAdsReceive();

            Consumer newConsumer = new Consumer.Builder(
                    request.getEmail(),
                    request.getName(),
                    encodedPassword,
                    request.getPhoneNumber(),
                    new AdsReceive(adsReceive.getToEmail(), adsReceive.getToSMSAndMMS(), adsReceive.getToAppPush())
            ).build();

            return consumerRepository.save(newConsumer);
        } catch (PersistenceException e) {
            throw new DuplicationException(Error.DUPLICATE, ErrorDetail.DUPLICATE_USER);
        }
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
