package xyz.fm.storerestapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fm.storerestapi.entity.Vendor;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.entity.duplicate.DuplicateVendorException;
import xyz.fm.storerestapi.exception.entity.duplicate.DuplicateVendorManagerException;
import xyz.fm.storerestapi.exception.value.duplicate.DuplicatePhoneException;
import xyz.fm.storerestapi.repository.VendorManagerRepository;
import xyz.fm.storerestapi.repository.VendorRepository;

@Service
@Transactional(readOnly = true)
public class VendorService {

    private final VendorRepository vendorRepository;
    private final VendorManagerRepository vendorManagerRepository;
    private final PasswordEncoder passwordEncoder;

    public VendorService(VendorRepository vendorRepository, VendorManagerRepository vendorManagerRepository, PasswordEncoder passwordEncoder) {
        this.vendorRepository = vendorRepository;
        this.vendorManagerRepository = vendorManagerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Vendor registerVendor(Vendor vendor, VendorManager executive) {
        duplicateCheckVendor(vendor);
        duplicateCheckVendorManager(executive);

        executive.encryptPassword(passwordEncoder);
        vendor.addManager(executive);

        return vendorRepository.save(vendor);
    }

    private void duplicateCheckVendor(Vendor vendor) {
        if (vendorRepository.existsByName(vendor.getName()))
            throw new DuplicateVendorException(ErrorCode.DUPLICATE_VENDOR_NAME);

        if (vendorRepository.existsByRegNumber(vendor.getRegNumber()))
            throw new DuplicateVendorException(ErrorCode.DUPLICATE_VENDOR_REG_NUMBER);
    }

    private void duplicateCheckVendorManager(VendorManager vendorManager) {
        if (vendorManagerRepository.existsByEmail(vendorManager.getEmail()))
            throw new DuplicateVendorManagerException(ErrorCode.DUPLICATE_EMAIL);

        vendorManagerRepository.findByPhone(vendorManager.getPhone())
                .ifPresent((vm) -> {
                    throw new DuplicatePhoneException(ErrorCode.DUPLICATE_PHONE, vm.getEmail());
                });
    }
}
