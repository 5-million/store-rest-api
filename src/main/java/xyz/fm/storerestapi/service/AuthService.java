package xyz.fm.storerestapi.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fm.storerestapi.dto.user.JwtReissueRequest;
import xyz.fm.storerestapi.dto.user.LoginRequest;
import xyz.fm.storerestapi.entity.Jwt;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.entity.notfound.JwtNotFoundException;
import xyz.fm.storerestapi.exception.value.invalid.LoginException;
import xyz.fm.storerestapi.exception.value.invalid.jwt.CustomJwtException;
import xyz.fm.storerestapi.jwt.JwtProvider;
import xyz.fm.storerestapi.jwt.TokenType;
import xyz.fm.storerestapi.repository.JwtRepository;

@Service
@Transactional
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;
    private final JwtRepository jwtRepository;

    public AuthService(AuthenticationManagerBuilder authenticationManagerBuilder, JwtProvider jwtProvider, JwtRepository jwtRepository) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtProvider = jwtProvider;
        this.jwtRepository = jwtRepository;
    }

    public Jwt login(LoginRequest request, String type) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getEmail() + CustomUserDetailsService.EMAIL_TYPE_SEPARATOR + type,
                request.getPassword()
        );

        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            Jwt jwt = jwtProvider.issueToken(authentication);
            return jwtRepository.save(jwt);
        } catch (BadCredentialsException e) {
            throw new LoginException(ErrorCode.LOGIN_FAIL);
        }
    }

    public Jwt reissueJwt(JwtReissueRequest request) {
        jwtProvider.validateToken(request.getRefreshToken(), TokenType.JWT_REFRESH_TOKEN);

        Authentication authentication = jwtProvider.getAuthentication(request.getAccessToken(), TokenType.JWT_ACCESS_TOKEN);
        Jwt oldJwt = jwtRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new JwtNotFoundException(ErrorCode.UNREGISTERED_JWT));

        if (!oldJwt.getRefreshToken().equals(request.getRefreshToken())) {
            throw new CustomJwtException(ErrorCode.INVALID_JWT);
        }

        oldJwt.update(jwtProvider.issueToken(authentication));
        return oldJwt;
    }
}
