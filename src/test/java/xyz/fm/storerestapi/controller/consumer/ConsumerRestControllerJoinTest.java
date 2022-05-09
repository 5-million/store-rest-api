package xyz.fm.storerestapi.controller.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.controller.ConsumerRestController;
import xyz.fm.storerestapi.dto.user.consumer.ConsumerJoinRequest;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.Password;
import xyz.fm.storerestapi.entity.user.Phone;
import xyz.fm.storerestapi.entity.user.consumer.AdReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.value.duplicate.DuplicateEmailException;
import xyz.fm.storerestapi.exception.value.duplicate.DuplicatePhoneException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ConsumerRestControllerJoinTest extends ConsumerRestControllerTest {

    private final String URL = "/api/consumer/join";

    @Test
    void join_fail_invalidArgument() throws Exception {
        //given
        ConsumerJoinRequest request = buildRequest("abc", "password", "password", "0101234567");

        //when
        ResultActions result = performJoin(request);

        //then
        assertStatus(result, status().isBadRequest());
        assertErrorCode(result, ErrorCode.INVALID_ARGUMENT);
        result.andExpect(jsonPath("$.errors").isArray());
    }

    private void assertErrorCode(ResultActions result, ErrorCode errorCode) throws Exception {
        result
                .andExpect(jsonPath("$.error").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").value(errorCode.getMessage()));
    }

    private void assertStatus(ResultActions result, ResultMatcher status) throws Exception {
        result
                .andExpect(handler().handlerType(ConsumerRestController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status);
    }

    private ResultActions performJoin(ConsumerJoinRequest request) throws Exception {
        return mvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());
    }

    private ConsumerJoinRequest buildRequest(String email, String password, String confirmPassword, String phone) {
        return new ConsumerJoinRequest(
                new Email(email),
                "name",
                new Password(password),
                new Password(confirmPassword),
                new Phone(phone),
                new AdReceive(true, false, false)
        );
    }

    @Test
    void join_fail_pwdNotEqualToConfirmPwd() throws Exception {
        //given
        ConsumerJoinRequest request = buildRequest("abc@test.com", "password1", "password2", "01012345678");

        //when
        ResultActions result = performJoin(request);

        //then
        assertStatus(result, status().isBadRequest());
        assertErrorCode(result, ErrorCode.PWD_NOT_EQUAL_TO_CONFIRM_PWD);
    }

    @Test
    void join_fail_duplicateEmail() throws Exception {
        //given
        ConsumerJoinRequest request = buildRequest("abc@test.com", "password", "password", "01012345678");
        given(consumerService.join(any(Consumer.class)))
                .willThrow(new DuplicateEmailException(ErrorCode.DUPLICATE_EMAIL));

        //when
        ResultActions result = performJoin(request);

        //then
        assertStatus(result, status().isConflict());
        assertErrorCode(result, ErrorCode.DUPLICATE_EMAIL);
    }

    @Test
    void join_fail_duplicatePhone() throws Exception {
        //given
        ConsumerJoinRequest request = buildRequest("abc@test.com", "password", "password", "01012345678");
        given(consumerService.join(any(Consumer.class)))
                .willThrow(new DuplicatePhoneException(ErrorCode.DUPLICATE_PHONE, request.getEmail()));

        //when
        ResultActions result = performJoin(request);

        //then
        assertStatus(result, status().isConflict());
        assertErrorCode(result, ErrorCode.DUPLICATE_PHONE);
        result.andExpect(jsonPath("$.registeredEmail").value(request.getEmail().encrypt()));
    }

    @Test
    void join_success() throws Exception {
        //given
        ConsumerJoinRequest request = buildRequest("abc@test.com", "password", "password", "01012345678");
        given(consumerService.join(any(Consumer.class))).willReturn(request.toEntity());

        //when
        ResultActions result = performJoin(request);

        //then
        assertStatus(result, status().isCreated());
        result
                .andExpect(jsonPath("$.email").value(request.getEmail().toString()))
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.phone").value(request.getPhone().toString()));
//                .andExpect(jsonPath("$.adReceive.toEmail").value(request.getAdReceive().getToEmail()))
//                .andExpect(jsonPath("$.adReceive.toMessage").value(request.getAdReceive().getToMessage()))
//                .andExpect(jsonPath("$.adReceive.toAppPush").value(request.getAdReceive().getToAppPush()));
    }
}
