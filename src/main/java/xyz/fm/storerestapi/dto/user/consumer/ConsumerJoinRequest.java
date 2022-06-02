package xyz.fm.storerestapi.dto.user.consumer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.Password;
import xyz.fm.storerestapi.entity.user.Phone;
import xyz.fm.storerestapi.entity.user.consumer.AdReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.value.invalid.password.PwdNotEqualToConfirmPwdException;

import javax.validation.Valid;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerJoinRequest {

    @Valid private Email email;
    private String name;
    private Password password;
    private Password confirmPassword;
    @Valid private Phone phone;
    private AdReceive adReceive;

    public Consumer toEntity() {
        if (!password.toString().equals(confirmPassword.toString()))
            throw new PwdNotEqualToConfirmPwdException(ErrorCode.PWD_NOT_EQUAL_TO_CONFIRM_PWD);

        return Consumer.builder()
                .email(email)
                .name(name)
                .phone(phone)
                .password(password)
                .adReceive(adReceive)
                .build();
    }
}
