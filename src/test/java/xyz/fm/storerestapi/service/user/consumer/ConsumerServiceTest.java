package xyz.fm.storerestapi.service.user.consumer;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.fm.storerestapi.repository.user.consumer.ConsumerRepository;

@ExtendWith(MockitoExtension.class)
class ConsumerServiceTest {

    @Mock
    protected ConsumerRepository consumerRepository;

    @InjectMocks
    protected ConsumerService consumerService;
}