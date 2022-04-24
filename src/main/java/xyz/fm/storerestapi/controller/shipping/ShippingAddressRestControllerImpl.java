package xyz.fm.storerestapi.controller.shipping;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.fm.storerestapi.dto.shipping.ShippingAddressInfo;
import xyz.fm.storerestapi.dto.shipping.ShippingAddressModifyRequest;
import xyz.fm.storerestapi.dto.shipping.ShippingAddressRegisterRequest;
import xyz.fm.storerestapi.entity.shipping.ShippingAddress;
import xyz.fm.storerestapi.entity.user.consumer.Consumer;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.UnauthorizedException;
import xyz.fm.storerestapi.jwt.JwtTokenUtil;
import xyz.fm.storerestapi.service.shipping.ShippingAddressService;
import xyz.fm.storerestapi.service.user.consumer.ConsumerService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("api/shipping/address")
public class ShippingAddressRestControllerImpl implements ShippingAddressRestController {

    private final ShippingAddressService shippingAddressService;
    private final ConsumerService consumerService;
    private final JwtTokenUtil jwtTokenUtil;

    public ShippingAddressRestControllerImpl(ShippingAddressService shippingAddressService, ConsumerService consumerService, JwtTokenUtil jwtTokenUtil) {
        this.shippingAddressService = shippingAddressService;
        this.consumerService = consumerService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    @PostMapping
    public ResponseEntity<ShippingAddressInfo> register(ShippingAddressRegisterRequest request, HttpServletRequest httpRequest) {
        String token = Optional.ofNullable(httpRequest.getHeader(JwtTokenUtil.JWT_KEY))
                .orElseThrow(() -> new UnauthorizedException(Error.UNAUTHORIZED, ErrorDetail.UNAUTHORIZED));

        Consumer consumer = consumerService.getByEmail(jwtTokenUtil.getEmailFromToken(token));
        ShippingAddress shippingAddress = shippingAddressService.register(consumer, request);
        return new ResponseEntity<>(
                new ShippingAddressInfo(shippingAddress),
                HttpStatus.CREATED
        );
    }

    @Override
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modify(
            @PathVariable("id") Long shippingAddressId,
            @Valid @RequestBody ShippingAddressModifyRequest request,
            HttpServletRequest httpRequest) {
        String token = Optional.ofNullable(httpRequest.getHeader(JwtTokenUtil.JWT_KEY))
                .orElseThrow(() -> new UnauthorizedException(Error.UNAUTHORIZED, ErrorDetail.UNAUTHORIZED));

        String consumerEmail = jwtTokenUtil.getEmailFromToken(token);

        request.setShippingAddressId(shippingAddressId);
        shippingAddressService.modify(consumerEmail, request);
    }

    @Override
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long shippingAddressId, HttpServletRequest httpRequest) {
        String token = Optional.ofNullable(httpRequest.getHeader(JwtTokenUtil.JWT_KEY))
                .orElseThrow(() -> new UnauthorizedException(Error.UNAUTHORIZED, ErrorDetail.UNAUTHORIZED));

        String consumerEmail = jwtTokenUtil.getEmailFromToken(token);
        shippingAddressService.delete(consumerEmail, shippingAddressId);
    }

    @Override
    @PatchMapping("default/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void designateDefault(@PathVariable("id") Long shippingAddressId, HttpServletRequest httpRequest) {
        String token = Optional.ofNullable(httpRequest.getHeader(JwtTokenUtil.JWT_KEY))
                .orElseThrow(() -> new UnauthorizedException(Error.UNAUTHORIZED, ErrorDetail.UNAUTHORIZED));

        Consumer consumer = consumerService.getByEmail(jwtTokenUtil.getEmailFromToken(token));
        shippingAddressService.designateDefaultShippingAddress(consumer, shippingAddressId);
    }
}
