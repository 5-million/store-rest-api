package xyz.fm.storerestapi.service.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import xyz.fm.storerestapi.entity.category.Category;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.DuplicationException;
import xyz.fm.storerestapi.error.exception.EntityNotFoundException;

import javax.persistence.PersistenceException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

public class CategoryServiceRegisterTest extends CategoryServiceTest {

    @Test
    @DisplayName("checkDuplication success")
    public void checkDuplication_success() throws Exception {
        //given
        String categoryName = "category";
        given(categoryRepository.existsByCategoryName(anyString())).willReturn(true);

        //when
        DuplicationException exception =
                assertThrows(DuplicationException.class, () -> categoryService.checkDuplication(categoryName));

        //then
        assertThat(exception.getError()).isEqualTo(Error.DUPLICATE);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.DUPLICATE_CATEGORY);
    }

    @Test
    @DisplayName("register fail: duplicate name")
    public void register_fail_duplicateName() throws Exception {
        //given
        String categoryName = "category";
        given(categoryRepository.existsByCategoryName(anyString())).willReturn(true);

        //when
        DuplicationException exception =
                assertThrows(DuplicationException.class, () -> categoryService.register(null, categoryName));

        //then
        assertThat(exception.getError()).isEqualTo(Error.DUPLICATE);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.DUPLICATE_CATEGORY);
    }

    @Test
    @DisplayName("register fail: not found parent")
    public void register_fail_notFoundParent() throws Exception {
        //given
        Long parentId = 1L;
        String categoryName = "category";
        given(categoryRepository.existsByCategoryName(anyString())).willReturn(false);
        given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class, () -> categoryService.register(parentId, categoryName));

        //then
        assertThat(exception.getError()).isEqualTo(Error.NOT_FOUND);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.NOT_FOUND_CATEGORY);
    }

    @Test
    @DisplayName("register fail: conflict")
    public void register_fail_conflict() throws Exception {
        //given
        Category parent = new Category.Builder("parent").id(1L).build();
        String categoryName = "category";

        given(categoryRepository.existsByCategoryName(anyString())).willReturn(false);
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(parent));
        given(categoryRepository.save(any(Category.class))).willThrow(PersistenceException.class);

        //when
        DuplicationException exception =
                assertThrows(DuplicationException.class, () -> categoryService.register(parent.getId(), categoryName));

        //then
        assertThat(exception.getError()).isEqualTo(Error.DUPLICATE);
        assertThat(exception.getDetail()).isEqualTo(ErrorDetail.DUPLICATE_CATEGORY);

    }

    @Test
    @DisplayName("register success: no parent")
    public void register_success_noParent() throws Exception {
        //given
        Category child = new Category.Builder("child").build();
        given(categoryRepository.existsByCategoryName(anyString())).willReturn(false);
        given(categoryRepository.save(any(Category.class))).willReturn(child);

        //when
        Category newCategory = categoryService.register(null, child.getCategoryName());

        //then
        assertThat(newCategory.getDepth()).isEqualTo(0);
    }

    @Test
    @DisplayName("register success: exists parent")
    public void register_success_existsParent() throws Exception {
        //given
        Category parent = new Category.Builder("parent").id(1L).build();
        Category child = new Category.Builder("child").parent(parent).build();

        given(categoryRepository.existsByCategoryName(anyString())).willReturn(false);
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(parent));
        given(categoryRepository.save(any(Category.class))).willReturn(child);

        //when
        Category newCategory = categoryService.register(parent.getId(), child.getCategoryName());

        //then
        assertThat(newCategory.getParent()).isEqualTo(parent);
        assertThat(newCategory.getDepth()).isEqualTo(parent.getDepth() + 1);
    }
}
