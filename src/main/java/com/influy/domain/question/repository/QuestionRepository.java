package com.influy.domain.question.repository;

import com.influy.domain.question.entity.Question;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Integer countByQuestionCategoryAndIsAnswered(QuestionCategory category, boolean b);
}
