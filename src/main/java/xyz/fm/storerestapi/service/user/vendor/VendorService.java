package xyz.fm.storerestapi.service.user.vendor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fm.storerestapi.dto.user.vendor.VendorManagerJoinRequest;
import xyz.fm.storerestapi.dto.user.vendor.VendorRegisterRequest;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.entity.user.vendor.VendorRole;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.DuplicationException;
import xyz.fm.storerestapi.error.exception.InvalidPasswordException;
import xyz.fm.storerestapi.error.exception.NotFoundException;
import xyz.fm.storerestapi.repository.user.vendor.VendorManagerRepository;
import xyz.fm.storerestapi.repository.user.vendor.VendorRepository;
import xyz.fm.storerestapi.util.EncryptUtil;

import javax.persistence.PersistenceException;

@Service
@Transactional(readOnly = true)
public class VendorService {

    private final VendorRepository vendorRepository;
    private final VendorManagerRepository vendorManagerRepository;

    public VendorService(VendorRepository vendorRepository, VendorManagerRepository vendorManagerRepository) {
        this.vendorRepository = vendorRepository;
        this.vendorManagerRepository = vendorManagerRepository;
    }

    public Boolean isExistsEmail(String email) {
        return vendorManagerRepository.existsByEmail(email);
    }

    public Boolean isExistsPhoneNumber(String phoneNumber) {
        return vendorManagerRepository.existsByPhoneNumber(phoneNumber);
    }

    @Transactional
    public Vendor register(VendorRegisterRequest request) {
        if (!request.getAdmin().getPassword().equals(request.getAdmin().getConfirmPassword())) {
            throw new InvalidPasswordException(Error.INVALID_PASSWORD, ErrorDetail.PWD_NOT_EQUAL_TO_CONFIRM_PWD);
        }

        if (vendorRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new DuplicationException(Error.DUPLICATE, ErrorDetail.DUPLICATE_VENDOR);
        }

        try {
            Vendor vendor = new Vendor.Builder(
                    request.getVendorName(),
                    request.getCeo(),
                    request.getRegistrationNumber(),
                    request.getLocation()
            ).build();

            VendorManager admin = new VendorManager.Builder(
                    request.getAdmin().getEmail(),
                    request.getAdmin().getName(),
                    EncryptUtil.encode(request.getAdmin().getPassword()),
                    request.getAdmin().getPhoneNumber(),
                    vendor
            )
                    .role(VendorRole.VENDOR_ROOT)
                    .permission(true)
                    .build();

            vendor.addManager(admin);
            vendorRepository.save(vendor);

            return vendor;
        } catch (PersistenceException e) {
            throw new DuplicationException(Error.DUPLICATE, ErrorDetail.DUPLICATE_VENDOR);
        }
    }

    @Transactional
    public VendorManager joinManager(VendorManagerJoinRequest request) {
        if (!request.isValidPassword()) {
            throw new InvalidPasswordException(Error.INVALID_PASSWORD, ErrorDetail.PWD_NOT_EQUAL_TO_CONFIRM_PWD);
        }

        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_VENDOR));

        try {
            VendorManager manager = new VendorManager.Builder(
                    request.getEmail(),
                    request.getName(),
                    EncryptUtil.encode(request.getPassword()),
                    request.getPhoneNumber(),
                    vendor
            ).build();

            return vendorManagerRepository.save(manager);
        } catch (PersistenceException e) {
            throw new DuplicationException(Error.DUPLICATE, ErrorDetail.DUPLICATE_USER);
        }
    }
}
