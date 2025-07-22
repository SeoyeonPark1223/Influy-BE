package com.influy.domain.item.dto.jpql;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ItemJPQLResponse {
    @AllArgsConstructor
    @Getter
    public static class ItemCount{
        private Boolean isArchived;
        private Long count;
    }
}
