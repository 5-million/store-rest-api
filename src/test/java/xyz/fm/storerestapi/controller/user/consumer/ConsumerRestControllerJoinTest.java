package xyz.fm.storerestapi.controller.user.consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.dto.user.consumer.ConsumerJoinRequest;
import xyz.fm.storerestapi.entity.user.consumer.AdsReceive;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.DuplicationException;
import xyz.fm.storerestapi.error.exception.InvalidPasswordException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ConsumerRestControllerJoinTest extends ConsumerRestControllerTest {

    private static final String URL = BASE_URL + "/join";

    @Test
    @DisplayName("join fail: invalid argument")
    public void join_fail_invalidArgument() throws Exception {
        //given
        ConsumerJoinRequest request = new ConsumerJoinRequest(
                "abc",
                "name",
                "pwd",
                "pwd",
                "phone",
                new ConsumerJoinRequest.AdsReceive(true, true, true)
        );

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ConsumerRestController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.NOT_VALID.getCode()));
    }


    @Test
    @DisplayName("join fail: duplicate consumer")
    public void join_fail_duplicateConsumer() throws Exception {
        //given
        ConsumerJoinRequest request = new ConsumerJoinRequest(
                "abc@test.com",
                "name",
                "pwd",
                "pwd",
                "01012345678",
                new ConsumerJoinRequest.AdsReceive(true, true, true)
        );

        given(consumerService.join(any(ConsumerJoinRequest.class)))
                .willThrow(new DuplicationException(Error.DUPLICATE, ErrorDetail.DUPLICATE_USER));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ConsumerRestController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value(Error.DUPLICATE.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.DUPLICATE_USER));
    }

    @Test
    @DisplayName("join fail: pwd != confirmPwd")
    public void join_fail_pwdNotEqualToConfirmPwd() throws Exception {
        //given
        ConsumerJoinRequest request = new ConsumerJoinRequest(
                "abc@test.com",
                "name",
                "pwd",
                "pwd",
                "01012345678",
                new ConsumerJoinRequest.AdsReceive(true, true, true)
        );

        given(consumerService.join(any(ConsumerJoinRequest.class)))
                .willThrow(new InvalidPasswordException(Error.INVALID_PASSWORD, ErrorDetail.PWD_NOT_EQUAL_TO_CONFIRM_PWD));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ConsumerRestController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.INVALID_PASSWORD.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.PWD_NOT_EQUAL_TO_CONFIRM_PWD));
    }

    @Test
    @DisplayName("join success")
    public void join_success() throws Exception {
        //given
        ConsumerJoinRequest request = new ConsumerJoinRequest(
                "abc@test.com",
                "name",
                "pwd",
                "pwd",
                "01012345678",
                new ConsumerJoinRequest.AdsReceive(true, true, true)
        );

        given(consumerService.join(any(ConsumerJoinRequest.class))).willReturn(
                new Consumer.Builder(
                        request.getEmail(),
                        request.getName(),
                        request.getPassword(),
                        request.getPhoneNumber(),
                        new AdsReceive(
                                request.getAdsReceive().getToEmail(),
                                request.getAdsReceive().getToSMSAndMMS(),
                                request.getAdsReceive().getToAppPush()
                        )
                ).build()
        );

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(ConsumerRestController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(status().isCreated());
    }
}
