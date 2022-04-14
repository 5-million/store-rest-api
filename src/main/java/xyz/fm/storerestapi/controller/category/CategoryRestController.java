package xyz.fm.storerestapi.controller.category;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fm.storerestapi.dto.category.CategoryInfoWithParent;
import xyz.fm.storerestapi.dto.category.CategoryRegisterRequest;
import xyz.fm.storerestapi.service.category.CategoryService;

@RestController
@RequestMapping("api/category")
public class CategoryRestController {

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryInfoWithParent> register(@RequestBody CategoryRegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CategoryInfoWithParent(categoryService.register(request.getParentId(), request.getCategoryName()))
        );
    }
}
