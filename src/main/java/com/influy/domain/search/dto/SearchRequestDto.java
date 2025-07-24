package com.influy.domain.search.dto;

import com.influy.global.validation.annotation.CheckPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class SearchRequestDto {
    @Getter
    public static class SearchDto {
        @Schema(description = "검색 내용", example = "팬츠")
        private String query;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class SellerPageRequestDto {
        @CheckPage
        private int page = 1;
        private int size = 10;

        public Pageable toPageable(Sort sort) {
            return PageRequest.of(page-1, size, sort);
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class ItemPageRequestDto {
        @CheckPage
        private int page = 1;
        private int size = 10;

        public Pageable toPageable(Sort sort) {
            return PageRequest.of(page-1, size, sort);
        }
    }
}
