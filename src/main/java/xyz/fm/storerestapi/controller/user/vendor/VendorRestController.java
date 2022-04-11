package xyz.fm.storerestapi.controller.user.vendor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.fm.storerestapi.dto.user.vendor.VendorList;
import xyz.fm.storerestapi.dto.user.vendor.VendorRegisterRequest;
import xyz.fm.storerestapi.repository.user.vendor.VendorApiRepository;
import xyz.fm.storerestapi.service.user.vendor.VendorService;

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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void register(@Valid @RequestBody VendorRegisterRequest request) {
        vendorService.register(request);
    }

    @GetMapping
    public ResponseEntity<VendorList> getAll() {
        return ResponseEntity.ok(new VendorList(vendorApiRepository.findAllVendorInfoBy()));
    }
}
