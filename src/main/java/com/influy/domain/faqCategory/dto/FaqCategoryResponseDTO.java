package com.influy.domain.faqCategory.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class FaqCategoryResponseDTO {
    public class AddResultDto {
    }

    public class viewDto {
        private Long id;

        private String category;

        private Integer order;
    }

    public class PageDto {
        @Schema(description = "")
        private List<viewDto> viewList;

        private Integer listSize;
        private Integer totalPage;
        private Long totalElements;
        private Boolean isFirst;
        private Boolean isLast;
    }

    public class ResultDto {

    }

    public class UpdateDto {

    }
}
