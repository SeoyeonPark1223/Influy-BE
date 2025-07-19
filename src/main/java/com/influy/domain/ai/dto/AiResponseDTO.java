package com.influy.domain.ai.dto;

import java.util.List;

public class AiResponseDTO {
    public static class QuestionClassification{
         private NewTagInfo targetQuestion;
         private List<OtherQuestionResult> otherQuestions;

        private static class NewTagInfo{
             private String newTagId;
             private String newTagName;
        }

        private static class OtherQuestionResult{
             private String questionId;
             private NewTagInfo newTagInfo;
        }
    }
}
