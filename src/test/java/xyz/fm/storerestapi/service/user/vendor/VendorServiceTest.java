package xyz.fm.storerestapi.service.user.vendor;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import xyz.fm.storerestapi.repository.user.vendor.VendorManagerRepository;
import xyz.fm.storerestapi.repository.user.vendor.VendorRepository;

@ExtendWith(MockitoExtension.class)
abstract class VendorServiceTest {

    @Mock protected VendorRepository vendorRepository;
    @Mock protected VendorManagerRepository vendorManagerRepository;
    @InjectMocks protected VendorService vendorService;

}