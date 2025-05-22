package com.influy.domain.itemCategory.converter;

import com.influy.domain.category.entity.Category;
import com.influy.domain.item.entity.Item;
import com.influy.domain.itemCategory.entity.ItemCategory;

public class ItemCategoryConverter {
    public static ItemCategory toItemCategory(Category category, Item item) {
        return ItemCategory.builder()
                .item(item)
                .category(category)
                .build();
    }
}
