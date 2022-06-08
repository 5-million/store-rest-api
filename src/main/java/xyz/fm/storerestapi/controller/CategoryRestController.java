package xyz.fm.storerestapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.fm.storerestapi.dto.category.CategoryBriefInfo;
import xyz.fm.storerestapi.dto.category.CategoryRegisterRequest;
import xyz.fm.storerestapi.entity.Category;
import xyz.fm.storerestapi.service.CategoryService;

import java.net.URI;

@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryBriefInfo> register(@RequestBody CategoryRegisterRequest request) {
        Category newCategory = categoryService.register(request.getParentId(), request.getCategoryName());

        return ResponseEntity.created(URI.create("/categories/" + newCategory.getId()))
                .body(CategoryBriefInfo.of(newCategory));
    }
}
