package xyz.fm.storerestapi.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import xyz.fm.storerestapi.dto.user.DuplicationCheckResponse;
import xyz.fm.storerestapi.dto.user.EmailCheckRequest;
import xyz.fm.storerestapi.dto.user.PasswordChangeRequest;
import xyz.fm.storerestapi.dto.user.PhoneNumberCheckRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

public interface UserRestController {

    ResponseEntity<DuplicationCheckResponse> checkEmailDuplication(@Valid @RequestBody EmailCheckRequest request);
    ResponseEntity<DuplicationCheckResponse> checkPhoneNumberDuplication(@Valid @RequestBody PhoneNumberCheckRequest request);

    // security, jwt 구현 후 수정
    void changePassword(@RequestBody PasswordChangeRequest request, HttpServletRequest httpRequest);
}
