package xyz.fm.storerestapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.value.duplicate.DuplicateEmailException;
import xyz.fm.storerestapi.exception.value.duplicate.DuplicatePhoneException;
import xyz.fm.storerestapi.repository.ConsumerRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ConsumerService {

    private final ConsumerRepository consumerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Consumer join(Consumer consumer) {
        if (consumerRepository.existsByEmail(consumer.getEmail()))
            throw new DuplicateEmailException(ErrorCode.DUPLICATE_EMAIL);

        consumerRepository.findByPhone(consumer.getPhone())
                .ifPresent(c -> {
                    throw new DuplicatePhoneException(ErrorCode.DUPLICATE_PHONE, c.getEmail());
                });

        consumer.encryptPassword(passwordEncoder);
        return consumerRepository.save(consumer);
    }
}
