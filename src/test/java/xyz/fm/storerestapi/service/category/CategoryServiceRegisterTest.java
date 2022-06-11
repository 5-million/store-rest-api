package xyz.fm.storerestapi.service.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.fm.storerestapi.entity.Category;
import xyz.fm.storerestapi.error.ErrorCode;
import xyz.fm.storerestapi.exception.CustomException;
import xyz.fm.storerestapi.exception.entity.duplicate.DuplicateCategoryException;
import xyz.fm.storerestapi.exception.entity.notfound.CategoryNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CategoryServiceRegisterTest extends CategoryServiceTest {

    private Category parent;
    private String name;

    @BeforeEach
    void beforeEach() {
        parent = Category.builder()
                .name("parent")
                .id(1L)
                .build();

        name = "newCategory";
    }

    @Test
    void register_willThrow_DuplicateEx() throws Exception {
        //given
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(parent));
        given(categoryRepository.existsByParentAndName(any(Category.class), anyString())).willReturn(true);

        //when
        DuplicateCategoryException exception =
                assertThrows(DuplicateCategoryException.class);

        //then
        assertErrorCode(exception, ErrorCode.DUPLICATE_CATEGORY);
    }

    @Test
    void register_willThrow_NotFoundEx() throws Exception {
        //given
        given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class);

        //then
        assertErrorCode(exception, ErrorCode.CATEGORY_NOT_FOUND);
    }

    @Test
    void register_success() throws Exception {
        //given
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(parent));

        //when
        categoryService.register(parent.getId(), name);

        //then
        verify(categoryRepository, times(1)).existsByParentAndName(any(Category.class), anyString());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    private <T extends Throwable> T assertThrows(Class<T> expectedType) {
        return Assertions.assertThrows(expectedType, () -> categoryService.register(parent.getId(), name));
    }

    private void assertErrorCode(CustomException exception, ErrorCode errorCode) {
        assertThat(exception.getErrorCode()).isEqualTo(errorCode);
    }
}
