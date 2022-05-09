package xyz.fm.storerestapi.service.consumer;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import xyz.fm.storerestapi.repository.ConsumerRepository;
import xyz.fm.storerestapi.service.ConsumerService;

@ExtendWith(MockitoExtension.class)
public abstract class ConsumerServiceTest {

    @InjectMocks protected ConsumerService consumerService;
    @Mock protected ConsumerRepository consumerRepository;
    @Mock protected PasswordEncoder passwordEncoder;
}
