package xyz.fm.storerestapi.controller.user.consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.dto.user.MyInfoRequest;
import xyz.fm.storerestapi.entity.user.consumer.AdsReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.UnauthorizedException;
import xyz.fm.storerestapi.util.EncryptUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.JWT_KEY;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.JWT_VALUE_PREFIX;

public class ConsumerRestControllerMeTest extends ConsumerRestControllerTest {

    private static final String URL = BASE_URL + "/me";

    @Test
    @DisplayName("me fail: 토큰이 포함되지 않음")
    public void me_fail_tokenNotIncluded() throws Exception {
        //given
        MyInfoRequest request = new MyInfoRequest("password");

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ConsumerRestController.class))
                .andExpect(handler().methodName("me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(Error.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.UNAUTHORIZED));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(UnauthorizedException.class);
    }

    @Test
    @DisplayName("me fail: incorrect password")
    public void me_fail_incorrectPassword() throws Exception {
        //given
        String email = "abc@test.com";
        MyInfoRequest request = new MyInfoRequest("pessword");
        Consumer consumer = new Consumer.Builder(
                email,
                "name",
                EncryptUtil.encode("password"),
                "01012341234",
                new AdsReceive(true, true, true)
        )
                .id(1L)
                .build();

        given(consumerService.getByEmail(anyString())).willReturn(consumer);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ConsumerRestController.class))
                .andExpect(handler().methodName("me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(Error.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.INCORRECT_PWD));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(UnauthorizedException.class);
    }

    @Test
    @DisplayName("me success")
    public void me_success() throws Exception {
        //given
        String email = "abc@test.com";
        MyInfoRequest request = new MyInfoRequest("password");
        Consumer consumer = new Consumer.Builder(
                email,
                "name",
                EncryptUtil.encode(request.getPassword()),
                "01012341234",
                new AdsReceive(true, true, true)
        )
                .id(1L)
                .build();

        given(consumerService.getByEmail(anyString())).willReturn(consumer);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ConsumerRestController.class))
                .andExpect(handler().methodName("me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.consumerId").value(consumer.getId()))
                .andExpect(jsonPath("$.email").value(consumer.getEmail()))
                .andExpect(jsonPath("$.name").value(consumer.getName()))
                .andExpect(jsonPath("$.phoneNumber").value(consumer.getPhoneNumber()))
                .andExpect(jsonPath("$.adsReceive.toEmail").value(consumer.getAdsReceive().getToEmail()))
                .andExpect(jsonPath("$.adsReceive.toSMSAndMMS").value(consumer.getAdsReceive().getToSMSAndMMS()))
                .andExpect(jsonPath("$.adsReceive.toAppPush").value(consumer.getAdsReceive().getToAppPush()));
    }
}
