package com.influy.domain.questionCategory.dto;

import com.influy.domain.question.dto.QuestionResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class QuestionCategoryResponseDTO {

    @Builder @Getter
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
