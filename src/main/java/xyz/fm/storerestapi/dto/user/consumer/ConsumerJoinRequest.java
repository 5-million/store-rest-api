package xyz.fm.storerestapi.dto.user.consumer;

import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.Password;
import xyz.fm.storerestapi.entity.user.Phone;
import xyz.fm.storerestapi.entity.user.consumer.AdReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.value.invalid.password.PwdNotEqualToConfirmPwdException;

import javax.validation.Valid;

public class ConsumerJoinRequest {

    @Valid private Email email;
    private String name;
    private Password password;
    private Password confirmPassword;
    @Valid private Phone phone;
    private AdReceive adReceive;

    public ConsumerJoinRequest() {/* empty */}

    public ConsumerJoinRequest(
            Email email,
            String name,
            Password password,
            Password confirmPassword,
            Phone phone,
            AdReceive adReceive
    ) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.phone = phone;
        this.adReceive = adReceive;
    }

    public Consumer toEntity() {
        if (!password.toString().equals(confirmPassword.toString()))
            throw new PwdNotEqualToConfirmPwdException(ErrorCode.PWD_NOT_EQUAL_TO_CONFIRM_PWD);

        return new Consumer.Builder(
                email,
                name,
                phone,
                password
        )
                .adReceive(adReceive)
                .build();
    }

    public Email getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Password getPassword() {
        return password;
    }

    public Password getConfirmPassword() {
        return confirmPassword;
    }

    public Phone getPhone() {
        return phone;
    }

    public AdReceive getAdReceive() {
        return adReceive;
    }
}
