package xyz.fm.storerestapi.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemOptions {

    private List<String> options;

    private ItemOptions(String options) {
        this.options = new ArrayList<>(Arrays.asList(options.split(",")));
    }

    public static ItemOptions of(String options) {
        return new ItemOptions(options);
    }

    public int size() {
        return options.size();
    }

    public String get(int index) {
        if (index < 0 || index >= this.options.size()) {
            throw new NullPointerException();
        }

        return options.get(index);
    }

    public boolean isEmpty() {
        return options.isEmpty();
    }
}
