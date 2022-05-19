package xyz.fm.storerestapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fm.storerestapi.dto.Tokens;
import xyz.fm.storerestapi.dto.user.JwtReissueRequest;
import xyz.fm.storerestapi.dto.user.LoginRequest;
import xyz.fm.storerestapi.service.AuthService;

@RestController
@RequestMapping("auth")
public class AuthRestController {

    private final AuthService authService;

    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<Tokens> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(Tokens.of(authService.login(request)));
    }

    @PostMapping("token/reissue")
    public ResponseEntity<Tokens> reissue(@RequestBody JwtReissueRequest request) {
        return ResponseEntity.ok(Tokens.of(authService.reissueJwt(request)));
    }
}
