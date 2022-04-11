package xyz.fm.storerestapi.controller.user.vendor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.fm.storerestapi.dto.user.DuplicationCheckResponse;
import xyz.fm.storerestapi.dto.user.EmailCheckRequest;
import xyz.fm.storerestapi.dto.user.PhoneNumberCheckRequest;
import xyz.fm.storerestapi.dto.user.vendor.VendorList;
import xyz.fm.storerestapi.dto.user.vendor.VendorManagerJoinRequest;
import xyz.fm.storerestapi.dto.user.vendor.VendorRegisterRequest;
import xyz.fm.storerestapi.repository.user.vendor.VendorApiRepository;
import xyz.fm.storerestapi.service.user.vendor.VendorService;
import xyz.fm.storerestapi.util.PhoneNumberUtil;

import javax.validation.Valid;

@RestController
@RequestMapping("api/vendor")
public class VendorRestController {

    private final VendorService vendorService;
    private final VendorApiRepository vendorApiRepository;

    public VendorRestController(VendorService vendorService, VendorApiRepository vendorApiRepository) {
        this.vendorService = vendorService;
        this.vendorApiRepository = vendorApiRepository;
    }

    @PostMapping("manager/join/check/email")
    ResponseEntity<DuplicationCheckResponse> checkEmailDuplication(@Valid @RequestBody EmailCheckRequest request) {
        return ResponseEntity.ok(
                new DuplicationCheckResponse(vendorService.isExistsEmail(request.getEmail()))
        );
    }

    @PostMapping("manager/join/check/phone")
    ResponseEntity<DuplicationCheckResponse> checkPhoneNumberDuplication(@Valid @RequestBody PhoneNumberCheckRequest request) {
        String phoneNumber = PhoneNumberUtil.valueOf(request.getPhoneNumber());
        return ResponseEntity.ok(
                new DuplicationCheckResponse(vendorService.isExistsPhoneNumber(phoneNumber))
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void register(@Valid @RequestBody VendorRegisterRequest request) {
        vendorService.register(request);
    }

    @GetMapping
    public ResponseEntity<VendorList> getAll() {
        return ResponseEntity.ok(new VendorList(vendorApiRepository.findAllVendorInfoBy()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("manager")
    public void joinManager(@Valid @RequestBody VendorManagerJoinRequest request) {
        vendorService.joinManager(request);
    }
}
