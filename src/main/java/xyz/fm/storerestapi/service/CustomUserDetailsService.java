package xyz.fm.storerestapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.repository.ConsumerRepository;
import xyz.fm.storerestapi.repository.VendorManagerRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    public static final String EMAIL_TYPE_SEPARATOR = ":";

    private final ConsumerRepository consumerRepository;
    private final VendorManagerRepository vendorManagerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] split = username.split(EMAIL_TYPE_SEPARATOR);
        Email email = new Email(split[0]);
        String type = split[1];

        xyz.fm.storerestapi.entity.user.User user;
        if (type.equals("csm"))
            user = consumerRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));
        else
            user = vendorManagerRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage()));

        return createUserDetails(user);
    }

    private UserDetails createUserDetails(xyz.fm.storerestapi.entity.user.User user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole().name());
        return new User(
                user.getEmail().toString(),
                user.getPassword().toString(),
                Collections.singleton(grantedAuthority)
        );
    }
}
