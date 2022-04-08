package xyz.fm.storerestapi.controller.user.vendor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import xyz.fm.storerestapi.dto.user.vendor.VendorRegisterRequest;
import xyz.fm.storerestapi.service.user.vendor.VendorService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/vendor")
public class VendorRestController {

    private final VendorService vendorService;

    public VendorRestController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void register(@Valid @RequestBody VendorRegisterRequest request) {
        vendorService.register(request);
    }
}
