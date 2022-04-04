package xyz.fm.storerestapi.controller.user.consumer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.fm.storerestapi.controller.user.UserRestController;
import xyz.fm.storerestapi.dto.user.DuplicationCheckResponse;
import xyz.fm.storerestapi.dto.user.EmailCheckRequest;
import xyz.fm.storerestapi.dto.user.PhoneNumberCheckRequest;
import xyz.fm.storerestapi.dto.user.consumer.ConsumerJoinRequest;
import xyz.fm.storerestapi.service.user.consumer.ConsumerService;
import xyz.fm.storerestapi.util.PhoneNumberUtil;

import javax.validation.Valid;

@RestController
@RequestMapping("api/consumer")
public class ConsumerRestController implements UserRestController {

    private final ConsumerService consumerService;

    public ConsumerRestController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @Override
    @PostMapping("join/check/email")
    public ResponseEntity<DuplicationCheckResponse> checkEmailDuplication(
            @Valid @RequestBody EmailCheckRequest request) {
        return ResponseEntity.ok().body(
                new DuplicationCheckResponse(consumerService.isExistEmail(request.getEmail()))
        );
    }

    @Override
    @PostMapping("join/check/phone")
    public ResponseEntity<DuplicationCheckResponse> checkPhoneNumberDuplication(
            @Valid @RequestBody PhoneNumberCheckRequest request) {
        String phoneNumber = PhoneNumberUtil.valueOf(request.getPhoneNumber());
        return ResponseEntity.ok().body(
                new DuplicationCheckResponse(consumerService.isExistPhoneNumber(phoneNumber))
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("join")
    public void join(@Valid @RequestBody ConsumerJoinRequest request) {
        consumerService.join(request);
    }
}
