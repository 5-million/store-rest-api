package xyz.fm.storerestapi.controller.user.vendor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.dto.user.vendor.VendorInfo;
import xyz.fm.storerestapi.dto.user.vendor.VendorManagerInfo;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.entity.user.vendor.VendorRole;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NoPermissionException;
import xyz.fm.storerestapi.error.exception.NotFoundException;
import xyz.fm.storerestapi.error.exception.UnauthorizedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.JWT_KEY;
import static xyz.fm.storerestapi.jwt.JwtTokenUtil.JWT_VALUE_PREFIX;

public class VendorRestControllerGetTest extends VendorRestControllerTest {

    @Test
    @DisplayName("getAll success: data exists")
    public void getAll_success_dataExists() throws Exception {
        //given
        Vendor apple = new Vendor.Builder("apple", "cook", "010", "usa").build();
        Vendor samsung = new Vendor.Builder("samsung", "jaemyung", "011", "seoul").build();

        List<VendorInfo> vendorInfoList = new ArrayList<>();
        vendorInfoList.add(VendorInfo.of(apple));
        vendorInfoList.add(VendorInfo.of(samsung));

        given(vendorApiRepository.findAllVendorInfoBy()).willReturn(vendorInfoList);

        //when
        ResultActions ra = mvc.perform(MockMvcRequestBuilders.get(BASE_URL)).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(VendorRestController.class))
                .andExpect(handler().methodName("getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendorList").isArray())
                .andExpect(jsonPath("$.vendorList[0].vendorName").value(apple.getVendorName()))
                .andExpect(jsonPath("$.vendorList[1].vendorName").value(samsung.getVendorName()));
    }

    @Test
    @DisplayName("getAll success: empty")
    public void getAll_success_dataEmpty() throws Exception {
        //given
        given(vendorApiRepository.findAllVendorInfoBy()).willReturn(Collections.emptyList());

        //when
        ResultActions ra = mvc.perform(MockMvcRequestBuilders.get(BASE_URL)).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(VendorRestController.class))
                .andExpect(handler().methodName("getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendorList").isArray())
                .andExpect(jsonPath("$.vendorList").isEmpty());
    }

    @Test
    @DisplayName("getManagerListByVendor fail: token not included")
    public void getManagerListByVendor_fail_tokenNotIncluded() throws Exception {
        //given


        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/manager")
        ).andDo(print());

        //then
        ra
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value(Error.UNAUTHORIZED.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.UNAUTHORIZED));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(UnauthorizedException.class);
    }

    @Test
    @DisplayName("getManagerListByVendor fail: not found admin")
    public void getManagerListByVendor_fail_notFoundAdmin() throws Exception {
        //given
        VendorManager manager = new VendorManager.Builder(
                "manager@vendor.com",
                "admin",
                "password",
                "01012345678", null
        ).build();

        String token = jwtTokenUtil.generateToken(manager);

        given(vendorService.getManagerByEmail(anyString()))
                .willThrow(new NotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_USER));
        
        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/manager")
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
        ).andDo(print());
        
        //then
        ra
                .andExpect(handler().handlerType(VendorRestController.class))
                .andExpect(handler().methodName("getManagerList"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(Error.NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_FOUND_USER));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NotFoundException.class);
    }

    @Test
    @DisplayName("getManagerListByVendor fail: not admin(no permission)")
    public void getManagerListByVendor_fail_notAdmin() throws Exception {
        //given
        VendorManager manager = new VendorManager.Builder(
                "manager@vendor.com",
                "admin",
                "password",
                "01012345678", null
        ).role(VendorRole.VENDOR_MANAGER).build();

        String token = jwtTokenUtil.generateToken(manager);

        given(vendorService.getManagerByEmail(anyString())).willReturn(manager);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/manager")
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(VendorRestController.class))
                .andExpect(handler().methodName("getManagerList"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value(Error.NO_PERMISSION.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_ADMIN));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(NoPermissionException.class);
    }

    @Test
    @DisplayName("getManagerListByVendor success")
    public void getManagerListByVendor_success() throws Exception {
        //given
        Vendor vendor = new Vendor.Builder("apple", "cook", "011", "usa").build();
        VendorManager manager = new VendorManager.Builder(
                "manager@vendor.com",
                "admin",
                "password",
                "01012345678", vendor
        ).role(VendorRole.VENDOR_ROOT).build();

        String token = jwtTokenUtil.generateToken(manager);

        given(vendorService.getManagerByEmail(anyString())).willReturn(manager);

        VendorManagerInfo managerInfo = new VendorManagerInfo(
                1L,
                "manager@vendor.com",
                "manager",
                "01012345678",
                VendorRole.VENDOR_MANAGER,
                false
        );

        List<VendorManagerInfo> infos = new ArrayList<>();
        infos.add(managerInfo);

        for (VendorManagerInfo info : infos) {
            System.out.println(info);
        }

        given(vendorManagerApiRepository.findByVendor(any(Vendor.class))).willReturn(infos);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(BASE_URL + "/manager")
                        .header(JWT_KEY, JWT_VALUE_PREFIX + token)
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(VendorRestController.class))
                .andExpect(handler().methodName("getManagerList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendorManagerList").isArray())
                .andExpect(jsonPath("$.vendorManagerList[0].managerId").value(managerInfo.getManagerId()))
                .andExpect(jsonPath("$.vendorManagerList[0].email").value(managerInfo.getEmail()))
                .andExpect(jsonPath("$.vendorManagerList[0].name").value(managerInfo.getName()))
                .andExpect(jsonPath("$.vendorManagerList[0].phoneNumber").value(managerInfo.getPhoneNumber()))
                .andExpect(jsonPath("$.vendorManagerList[0].role").value(managerInfo.getRole()))
                .andExpect(jsonPath("$.vendorManagerList[0].permission").value(managerInfo.getPermission()));
    }
}
