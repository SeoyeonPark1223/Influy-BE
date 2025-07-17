package com.influy.domain.questionTag.repository;

import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionTag.entity.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionTagRepository extends JpaRepository<QuestionTag, Long> {
    Optional<QuestionTag> findByQuestionCategoryAndName(QuestionCategory questionCategory, String 기타);
}
