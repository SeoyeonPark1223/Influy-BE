package com.influy.domain.questionTag.entity;

import com.influy.domain.item.entity.Item;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "question_tag",
        indexes = {
                @Index(name = "idx_question_tag_category", columnList = "question_category_id")
        }
)
public class QuestionTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_category_id")
    private QuestionCategory questionCategory;

    @NotBlank
    private String name;

    @OneToMany(mappedBy = "questionTag", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Question> questionList = new ArrayList<>();
}
