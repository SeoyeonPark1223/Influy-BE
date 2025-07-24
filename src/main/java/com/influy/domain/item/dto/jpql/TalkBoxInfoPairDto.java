package com.influy.domain.item.dto.jpql;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class TalkBoxInfoPairDto {
    @Schema(description = "아이템 id", example = "1")
    private Long itemId;

    @Schema(description = "답변 여부", example = "false")
    private Boolean isAnswered;

    @Schema(description = "해당 개수 (waiting / completed)", example = "5")
    private Long cnt;

}
