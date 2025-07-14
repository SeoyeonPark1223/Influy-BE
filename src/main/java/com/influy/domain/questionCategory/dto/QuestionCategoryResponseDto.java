package com.influy.domain.questionCategory.dto;

import com.influy.domain.question.dto.QuestionResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.ArrayList;
import java.util.List;

public class QuestionCategoryResponseDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ViewDto {
        @Schema(description = "질문 카테고리 id", example = "1")
        private Long id;

        @Schema(description = "질문 카테고리", example = "사이즈")
        private String category;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DeleteResultDto {
        @Schema(description = "질문 카테고리 id", example = "1")
        private Long id;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class General {
        private Long id;
        private String name;
        private Integer pendingCnt;
        private Integer answeredCnt;
    }

    @Builder @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Preview {
        private Long id;
        private String name;
        private Integer pendingCnt;
        private Integer answeredCnt;
        @Builder.Default
        private List<QuestionResponseDTO.General> questions = new ArrayList<>();

        public void setCount(Boolean isAnswered, Long cnt){
            if(isAnswered){
                this.answeredCnt = cnt.intValue();
            }else{
                this.pendingCnt = cnt.intValue();
            }
        }
    }

}
