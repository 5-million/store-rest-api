package xyz.fm.storerestapi.controller.user.consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.dto.user.EmailCheckRequest;
import xyz.fm.storerestapi.dto.user.PhoneNumberCheckRequest;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ConsumerRestControllerCheckTest extends ConsumerRestControllerTest {

    private static final String CHECK_BASE_URL = BASE_URL + "/join/check";

    @Test
    @DisplayName("checkEmail: invalid format")
    public void checkEmail_invalidFormat() throws Exception {
        //given
        String url = CHECK_BASE_URL + "/email";
        EmailCheckRequest request = new EmailCheckRequest("aaa");

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.NOT_VALID.getCode()))
                .andExpect(jsonPath("$.message").value(Error.NOT_VALID.getMessage()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_EMAIL_FORMAT));
    }

    @Test
    @DisplayName("checkEmail: email is Null")
    public void checkEmail_emailIsNull() throws Exception {
        //given
        String url = CHECK_BASE_URL + "/email";
        EmailCheckRequest request = new EmailCheckRequest(null);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.NOT_VALID.getCode()))
                .andExpect(jsonPath("$.message").value(Error.NOT_VALID.getMessage()))
                .andExpect(jsonPath("$.detail").value("이메일은 " + ErrorDetail.NOT_BLANK));
    }

    @Test
    @DisplayName("checkEmail: email is Blank")
    public void checkEmail_emailIsBlank() throws Exception {
        //given
        String url = CHECK_BASE_URL + "/email";
        EmailCheckRequest request = new EmailCheckRequest("");

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.NOT_VALID.getCode()))
                .andExpect(jsonPath("$.message").value(Error.NOT_VALID.getMessage()))
                .andExpect(jsonPath("$.detail").value("이메일은 " + ErrorDetail.NOT_BLANK));
    }

    @Test
    @DisplayName("checkEmail success")
    public void checkEmail_success() throws Exception {
        //given
        String url = CHECK_BASE_URL + "/email";
        EmailCheckRequest request = new EmailCheckRequest("abc@test.com");

        given(consumerService.isExistEmail(anyString())).willReturn(true);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exist").value(true));
    }

    @Test
    @DisplayName("checkPhoneNumber: invalid format")
    public void checkPhoneNumber_invalidFormat() throws Exception {
        //given
        String url = CHECK_BASE_URL + "/phone";
        PhoneNumberCheckRequest request = new PhoneNumberCheckRequest("000");

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders
                        .post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.NOT_VALID.getCode()))
                .andExpect(jsonPath("$.message").value(Error.NOT_VALID.getMessage()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_PHONE_NUMBER_FORMAT));
    }

    @Test
    @DisplayName("checkPhoneNumber: phone number is Null")
    public void checkPhoneNumber_phoneNumberIsNull() throws Exception {
        //given
        String url = CHECK_BASE_URL + "/phone";
        PhoneNumberCheckRequest request = new PhoneNumberCheckRequest(null);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.NOT_VALID.getCode()))
                .andExpect(jsonPath("$.message").value(Error.NOT_VALID.getMessage()))
                .andExpect(jsonPath("$.detail").value("휴대폰 번호는 " + ErrorDetail.NOT_BLANK));
    }

    @Test
    @DisplayName("checkPhoneNumber: phone number is Blank")
    public void checkPhoneNumber_phoneNumberIsBlank() throws Exception {
        //given
        String url = CHECK_BASE_URL + "/phone";
        PhoneNumberCheckRequest request = new PhoneNumberCheckRequest("");

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.NOT_VALID.getCode()))
                .andExpect(jsonPath("$.message").value(Error.NOT_VALID.getMessage()))
                .andExpect(jsonPath("$.detail").value("휴대폰 번호는 " + ErrorDetail.NOT_BLANK));
    }

    @Test
    @DisplayName("checkPhoneNumber success")
    public void checkPhoneNumber_success() throws Exception {
        //given
        String url = CHECK_BASE_URL + "/phone";
        PhoneNumberCheckRequest request = new PhoneNumberCheckRequest("01012345678");

        given(consumerService.isExistPhoneNumber(anyString())).willReturn(true);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exist").value(true));
    }
}
