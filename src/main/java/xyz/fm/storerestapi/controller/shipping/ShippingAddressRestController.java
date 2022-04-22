package xyz.fm.storerestapi.controller.shipping;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import xyz.fm.storerestapi.dto.shipping.ShippingAddressInfo;
import xyz.fm.storerestapi.dto.shipping.ShippingAddressModifyRequest;
import xyz.fm.storerestapi.dto.shipping.ShippingAddressRegisterRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

public interface ShippingAddressRestController {

    ResponseEntity<ShippingAddressInfo> register(@Valid @RequestBody ShippingAddressRegisterRequest request, HttpServletRequest httpRequest);
    void modify(@PathVariable("id") Long shippingAddressId, @Valid @RequestBody ShippingAddressModifyRequest request, HttpServletRequest httpRequest);
}
