package com.influy.domain.ai.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.influy.domain.ai.service.converter.AiConverter;
import com.influy.domain.ai.service.dto.AiRequestDTO;
import com.influy.domain.item.entity.Item;
import com.influy.domain.questionCategory.converter.QuestionCategoryConverter;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionCategory.repository.QuestionCategoryRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.LinkedHashSet;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {
    private final ObjectMapper objectMapper;
    private final ChatClient chatClient;
    private final QuestionCategoryRepository questionCategoryRepository;
    private final QuestionTagRepository questionTagRepository;

    private static final String PROMPT_FILE_PATH = "src/main/resources/category-storage/prompt-question-category.txt";
    private static final String JSON_FILE_PATH = "src/main/resources/category-storage/question-category.json";

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
            saveToJson(aiCategories); // json 파일에 추가
            saveToRepository(item); // repository에 저장
        } catch (IOException e) {
            throw new RuntimeException("Failed to process AI response", e);
        }
    }

    @Override
    public String classifyQuestion(String content, QuestionCategory questionCategory) {

        //2. AI에게 넘길 대분류 카테고리의 전체 questionTag를 DTO로 변환한 리스트
        List<AiRequestDTO.QuestionTag> questionTagDTOs = questionCategory.getQuestionTagList().stream().map(AiConverter::toAiQuestionTagDTO).toList();

        //3. 기타 소분류에 포함된 질문 목록 추출
        QuestionTag ectQuestionTag = questionTagRepository.findByQuestionCategoryAndName(questionCategory,"기타")
                .orElseThrow(()->new GeneralException(ErrorStatus.QUESTION_TAG_NOT_FOUND));
        List<AiRequestDTO.Question> questionDTOs = ectQuestionTag.getQuestionList().stream().map(AiConverter::toAiQuestionDTO).toList();

        //4. ai Client 호출
        return "";
    }

    private String buildPromptCategory(Item item) throws IOException{
        try {
            String template = Files.readString(Paths.get(PROMPT_FILE_PATH));
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

    private void saveToJson(List<String> categories) throws IOException {
        File file = new File(JSON_FILE_PATH);

        List<Map<String, String>> defaultCat = new ArrayList<>();
        if (!file.exists()) throw new IOException("Failed to read question-category.json file");
        else if (file.exists() && file.length() > 0) defaultCat = objectMapper.readValue(file, new TypeReference<>() {});


        Set<String> defaultCatSet = defaultCat.stream()
                .map(map -> map.get("category"))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        categories.stream()
                .map(String::trim)
                .forEach(defaultCatSet::add);

        LinkedHashSet<String> mergedSet = new LinkedHashSet<>();
        mergedSet.addAll(categories);
        mergedSet.addAll(defaultCatSet);

        List<Map<String, String>> result = mergedSet.stream()
                .map(cat -> Map.of("category", cat))
                .collect(Collectors.toList());

        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, result);
    }

    private void saveToRepository(Item item) throws IOException {
        File file = new File(JSON_FILE_PATH);
        if (!file.exists()) throw new IOException("question-category.json file not found");

        List<Map<String, String>> categoryList = objectMapper.readValue(file, new TypeReference<>() {});

        for (Map<String, String> entry : categoryList) {
            String cat = entry.get("category").trim();

            if (!questionCategoryRepository.existsByCategory(cat)) {
                QuestionCategory category = QuestionCategoryConverter.toQuestionCategory(item, cat);
                questionCategoryRepository.save(category);
            }
        }
    }
}
