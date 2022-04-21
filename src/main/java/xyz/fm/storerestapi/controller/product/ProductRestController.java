package xyz.fm.storerestapi.controller.product;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.fm.storerestapi.dto.product.*;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.UnauthorizedException;
import xyz.fm.storerestapi.jwt.JwtTokenUtil;
import xyz.fm.storerestapi.repository.product.ProductApiRepository;
import xyz.fm.storerestapi.service.product.ProductService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("api/product")
public class ProductRestController {

    private final ProductService productService;
    private final ProductApiRepository productApiRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public ProductRestController(ProductService productService, ProductApiRepository productApiRepository, JwtTokenUtil jwtTokenUtil) {
        this.productService = productService;
        this.productApiRepository = productApiRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping
    public ResponseEntity<ProductRegisterResponse> registerProduct(
            @RequestBody ProductRegisterRequest request,
            HttpServletRequest httpRequest) {
        request.getItemRegisterRequest().getVendorItemRegisterRequest().setManagerEmail(getManagerEmailFromHeader(httpRequest));
        return new ResponseEntity<>(
                new ProductRegisterResponse(productService.registerProduct(request)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("item")
    public ResponseEntity<ProductRegisterResponse> registerItem(
            @RequestBody ItemRegisterRequest request,
            HttpServletRequest httpRequest) {
        request.getVendorItemRegisterRequest().setManagerEmail(getManagerEmailFromHeader(httpRequest));
        return new ResponseEntity<>(
                new ProductRegisterResponse(productService.registerItem(request)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("item/vendorItem")
    public ResponseEntity<ProductRegisterResponse> registerVendorItem(
            @RequestBody VendorItemRegisterRequest request,
            HttpServletRequest httpRequest) {
        request.setManagerEmail(getManagerEmailFromHeader(httpRequest));
        return new ResponseEntity<>(
                new ProductRegisterResponse(productService.registerVendorItem(request)),
                HttpStatus.CREATED
        );
    }

    private String getManagerEmailFromHeader(HttpServletRequest httpRequest) {
        String token = Optional.ofNullable(httpRequest.getHeader(JwtTokenUtil.JWT_KEY))
                .orElseThrow(() -> new UnauthorizedException(Error.UNAUTHORIZED, ErrorDetail.UNAUTHORIZED));

        return jwtTokenUtil.getEmailFromToken(token);
    }

    @GetMapping
    private ResponseEntity<ProductList> getByCategory(@RequestParam("categoryId") Long categoryId) {
        return ResponseEntity.ok(
                new ProductList(productApiRepository.findByCategory(categoryId))
        );
    }

    @GetMapping("{id}")
    private ResponseEntity<ProductDetails> getDetails(
            @PathVariable("id") Long productId,
            @RequestParam("itemId") Long itemId,
            @RequestParam("vendorItemId") Long vendorItemId) {
        return ResponseEntity.ok(
                new ProductDetails(productService.getAllFetchedById(productId), itemId, vendorItemId)
        );
    }
}

