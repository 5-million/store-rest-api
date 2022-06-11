package xyz.fm.storerestapi.dto.category;

import lombok.Getter;
import xyz.fm.storerestapi.entity.Category;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Categories {

    List<CategoryInherit> categories = new ArrayList<>();

    public Categories(List<Category> categories) {
        for (Category category : categories) {
            this.categories.add(new CategoryInherit(category));
        }
    }
}
