package com.influy.domain.like.dto.jpql;

import com.influy.domain.like.entity.Like;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SellerLikeWithCntDto {
    @Schema(description = "셀러 좋아요")
    private Like like;

    @Schema(description = "해당 셀러의 좋아요 개수", example = "34")
    private Long cnt;
}
