package xyz.fm.storerestapi.controller.category;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.fm.storerestapi.dto.category.CategoryBriefInfo;
import xyz.fm.storerestapi.error.ErrorCode;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryRestControllerGetTest extends CategoryRestControllerTest {

    private final Long categoryId = 1L;
    private final String URL = BASE_URL + "/" + categoryId;

    @Test
    void getById_404() throws Exception {
        givenFindBriefInfoById().willReturn(Optional.empty());

        ResultActions ra = performGetById();

        ra
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.error").value(ErrorCode.CATEGORY_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.CATEGORY_NOT_FOUND.getMessage()));
    }

    private BDDMockito.BDDMyOngoingStubbing<Optional<CategoryBriefInfo>> givenFindBriefInfoById() {
        return given(categoryQueryRepository.findBriefInfoById(anyLong()));
    }

    @Test
    void getById_200() throws Exception {
        CategoryBriefInfo info = new CategoryBriefInfo(categoryId, "category", 0, null);
        givenFindBriefInfoById().willReturn(Optional.of(info));

        ResultActions ra = performGetById();

        ra
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.categoryId").value(info.getCategoryId()))
                .andExpect(jsonPath("$.categoryName").value(info.getCategoryName()))
                .andExpect(jsonPath("$.depth").value(info.getDepth()))
                .andExpect(jsonPath("$.parentId").value(info.getParentId()));
    }

    private ResultActions performGetById() throws Exception {
        return mvc.perform(
                MockMvcRequestBuilders.get(URL)
        ).andDo(print());
    }
}
