package xyz.fm.storerestapi.controller.user.vendor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.dto.user.vendor.VendorInfo;
import xyz.fm.storerestapi.entity.user.vendor.Vendor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
}
