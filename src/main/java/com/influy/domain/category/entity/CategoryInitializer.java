package com.influy.domain.category.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.influy.domain.category.repository.CategoryRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryInitializer implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            // JAR 내부 classpath에서 JSON 파일 로드
            try (InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream("category-storage/category.json")) {

                if (inputStream == null) {
                    throw new IOException("category.json 파일을 찾을 수 없습니다.");
                }

                // JSON 파일을 Java 객체(List<Category>)로 변환
                List<Category> categoryList = objectMapper.readValue(
                        inputStream,
                        new TypeReference<List<Category>>() {}
                );

                // DB에 저장
                categoryRepository.saveAll(categoryList);

            } catch (IOException e) {
                throw new RuntimeException("JSON 파일 로드 중 오류 발생: " + e.getMessage(), e);
            }
        }
    }

}
