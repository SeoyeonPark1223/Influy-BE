package com.influy.domain.image.converter;

import com.influy.domain.image.entity.Image;
import com.influy.domain.item.entity.Item;

public class ImageConverter {
    public static Image toImage(Item item, String imgLink, Boolean isMainImg) {
        return Image.builder()
                .item(item)
                .imageLink(imgLink)
                .isMainImg(isMainImg)
                .build();
    }
}
