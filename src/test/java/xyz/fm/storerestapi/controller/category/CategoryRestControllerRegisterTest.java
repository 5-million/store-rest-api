package xyz.fm.storerestapi.controller.category;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.dto.category.CategoryRegisterRequest;
import xyz.fm.storerestapi.entity.category.Category;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.DuplicationException;
import xyz.fm.storerestapi.error.exception.EntityNotFoundException;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CategoryRestControllerRegisterTest extends CategoryRestControllerTest {

    private static final String url = BASE_URL;

    @Test
    @DisplayName("register fail: duplicate")
    public void register_fail_duplicate() throws Exception {
        //given
        CategoryRegisterRequest request = new CategoryRegisterRequest(1L, "category");
        given(categoryService.register(anyLong(), anyString()))
                .willThrow(new DuplicationException(Error.DUPLICATE, ErrorDetail.DUPLICATE_CATEGORY));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(CategoryRestController.class))
                .andExpect(handler().methodName("register"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value(Error.DUPLICATE.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.DUPLICATE_CATEGORY));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(DuplicationException.class);
    }

    @Test
    @DisplayName("register fail: not found parent")
    public void register_fail_notFoundParent() throws Exception {
        //given
        CategoryRegisterRequest request = new CategoryRegisterRequest(1L, "category");
        given(categoryService.register(anyLong(), anyString()))
                .willThrow(new EntityNotFoundException(Error.NOT_FOUND, ErrorDetail.NOT_FOUND_CATEGORY));

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(CategoryRestController.class))
                .andExpect(handler().methodName("register"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(Error.NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.detail").value(ErrorDetail.NOT_FOUND_CATEGORY));

        assertThat(ra.andReturn().getResolvedException().getClass()).isEqualTo(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("register success")
    public void register_success() throws Exception {
        //given
        CategoryRegisterRequest request = new CategoryRegisterRequest(1L, "childCategory");
        Category parent = new Category.Builder("parentCategory").id(request.getParentId()).build();
        Category child = new Category.Builder(request.getCategoryName()).id(2L).parent(parent).build();

        given(categoryService.register(anyLong(), anyString())).willReturn(child);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(request))
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(CategoryRestController.class))
                .andExpect(handler().methodName("register"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryId").value(child.getId()))
                .andExpect(jsonPath("$.categoryName").value(child.getCategoryName()))
                .andExpect(jsonPath("$.depth").value(child.getDepth()))
                .andExpect(jsonPath("$.parent.categoryId").value(child.getParent().getId()))
                .andExpect(jsonPath("$.parent.categoryName").value(child.getParent().getCategoryName()))
                .andExpect(jsonPath("$.parent.depth").value(child.getParent().getDepth()));
    }
}
