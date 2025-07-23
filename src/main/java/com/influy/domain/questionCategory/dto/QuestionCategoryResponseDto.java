package com.influy.domain.questionCategory.dto;

import com.influy.domain.question.dto.QuestionResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class QuestionCategoryResponseDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ViewListDto {
        @Schema(description = "질문 카테고리 리스트", example = "[ 사이즈, 세탁, 색상 ]")
        private List<ViewDto> viewList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListWithCntDto {
        @Schema(description = "질문 카테고리 리스트")
        private List<ViewWithCntDto> viewList;

        private Integer listSize;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ViewWithCntDto {
        @Schema(description = "질문 카테고리 id", example = "1")
        private Long questionCategoryId;

        @Schema(description = "질문 카테고리", example = "사이즈")
        private String questionCategoryName;

        @Schema(description = "해당 카테고리의 질문 개수", example = "6")
        private Integer questionCnt;

        @Schema(description = "해당 카테고리의 미확인 질문 개수 (아직 개발 미완료)", example = "1")
        private Integer unCheckedCnt;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ViewDto {
        @Schema(description = "질문 카테고리 id", example = "1")
        private Long questionCategoryId;

        @Schema(description = "질문 카테고리", example = "사이즈")
        private String questionCategoryName;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GeneratedResultDto {
        @Schema(description = "생성된 질문 카테고리 리스트", example = "[ 사이즈, 세탁, 색상 ]")
        private List<String> generatedNameList;
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
