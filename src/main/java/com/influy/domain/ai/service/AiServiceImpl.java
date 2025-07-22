package com.influy.domain.ai.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.influy.domain.ai.converter.AiConverter;
import com.influy.domain.ai.dto.AiRequestDTO;
import com.influy.domain.ai.dto.AiResponseDTO;
import com.influy.domain.item.entity.Item;
import com.influy.domain.question.entity.Question;
import com.influy.domain.question.repository.QuestionRepository;
import com.influy.domain.questionCategory.converter.QuestionCategoryConverter;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionCategory.repository.QuestionCategoryRepository;
import com.influy.domain.questionTag.converter.QuestionTagConverter;
import com.influy.domain.questionTag.entity.QuestionTag;
import com.influy.domain.questionTag.repository.QuestionTagRepository;
import com.influy.domain.questionTag.service.QuestionTagService;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ai.chat.client.ChatClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.influy.global.util.StaticValues.DEFAULT_QUESTION_CATEGORIES;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {
    private final ObjectMapper objectMapper;
    private final ChatClient chatClient;
    private final QuestionCategoryRepository questionCategoryRepository;
    private final QuestionTagService questionTagService;
    private final QuestionTagRepository questionTagRepository;

    private static final String CATEGORY_PROMPT_FILE_PATH = "src/main/resources/category-storage/aiQuestionCategory/prompt-question-category.txt";
//    private static final String CATEGORY_JSON_FILE_PATH = "src/main/resources/category-storage/question-category.json";
    private static final String QUESTION_CLASSIFICATION_FILE_PATH = "src/main/resources/category-storage/aiQuestionClassification/system-prompt.txt";

    @Override
    @Transactional
    public List<String> generateCategory(Item item) {
        // 상품 이름 + 상품 카테고리 (최대 3개) + 상품 한줄 소개 + 코멘트
        // 상품 관련 6개 질문 카테고리를 ai로 생성

        try {
            String prompt = buildPromptCategory(item);
            String cleanResponse = callAIClient(prompt);
            List<String> aiCategoryList = objectMapper.readValue(cleanResponse, new TypeReference<>() {});
            List<String> defaultCategoryList = Arrays.stream(DEFAULT_QUESTION_CATEGORIES).toList();
            aiCategoryList.addAll(defaultCategoryList); // 생성된 카테고리 + 디폴트 카테고리
            return aiCategoryList;
        } catch (IOException e) {
            throw new RuntimeException("Failed to process AI response", e);
        }
    }

    @Override
    @Transactional
    public QuestionTag classifyQuestion(String content, QuestionCategory questionCategory) {

        //2. AI에게 넘길 대분류 카테고리의 전체 questionTag를 DTO로 변환한 리스트
        //-- 추후에 태그 id 별로 재분류 가능성이 있어 미리 map 만들어두기
        Map<Long, QuestionTag> questionTagMap = questionCategory.getQuestionTagList().stream()
                .collect(Collectors.toMap(QuestionTag::getId, Function.identity()));
        List<AiRequestDTO.QuestionTag> questionTagDTOs = questionTagMap.values().stream().map(AiConverter::toAiQuestionTagDTO).toList();

        //3. 기타 소분류에 포함된 질문 목록 추출
        QuestionTag etcQuestionTag = questionTagRepository.findByQuestionCategoryAndName(questionCategory,"기타")
                .orElseThrow(()->new GeneralException(ErrorStatus.QUESTION_TAG_NOT_FOUND));
        //--후에 사용될 map 미리 만들어두기 2
        Map<Long, Question> questionMap = etcQuestionTag.getQuestionList().stream().collect(Collectors.toMap(Question::getId, Function.identity()));
        List<AiRequestDTO.Question> questionDTOs = questionMap.values().stream().map(AiConverter::toAiQuestionDTO).toList();

        //4. ai Client 호출
        try{
            String prompt = buildPromptClassifyQuestion(content, questionCategory.getName(),questionTagDTOs,questionDTOs);
            String cleanResponse = callAIClient(prompt);


            AiResponseDTO.QuestionClassification result = objectMapper.readValue(cleanResponse, AiResponseDTO.QuestionClassification.class);
            QuestionTag createdTag = null;
            if(result.getTargetQuestion().getNewTagId()==-1L){
                String tagName = result.getTargetQuestion().getNewTagName();
                //-1이면 새로운 태그 생성, 저장까지 완료
                createdTag = questionTagService.createTag(tagName, questionCategory);
            }

            //4. for문 돌며 get(tagid)해서 update 작업
            updateQuestionsToNewTag(result, createdTag, questionTagMap, questionMap);


            return createdTag == null ? questionTagMap.get(result.getTargetQuestion().getNewTagId()): createdTag;

        }catch (Exception e){
            throw new RuntimeException("Failed to process AI response", e);
        }
    }

    private String buildPromptClassifyQuestion(String content, String questionCategoryName,
                                               List<AiRequestDTO.QuestionTag> questionTagDTOs,
                                               List<AiRequestDTO.Question> questionDTOs) throws IOException {

        try {
            String template = Files.readString(Paths.get(QUESTION_CLASSIFICATION_FILE_PATH));
            return template
                    .replace("{{questioncategoryname}}", questionCategoryName)
                    .replace("{{questiontagdtolist}}", objectMapper.writeValueAsString(questionTagDTOs))
                    .replace("{{questiondtolist}}", objectMapper.writeValueAsString(questionDTOs))
                    .replace("{{content}}", content);
        } catch (IOException e) {
            throw new IOException("Failed to read prompt text", e);
        }
    }

    private String buildPromptCategory(Item item) throws IOException{
        try {
            String template = Files.readString(Paths.get(CATEGORY_PROMPT_FILE_PATH));
            return template
                    .replace("{{name}}", item.getName())
                    .replace("{{categories}}", item.getItemCategoryList().stream()
                            .map(i -> i.getCategory().getCategory())
                            .collect(Collectors.joining(", ")))
                    .replace("{{tagline}}", item.getTagline())
                    .replace("{{comment}}", item.getComment());
        } catch (IOException e) {
            throw new IOException("Failed to read prompt text", e);
        }
    }

    private String callAIClient(String prompt) {

        String response = chatClient.prompt()
                .system(prompt)
                .call()
                .content();

        //응답 정제(json 코드 블럭으로 줄 경우)
        return response
                .replaceAll("(?s)```json", "")
                .replaceAll("```", "")
                .trim();

    }


//    private void updateQuestionsToNewTag(Item item, List<String> aiCategories) throws IOException {
//
//        //기본 대분류
//        List<String> defaultCategoryList = Arrays.stream(DEFAULT_QUESTION_CATEGORIES).toList();
//        //ai 생성 카테고리에 더하기
//        aiCategories.addAll(defaultCategoryList);
//
//        for (String name:aiCategories) {
//            if (!questionCategoryRepository.existsByItemIdAndName(item.getId(), name.trim())) {
//                QuestionCategory category = QuestionCategoryConverter.toQuestionCategory(item, name.trim());
//                //각 카테고리마다 기타 태그 기본으로 생성
//                category.getQuestionTagList().add(QuestionTagConverter.toQuestionTag("기타", category));
//            }
//        }
//    }

    private void updateQuestionsToNewTag(AiResponseDTO.QuestionClassification result,
                                         QuestionTag createdTag, Map<Long,QuestionTag> questionTagMap,
                                         Map<Long, Question> questionMap) {

        for(AiResponseDTO.QuestionClassification.OtherQuestionResult questionResult : result.getOtherQuestions()){
            long tagId = questionResult.getNewTagInfo().getNewTagId();
            //현재 태그로 갱신해야할 질문
            List<Long> questionIds = questionResult.getQuestionIds();
            //현재 태그
            QuestionTag currentQuestionTag = tagId==-1 ? createdTag : questionTagMap.get(tagId);

            if(currentQuestionTag==null){
                //못찾았으면 예외처리
                throw new GeneralException(ErrorStatus.AI_CLASSIFICATION_ERROR);
            }

            //현재 태그로 업데이트되어야 할 질문 목록 순회하며 업데이트
            for(Long questionId : questionIds){
                Question question = questionMap.get(questionId);
                question.updateTag(currentQuestionTag);

            }
        }

    }
}
