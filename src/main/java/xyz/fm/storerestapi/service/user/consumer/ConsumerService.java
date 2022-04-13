package xyz.fm.storerestapi.service.user.consumer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fm.storerestapi.dto.user.LoginRequest;
import xyz.fm.storerestapi.dto.user.PasswordChangeRequest;
import xyz.fm.storerestapi.dto.user.UserJoinRequest;
import xyz.fm.storerestapi.dto.user.WithdrawalRequest;
import xyz.fm.storerestapi.dto.user.consumer.ConsumerJoinRequest;
import xyz.fm.storerestapi.entity.user.consumer.AdsReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.*;
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
    public Consumer getByEmail(String email) {
        return consumerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_USER));
    }

    @Override
    @Transactional
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
    @Transactional
    public Consumer login(LoginRequest request) {
        Consumer consumer = consumerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new LoginException(Error.LOGIN_FAIL, ErrorDetail.NOT_FOUND_USER, true));

        if (!consumer.login(request.getPassword())) {
            throw new LoginException(Error.LOGIN_FAIL, ErrorDetail.INCORRECT_PWD, true);
        }

        return consumer;
    }

    @Override
    @Transactional
    public Boolean withdrawal(String email, WithdrawalRequest request) {
        Consumer consumer = getByEmail(email);

        if (!consumer.isMatchedName(request.getName())) {
            throw new UnauthorizedException(Error.UNAUTHORIZED, ErrorDetail.INCORRECT_NAME);
        }

        if (!consumer.isMatchedPassword(request.getPassword())) {
            throw new UnauthorizedException(Error.UNAUTHORIZED, ErrorDetail.INCORRECT_PWD);
        }

        consumerRepository.delete(consumer);
        return true;
    }

    @Override
    @Transactional
    public void changePassword(String email, PasswordChangeRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new InvalidPasswordException(Error.INVALID_PASSWORD, ErrorDetail.PWD_NOT_EQUAL_TO_CONFIRM_PWD);
        }

        Consumer consumer = getByEmail(email);

        consumer.changePassword(request.getOldPassword(), request.getNewPassword());
    }
}
