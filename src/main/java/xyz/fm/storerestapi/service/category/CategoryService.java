package xyz.fm.storerestapi.service.category;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fm.storerestapi.entity.category.Category;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.DuplicationException;
import xyz.fm.storerestapi.error.exception.EntityNotFoundException;
import xyz.fm.storerestapi.repository.category.CategoryRepository;

import javax.persistence.PersistenceException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public boolean isPresentCategoryName(String categoryName) {
        return categoryRepository.existsByCategoryName(categoryName);
    }

    protected void checkDuplication(String categoryName) {
        if (isPresentCategoryName(categoryName)) {
            throw new DuplicationException(Error.DUPLICATE, ErrorDetail.DUPLICATE_CATEGORY);
        }
    }

    @Transactional
    public Category register(Long parentId, String categoryName) {
        checkDuplication(categoryName);
        Category child = new Category.Builder(categoryName).build();

        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new EntityNotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_CATEGORY));
            parent.addChild(child);
        }

        try {
            return categoryRepository.save(child);
        } catch (PersistenceException e) {
            throw new DuplicationException(Error.DUPLICATE, ErrorDetail.DUPLICATE_CATEGORY);
        }
    }

    public List<Category> getAllSortByDepth() {
        return categoryRepository.findAllSortByDepth();
    }
}
