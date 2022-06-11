package xyz.fm.storerestapi.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRegisterRequest {

    private Long parentId;
    private String categoryName;
}
