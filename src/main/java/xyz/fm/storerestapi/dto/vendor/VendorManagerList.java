package xyz.fm.storerestapi.dto.vendor;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class VendorManagerList {

    private List<VendorManagerInfo> vendorManagerList;
}
