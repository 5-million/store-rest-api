package xyz.fm.storerestapi.controller.user.vendor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.dto.user.vendor.VendorManagerApproveRequest;
import xyz.fm.storerestapi.dto.user.vendor.VendorManagerApproveResult;
import xyz.fm.storerestapi.dto.user.vendor.VendorManagerApproveResult.ApproveFail;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.entity.user.vendor.VendorRole;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NoPermissionException;
import xyz.fm.storerestapi.error.exception.NotFoundException;
import xyz.fm.storerestapi.error.exception.UnauthorizedException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.fm.storerestapi.dto.user.vendor.VendorManagerApproveResult.ApproveSuccess;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.JWT_KEY;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.JWT_VALUE_PREFIX;

public class VendorRestControllerApproveTest extends VendorRestControllerTest {

    private final String approveUrl = BASE_URL + "/manager/approve";

    @Test
    @DisplayName("approve fail: token not included")
    public void approve_fail_tokenNotIncluded() throws Exception {
        //given
        VendorManagerApproveRequest request = new VendorManagerApproveRequest(new ArrayList<>());

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.patch(approveUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(Error.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.UNAUTHORIZED));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(UnauthorizedException.class);


        //then

    }

    @Test
    @DisplayName("approve fail: not found admin")
    public void approve_fail_notFoundAdmin() throws Exception {
        //given
        VendorManagerApproveRequest request = new VendorManagerApproveRequest(new ArrayList<>());
        VendorManager admin = new VendorManager.Builder(
                "admin@vendor.com",
                "admin",
                "password",
                "01012345678",
                null
        ).build();

        String token = jwtTokenUtil.generateToken(admin);

        given(vendorService.approve(anyString(), any(VendorManagerApproveRequest.class)))
                .willThrow(new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_USER));


        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.patch(approveUrl)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(VendorRestController.class))
                .andExpect(handler().methodName("approve"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(Error.NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_FOUND_USER));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NotFoundException.class);
    }

    @Test
    @DisplayName("approve fail: not admin")
    public void approve_fail_notAdmin() throws Exception {
        //given
        VendorManagerApproveRequest request = new VendorManagerApproveRequest(new ArrayList<>());
        VendorManager manager = new VendorManager.Builder(
                "manager@vendor.com",
                "manager",
                "password",
                "01012345678",
                null
        ).role(VendorRole.VENDOR_MANAGER).build();

        String token = jwtTokenUtil.generateToken(manager);

        given(vendorService.approve(anyString(), any(VendorManagerApproveRequest.class)))
                .willThrow(new NoPermissionException(Error.NO_PERMISSION, ErrorDetail.NOT_ADMIN));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.patch(approveUrl)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(VendorRestController.class))
                .andExpect(handler().methodName("approve"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value(Error.NO_PERMISSION.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_ADMIN));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NoPermissionException.class);

    }

    @Test
    @DisplayName("approve success")
    public void approve_success() throws Exception {
        //given
        List<String> targets = new ArrayList<>();
        targets.add("success@vendor.com");
        targets.add("fail@vendor.com");
        VendorManagerApproveRequest request = new VendorManagerApproveRequest(targets);

        VendorManager manager = new VendorManager.Builder(
                "manager@vendor.com",
                "manager",
                "password",
                "01012345678",
                null
        ).role(VendorRole.VENDOR_MANAGER).build();

        String token = jwtTokenUtil.generateToken(manager);

        List<ApproveSuccess> success = new ArrayList<>();
        success.add(new ApproveSuccess("success@vendor.com"));
        List<ApproveFail> fail = new ArrayList<>();
        fail.add(new ApproveFail("fail1@vendor.com", ApproveFail.ALREADY_APPROVED));
        fail.add(new ApproveFail("fail1@vendor.com", ApproveFail.NOT_SAME_VENDOR));

        VendorManagerApproveResult result = new VendorManagerApproveResult(success, fail);

        given(vendorService.approve(anyString(), any(VendorManagerApproveRequest.class))).willReturn(result);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.patch(approveUrl)
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());


        //then
        ra
                .andExpect(handler().handlerType(VendorRestController.class))
                .andExpect(handler().methodName("approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success.size").value(success.size()))
                .andExpect(jsonPath("$.success.list").isArray())
                .andExpect(jsonPath("$.success.list[0].target").value(success.get(0).getTarget()))
                .andExpect(jsonPath("$.fail.size").value(fail.size()))
                .andExpect(jsonPath("$.fail.list").isArray())
                .andExpect(jsonPath("$.fail.list[0].target").value(fail.get(0).getTarget()))
                .andExpect(jsonPath("$.fail.list[0].cause").value(fail.get(0).getCause()))
                .andExpect(jsonPath("$.fail.list[1].target").value(fail.get(1).getTarget()))
                .andExpect(jsonPath("$.fail.list[1].cause").value(fail.get(1).getCause()));
    }
}
