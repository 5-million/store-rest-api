package xyz.fm.storerestapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.fm.storerestapi.dto.vendor.*;
import xyz.fm.storerestapi.entity.Vendor;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.repository.query.VendorManagerQueryRepository;
import xyz.fm.storerestapi.service.VendorService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("vendor")
@RequiredArgsConstructor
public class VendorRestController {

    private final VendorService vendorService;
    private final VendorManagerQueryRepository vendorManagerQueryRepository;

    @PostMapping
    public ResponseEntity<VendorInfo> registerVendor(@Valid @RequestBody VendorRegisterRequest request) {
        Vendor vendor = request.toEntity();
        VendorManager executive = request.getExecutive().toEntity();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(VendorInfo.of(vendorService.registerVendor(vendor, executive)));
    }

    @PostMapping("manager")
    public ResponseEntity<VendorManagerInfo> registerVendorManager(@Valid @RequestBody VendorManagerJoinRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        VendorManagerInfo.of(vendorService.joinVendorManager(request.getVendorId(), request.toEntity()))
                );
    }

    @GetMapping("manager")
    public ResponseEntity<VendorManagerList> getManagerList(Principal principal) {
        Vendor vendor = vendorService.getVendorManagerByEmail(new Email(principal.getName())).getVendor();

        return ResponseEntity.ok(
                new VendorManagerList(vendorManagerQueryRepository.findAllByVendorId(vendor))
        );
    }

    @PostMapping("manager/{targetId}/approve")
    public void approveManager(@PathVariable("targetId") Long targetId, Principal principal) {
        VendorManager executive = vendorService.getVendorManagerByEmail(new Email(principal.getName()));
        VendorManager target = vendorService.getVendorManagerById(targetId);
        vendorService.approveManager(executive, target);
    }
}
