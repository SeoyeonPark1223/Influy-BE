package com.influy.domain.item.dto.jpql;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ItemJPQLResponse {
    private Boolean isArchived;
    private Long count;
}
