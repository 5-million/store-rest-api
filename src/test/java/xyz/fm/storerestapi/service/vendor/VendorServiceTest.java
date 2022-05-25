package xyz.fm.storerestapi.service.vendor;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import xyz.fm.storerestapi.repository.VendorManagerRepository;
import xyz.fm.storerestapi.repository.VendorRepository;
import xyz.fm.storerestapi.service.VendorService;

@ExtendWith(MockitoExtension.class)
public abstract class VendorServiceTest {

    @Mock protected VendorRepository vendorRepository;
    @Mock protected VendorManagerRepository vendorManagerRepository;
    @Mock protected PasswordEncoder passwordEncoder;
    @InjectMocks protected VendorService vendorService;
}