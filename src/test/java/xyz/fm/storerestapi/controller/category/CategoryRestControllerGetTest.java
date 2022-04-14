package xyz.fm.storerestapi.controller.category;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.dto.category.Categories;
import xyz.fm.storerestapi.entity.category.Category;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CategoryRestControllerGetTest extends CategoryRestControllerTest {

    private static final String url = BASE_URL;

    private List<Category> buildTestCategories() {
        // layer 0
        Category phone = new Category.Builder("phone").id(1L).build();

        // layer 1
        Category apple = new Category.Builder("apple").parent(phone).id(2L).build();
        Category samsung = new Category.Builder("samsung").parent(phone).id(3L).build();

        // layer 2
        Category iPhone13 = new Category.Builder("iPhone13").parent(apple).id(4L).build();
        Category iPhone13Pro = new Category.Builder("iPhone13Pro").parent(apple).id(5L).build();
        Category galaxy22 = new Category.Builder("galaxy22").parent(samsung).id(6L).build();
        Category zFlip = new Category.Builder("zFlip").parent(samsung).id(5L).build();

        List<Category> categories = new ArrayList<>();
        categories.add(phone);
        categories.add(apple);
        categories.add(samsung);
        categories.add(iPhone13);
        categories.add(iPhone13Pro);
        categories.add(galaxy22);
        categories.add(zFlip);

        return categories;
    }

    @Test
    @DisplayName("getCategories success")
    public void getCategories_success() throws Exception {
        //given
        List<Category> categories = buildTestCategories();
        given(categoryService.getAllSortByDepth()).willReturn(categories);

        //when
        ResultActions ra = mvc.perform(
                MockMvcRequestBuilders.get(url)
        ).andDo(print());

        //then
        ra
                .andExpect(handler().handlerType(CategoryRestController.class))
                .andExpect(handler().methodName("getCategories"))
                .andExpect(status().isOk());

        assertThat(ra.andReturn().getResponse().getContentAsString())
                .isEqualTo(om.writeValueAsString(new Categories(categories)));
    }
}
