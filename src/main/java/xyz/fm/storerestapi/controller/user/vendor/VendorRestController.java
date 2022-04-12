package xyz.fm.storerestapi.controller.user.vendor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.fm.storerestapi.dto.user.DuplicationCheckResponse;
import xyz.fm.storerestapi.dto.user.EmailCheckRequest;
import xyz.fm.storerestapi.dto.user.PhoneNumberCheckRequest;
import xyz.fm.storerestapi.dto.user.vendor.VendorList;
import xyz.fm.storerestapi.dto.user.vendor.VendorManagerJoinRequest;
import xyz.fm.storerestapi.dto.user.vendor.VendorManagerList;
import xyz.fm.storerestapi.dto.user.vendor.VendorRegisterRequest;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NoPermissionException;
import xyz.fm.storerestapi.error.exception.UnauthorizedException;
import xyz.fm.storerestapi.jwt.JwtTokenUtil;
import xyz.fm.storerestapi.repository.user.vendor.VendorApiRepository;
import xyz.fm.storerestapi.repository.user.vendor.VendorManagerApiRepository;
import xyz.fm.storerestapi.service.user.vendor.VendorService;
import xyz.fm.storerestapi.util.PhoneNumberUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("api/vendor")
public class VendorRestController {

    private final VendorService vendorService;
    private final VendorApiRepository vendorApiRepository;
    private final VendorManagerApiRepository vendorManagerApiRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public VendorRestController(VendorService vendorService, VendorApiRepository vendorApiRepository, VendorManagerApiRepository vendorManagerApiRepository, JwtTokenUtil jwtTokenUtil) {
        this.vendorService = vendorService;
        this.vendorApiRepository = vendorApiRepository;
        this.vendorManagerApiRepository = vendorManagerApiRepository;
        this.jwtTokenUtil = jwtTokenUtil;
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

    @GetMapping("manager")
    public ResponseEntity<VendorManagerList> getManagerList(HttpServletRequest httpRequest) {
        String token = Optional.ofNullable(httpRequest.getHeader(JwtTokenUtil.JWT_KEY))
                .orElseThrow(() -> new UnauthorizedException(Error.UNAUTHORIZED, ErrorDetail.UNAUTHORIZED));

        VendorManager manager = vendorService.getManagerByEmail(jwtTokenUtil.getEmailFromToken(token));

        if (!manager.isAdmin()) {
            throw new NoPermissionException(Error.NO_PERMISSION, ErrorDetail.NOT_ADMIN);
        }

        return ResponseEntity.ok(
                new VendorManagerList(vendorManagerApiRepository.findByVendor(manager.getVendor()))
        );
    }
}
