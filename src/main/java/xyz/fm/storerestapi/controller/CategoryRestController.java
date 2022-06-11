package xyz.fm.storerestapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.fm.storerestapi.dto.category.Categories;
import xyz.fm.storerestapi.dto.category.CategoryBriefInfo;
import xyz.fm.storerestapi.dto.category.CategoryRegisterRequest;
import xyz.fm.storerestapi.entity.Category;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.entity.notfound.CategoryNotFoundException;
import xyz.fm.storerestapi.repository.query.CategoryQueryRepository;
import xyz.fm.storerestapi.service.CategoryService;

import java.net.URI;

@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;
    private final CategoryQueryRepository categoryQueryRepository;

    @PostMapping
    public ResponseEntity<CategoryBriefInfo> register(@RequestBody CategoryRegisterRequest request) {
        Category newCategory = categoryService.register(request.getParentId(), request.getCategoryName());

        return ResponseEntity.created(URI.create("/categories/" + newCategory.getId()))
                .body(CategoryBriefInfo.of(newCategory));
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryBriefInfo> getById(@PathVariable("id") Long id) {
        CategoryBriefInfo info = categoryQueryRepository.findBriefInfoById(id)
                .orElseThrow(() -> new CategoryNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));

        return ResponseEntity.ok(info);
    }

    @GetMapping
    public ResponseEntity<Categories> getAll() {
        return ResponseEntity.ok(
                new Categories(categoryQueryRepository.findAllByDepthIs(0))
        );
    }
}
