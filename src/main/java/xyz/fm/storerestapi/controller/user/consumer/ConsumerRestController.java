package xyz.fm.storerestapi.controller.user.consumer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.fm.storerestapi.controller.user.UserRestController;
import xyz.fm.storerestapi.dto.user.*;
import xyz.fm.storerestapi.dto.user.consumer.ConsumerInfo;
import xyz.fm.storerestapi.dto.user.consumer.ConsumerJoinRequest;
import xyz.fm.storerestapi.dto.user.consumer.ConsumerLoginResponse;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.UnauthorizedException;
import xyz.fm.storerestapi.jwt.JwtTokenUtil;
import xyz.fm.storerestapi.service.user.consumer.ConsumerService;
import xyz.fm.storerestapi.util.PhoneNumberUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("api/consumer")
public class ConsumerRestController implements UserRestController {

    private final ConsumerService consumerService;
    private final JwtTokenUtil jwtTokenUtil;

    public ConsumerRestController(ConsumerService consumerService, JwtTokenUtil jwtTokenUtil) {
        this.consumerService = consumerService;
        this.jwtTokenUtil = jwtTokenUtil;
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

    @PostMapping("login")
    public ResponseEntity<ConsumerLoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        Consumer consumer = consumerService.login(request);
        response.addCookie(jwtTokenUtil.generateTokenCookie(consumer));
        return ResponseEntity.ok(new ConsumerLoginResponse(consumer));
    }

    @PostMapping("me")
    public ResponseEntity<ConsumerInfo> me(@RequestBody MyInfoRequest request, HttpServletRequest httpRequest) {
        // spring security, jwt 구현 후 수정필요
        String token = Optional.ofNullable(httpRequest.getHeader(JwtTokenUtil.JWT_KEY))
                .orElseThrow(() -> new UnauthorizedException(Error.UNAUTHORIZED, ErrorDetail.UNAUTHORIZED));

        Consumer consumer = consumerService.getByEmail(jwtTokenUtil.getEmailFromToken(token));

        if (!consumer.isMatchedPassword(request.getPassword())) {
            throw new UnauthorizedException(Error.UNAUTHORIZED, ErrorDetail.INCORRECT_PWD);
        }

        return ResponseEntity.ok(new ConsumerInfo(consumer));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("withdrawal")
    public void withdrawal(@RequestBody WithdrawalRequest request, HttpServletRequest httpRequest) {
        // spring security, jwt 구현 후 수정필요
        String token = Optional.ofNullable(httpRequest.getHeader(JwtTokenUtil.JWT_KEY))
                .orElseThrow(() -> new UnauthorizedException(Error.UNAUTHORIZED, ErrorDetail.UNAUTHORIZED));

        String email = jwtTokenUtil.getEmailFromToken(token);

        consumerService.withdrawal(email, request);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("change/pwd")
    public void changePassword(@RequestBody PasswordChangeRequest request, HttpServletRequest httpRequest) {
        // spring security, jwt 구현 후 수정필요
        String token = Optional.ofNullable(httpRequest.getHeader(JwtTokenUtil.JWT_KEY))
                .orElseThrow(() -> new UnauthorizedException(Error.UNAUTHORIZED, ErrorDetail.UNAUTHORIZED));

        String email = jwtTokenUtil.getEmailFromToken(token);

        consumerService.changePassword(email, request);
    }
}
