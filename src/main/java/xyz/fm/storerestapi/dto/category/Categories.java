package xyz.fm.storerestapi.dto.category;

import xyz.fm.storerestapi.entity.category.Category;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Categories {

    Integer totalDepth;
    List<List<CategoryInfoWithParent>> depth = new ArrayList<>();

    public Categories(List<Category> categories) {
        categories.sort(Comparator.comparing(Category::getDepth));
        for (Category category : categories) {
            if (category.getDepth() == depth.size()) {
                depth.add(new ArrayList<>());
            }

            depth.get(category.getDepth()).add(new CategoryInfoWithParent(category));
        }

        totalDepth = depth.size();
    }

    public Integer getTotalDepth() {
        return totalDepth;
    }

    public List<List<CategoryInfoWithParent>> getDepth() {
        return depth;
    }

    public List<CategoryInfoWithParent> getByDepth(int depth) {
        if (depth > totalDepth) return null;
        else return this.depth.get(depth);
    }
}
