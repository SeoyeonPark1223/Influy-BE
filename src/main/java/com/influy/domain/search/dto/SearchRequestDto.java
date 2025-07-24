package com.influy.domain.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

public class SearchRequestDto {
    @Getter
    public static class SearchDto {
        @Schema(description = "검색 내용", example = "팬츠")
        private String query;
    }
}
