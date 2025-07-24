package com.influy.domain.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

public class QuestionRequestDTO {

    @Getter
    public static class Create{
        @Schema(description = "질문 내용", example = "폐업하시면 저는 옷 어디서 사나요?")
        private String content;
    }

    @Getter
    public static class DeleteDto {
        @Schema(description = "삭제할 질문 id 리스트 (1개도 가능)", example = "[1, 2]")
        private List<Long> questionIdList;
    }
}
