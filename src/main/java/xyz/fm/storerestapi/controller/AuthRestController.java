package xyz.fm.storerestapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.fm.storerestapi.dto.Tokens;
import xyz.fm.storerestapi.dto.user.JwtReissueRequest;
import xyz.fm.storerestapi.dto.user.LoginRequest;
import xyz.fm.storerestapi.service.AuthService;

import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("auth")
@Validated
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<Tokens> login(
            @RequestBody LoginRequest request,
            @RequestParam("type") @Pattern(regexp = "^(csm|vm)$", message = "type is only csm or vm.") String type) {
        return ResponseEntity.ok(Tokens.of(authService.login(request, type)));
    }

    @PostMapping("token/reissue")
    public ResponseEntity<Tokens> reissue(@RequestBody JwtReissueRequest request) {
        return ResponseEntity.ok(Tokens.of(authService.reissueJwt(request)));
    }
}
