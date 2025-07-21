package com.influy.domain.ai.dto;

import lombok.Getter;

import java.util.List;

public class AiResponseDTO {

    @Getter
    public static class QuestionClassification{
         private NewTagInfo targetQuestion;
         private List<OtherQuestionResult> otherQuestions;

         @Getter
         public static class NewTagInfo {
             private Long newTagId;
             private String newTagName;
         }
         @Getter
         public static class OtherQuestionResult{
             private NewTagInfo newTagInfo;
             private List<Long> questionIds;
         }
    }
}
