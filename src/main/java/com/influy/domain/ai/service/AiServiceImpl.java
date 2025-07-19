package com.influy.domain.ai.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.influy.domain.ai.converter.AiConverter;
import com.influy.domain.ai.dto.AiRequestDTO;
import com.influy.domain.ai.dto.AiResponseDTO;
import com.influy.domain.item.entity.Item;
import com.influy.domain.questionCategory.converter.QuestionCategoryConverter;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionCategory.repository.QuestionCategoryRepository;
import com.influy.domain.questionTag.converter.QuestionTagConverter;
import com.influy.domain.questionTag.entity.QuestionTag;
import com.influy.domain.questionTag.repository.QuestionTagRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ai.chat.client.ChatClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.influy.global.util.StaticValues.DEFAULT_QUESTION_CATEGORIES;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {
    private final ObjectMapper objectMapper;
    private final ChatClient chatClient;
    private final QuestionCategoryRepository questionCategoryRepository;
    private final QuestionTagRepository questionTagRepository;

    private static final String CATEGORY_PROMPT_FILE_PATH = "src/main/resources/category-storage/aiQuestionCategory/prompt-question-category.txt";
    private static final String CATEGORY_JSON_FILE_PATH = "src/main/resources/category-storage/question-category.json";
    private static final String QUESTION_CLASSIFICATION_FILE_PATH = "src/main/resources/category-storage/aiQuestionClassification/system-prompt.txt";

    @Override
    @Transactional
    public void generateCategory(Item item) {
        // 상품 이름 + 상품 카테고리 (최대 3개) + 상품 한줄 소개 + 코멘트
        // 상품 관련 6개 질문 카테고리를 ai로 생성

        try {
            String prompt = buildPromptCategory(item);
            String response = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            // 응답 정제
            String cleanResponse = response
                    .replaceAll("(?s)```json", "")
                    .replaceAll("```", "")
                    .trim();

            System.out.println(cleanResponse);
            List<String> aiCategories = objectMapper.readValue(cleanResponse, new TypeReference<>() {});
            saveToRepository(item, aiCategories); // repository에 저장
        } catch (IOException e) {
            throw new RuntimeException("Failed to process AI response", e);
        }
    }

    @Override
    public String classifyQuestion(String content, QuestionCategory questionCategory) {

        //2. AI에게 넘길 대분류 카테고리의 전체 questionTag를 DTO로 변환한 리스트
        List<AiRequestDTO.QuestionTag> questionTagDTOs = questionCategory.getQuestionTagList().stream().map(AiConverter::toAiQuestionTagDTO).toList();

        //3. 기타 소분류에 포함된 질문 목록 추출
        QuestionTag etcQuestionTag = questionTagRepository.findByQuestionCategoryAndName(questionCategory,"기타")
                .orElseThrow(()->new GeneralException(ErrorStatus.QUESTION_TAG_NOT_FOUND));
        List<AiRequestDTO.Question> questionDTOs = etcQuestionTag.getQuestionList().stream().map(AiConverter::toAiQuestionDTO).toList();

        //4. ai Client 호출
        try{
            String prompt = buildPromptClassifyQuestion(content, questionCategory.getName(),questionTagDTOs,questionDTOs);
            System.out.println(prompt);
            String response = chatClient.prompt()
                    .system(prompt)
                    .call()
                    .content();

            //응답 정제(json 코드 블럭으로 줄 경우)
            String cleanResponse = response
                    .replaceAll("(?s)```json", "")
                    .replaceAll("```", "")
                    .trim();

            System.out.println(cleanResponse);
            AiResponseDTO.QuestionClassification result = objectMapper.readValue(cleanResponse, AiResponseDTO.QuestionClassification.class);


        }catch (Exception e){
            throw new RuntimeException("Failed to process AI response", e);
        }
        return "";
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

    private void saveToRepository(Item item, List<String> aiCategories) throws IOException {

        //기본 대분류
        List<String> defaultCategoryList = Arrays.stream(DEFAULT_QUESTION_CATEGORIES).toList();
        //ai 생성 카테고리에 더하기
        aiCategories.addAll(defaultCategoryList);

        for (String name:aiCategories) {

            if (!questionCategoryRepository.existsByItemIdAndName(item.getId(), name.trim())) {
                QuestionCategory category = QuestionCategoryConverter.toQuestionCategory(item, name.trim());
                //각 카테고리마다 기타 태그 기본으로 생성
                category.getQuestionTagList().add(QuestionTagConverter.toQuestionTag("기타", category, new ArrayList<>()));
                questionCategoryRepository.save(category);
            }
        }
    }
}
