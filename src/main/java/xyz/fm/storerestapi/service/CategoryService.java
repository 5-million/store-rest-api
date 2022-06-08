package xyz.fm.storerestapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fm.storerestapi.entity.Category;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.entity.duplicate.DuplicateCategoryException;
import xyz.fm.storerestapi.exception.entity.notfound.CategoryNotFoundException;
import xyz.fm.storerestapi.repository.CategoryRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Category register(Long parentId, String name) {
        Category parent = null;

        if (parentId != null)
            parent = getById(parentId);

        if (existsCategoryByParentAndName(parent, name))
            throw new DuplicateCategoryException(ErrorCode.DUPLICATE_CATEGORY);

        Category newCategory = Category.builder()
                .parent(parent)
                .name(name)
                .build();

        return categoryRepository.save(newCategory);
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public boolean existsCategoryByParentAndName(Category parent, String name) {
        return categoryRepository.existsByParentAndName(parent, name);
    }
}
