package xyz.fm.storerestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import xyz.fm.storerestapi.entity.user.User;
import xyz.fm.storerestapi.jwt.JwtProvider;

import java.util.Collections;

public class TestJwtFactory extends ControllerTestConfig {

    private final JwtProvider jwtProvider;

    public TestJwtFactory(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public String buildAccessToken(User user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole().name());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getEmail().toString(),
                null,
                Collections.singleton(grantedAuthority)
        );
        return jwtProvider.issueToken(authenticationToken).getAccessToken();
    }
}
