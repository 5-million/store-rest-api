package xyz.fm.storerestapi.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fm.storerestapi.entity.Vendor;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.entity.duplicate.DuplicateVendorException;
import xyz.fm.storerestapi.exception.entity.duplicate.DuplicateVendorManagerException;
import xyz.fm.storerestapi.exception.entity.notfound.VendorManagerNotFoundException;
import xyz.fm.storerestapi.exception.entity.notfound.VendorNotFoundException;
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

    @Transactional
    public VendorManager joinVendorManager(long vendorId, VendorManager vendorManager) {
        Vendor vendor = getVendorById(vendorId);
        duplicateCheckVendorManager(vendorManager);

        vendorManager.encryptPassword(passwordEncoder);
        vendor.addManager(vendorManager);
        return vendorManager;
    }

    public Vendor getVendorById(long vendorId) {
        return vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorNotFoundException(ErrorCode.VENDOR_NOT_FOUND));
    }

    public VendorManager getVendorManagerById(Long id) {
        return vendorManagerRepository.findById(id)
                .orElseThrow(() -> new VendorManagerNotFoundException(ErrorCode.VENDOR_MANAGER_NOT_FOUND));
    }

    public VendorManager getVendorManagerByEmail(Email email) {
        return vendorManagerRepository.findByEmail(email)
                .orElseThrow(() -> new VendorManagerNotFoundException(ErrorCode.VENDOR_MANAGER_NOT_FOUND));
    }

    @Transactional
    public void approveManager(VendorManager executive, VendorManager target) {
        executive.approve(target);
    }

    public void duplicateCheckVendor(Vendor vendor) {
        if (vendorRepository.existsByName(vendor.getName()))
            throw new DuplicateVendorException(ErrorCode.DUPLICATE_VENDOR_NAME);

        if (vendorRepository.existsByRegNumber(vendor.getRegNumber()))
            throw new DuplicateVendorException(ErrorCode.DUPLICATE_VENDOR_REG_NUMBER);
    }

    public void duplicateCheckVendorManager(VendorManager vendorManager) {
        if (vendorManagerRepository.existsByEmail(vendorManager.getEmail()))
            throw new DuplicateVendorManagerException(ErrorCode.DUPLICATE_EMAIL);

        vendorManagerRepository.findByPhone(vendorManager.getPhone())
                .ifPresent((vm) -> {
                    throw new DuplicatePhoneException(ErrorCode.DUPLICATE_PHONE, vm.getEmail());
                });
    }
}
