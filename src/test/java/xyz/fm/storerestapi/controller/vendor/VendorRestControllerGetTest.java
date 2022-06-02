package xyz.fm.storerestapi.controller.vendor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.controller.TestJwtFactory;
import xyz.fm.storerestapi.dto.vendor.VendorManagerInfo;
import xyz.fm.storerestapi.entity.Address;
import xyz.fm.storerestapi.entity.Vendor;
import xyz.fm.storerestapi.entity.user.Email;
import xyz.fm.storerestapi.entity.user.Password;
import xyz.fm.storerestapi.entity.user.Phone;
import xyz.fm.storerestapi.entity.user.Role;
import xyz.fm.storerestapi.entity.user.vendor.VendorManager;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.jwt.JwtAuthenticationFilter;
import xyz.fm.storerestapi.jwt.JwtProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VendorRestControllerGetTest extends VendorRestControllerTest {

    private final String URL = "/vendor/manager";
    private final TestJwtFactory testJwtFactory;
    private Vendor vendor;
    VendorManager executive;
    private String accessToken;

    public VendorRestControllerGetTest(@Autowired JwtProvider jwtProvider) {
        this.testJwtFactory = new TestJwtFactory(jwtProvider);
    }

    @BeforeEach
    void beforeEach() {
        vendor = Vendor.builder()
                .name("vendor")
                .regNumber("1")
                .ceo("ceo")
                .location(new Address("zipcode", "base", "detail"))
                .id(1L)
                .build();

        executive = VendorManager.builder()
                .email(new Email("executive@vendor.com"))
                .name("executive")
                .phone(new Phone("01012341234"))
                .password(new Password("password"))
                .id(0L)
                .role(Role.ROLE_VENDOR_EXECUTIVE)
                .build();

        vendor.addManager(executive);
        for (int i = 0; i < 10; i++) {
            vendor.addManager(
                    VendorManager.builder()
                            .email(new Email("staff" + i + "@vendor.com"))
                            .name("staff" + i)
                            .phone(new Phone("0101234567" + i))
                            .password(new Password("password"))
                            .id((long) i + 1)
                            .role(Role.ROLE_VENDOR_STAFF)
                            .build()
            );
        }

        accessToken = testJwtFactory.buildAccessToken(executive);
    }

    @Test
    void getManagerList_401() throws Exception {
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(URL)
        ).andDo(print());

        assertErrorResponse(ra, ErrorCode.JWT_UNAUTHORIZED);
    }

    private void assertErrorResponse(ResultActions ra, ErrorCode errorCode) throws Exception {
        ra
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andExpect(jsonPath("$.error").value(errorCode.getCode()))
                .andExpect(jsonPath("$.message").value(errorCode.getMessage()));
    }

    @Test
    void getManagerList_403() throws Exception {
        String staffAccessToken = testJwtFactory.buildAccessToken(vendor.getVendorManagerList().get(1));

        ResultActions ra = performGetManagerList(staffAccessToken);

        assertErrorResponse(ra, ErrorCode.JWT_NO_PERMISSION);
    }

    private ResultActions performGetManagerList(String accessToken) throws Exception {
        return mvc.perform(
                MockMvcRequestBuilders.get(URL)
                        .header(JwtAuthenticationFilter.AUTHORIZATION_HEADER, JwtAuthenticationFilter.BEARER_PREFIX + accessToken)
        ).andDo(print());
    }

    @Test
    void getManagerList_200() throws Exception {
        List<VendorManagerInfo> vendorManagerInfoList = vendor.getVendorManagerList()
                .stream()
                .map(VendorManagerInfo::of)
                .collect(Collectors.toList());

        given(vendorService.getVendorManagerByEmail(any(Email.class))).willReturn(executive);
        given(vendorManagerQueryRepository.findAllByVendorId(any(Vendor.class)))
                .willReturn(vendorManagerInfoList);

        ResultActions ra = performGetManagerList(accessToken);

        ra
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendorManagerList").isArray())
                .andExpect(jsonPath("$.vendorManagerList").isNotEmpty());

        assertJsonPathVendorManagerInfo(ra, vendorManagerInfoList);
    }

    private void assertJsonPathVendorManagerInfo(ResultActions ra, List<VendorManagerInfo> infos) throws Exception {
        for (int i = 1; i < infos.size(); i++) {
            ra
                    .andExpect(jsonPath("$.vendorManagerList[" + i + "].vendorManagerId").value(infos.get(i).getVendorManagerId()))
                    .andExpect(jsonPath("$.vendorManagerList[" + i + "].vendorId").value(infos.get(i).getVendorId()))
                    .andExpect(jsonPath("$.vendorManagerList[" + i + "].email").value(infos.get(i).getEmail()))
                    .andExpect(jsonPath("$.vendorManagerList[" + i + "].name").value(infos.get(i).getName()))
                    .andExpect(jsonPath("$.vendorManagerList[" + i + "].phone").value(infos.get(i).getPhone()))
                    .andExpect(jsonPath("$.vendorManagerList[" + i + "].approved").value(infos.get(i).getApproved()))
                    .andExpect(jsonPath("$.vendorManagerList[" + i + "].approvalManagerId").doesNotExist())
                    .andExpect(jsonPath("$.vendorManagerList[" + i + "].role").value(infos.get(i).getRole().toString()));
        }
    }
}
