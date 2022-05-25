package xyz.fm.storerestapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fm.storerestapi.dto.vendor.VendorInfo;
import xyz.fm.storerestapi.dto.vendor.VendorRegisterRequest;
import xyz.fm.storerestapi.entity.Vendor;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.service.VendorService;

import javax.validation.Valid;

@RestController
@RequestMapping("vendor")
public class VendorRestController {

    private final VendorService vendorService;

    public VendorRestController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping
    public ResponseEntity<VendorInfo> registerVendor(@Valid @RequestBody VendorRegisterRequest request) {
        Vendor vendor = request.toEntity();
        VendorManager executive = request.getExecutive().toEntity();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(VendorInfo.of(vendorService.registerVendor(vendor, executive)));
    }
}
