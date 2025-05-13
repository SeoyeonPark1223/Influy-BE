package com.influy.domain.question.entity;

import com.influy.domain.answer.entity.Answer;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.seller.entity.Seller;
import com.influy.domain.user.entity.User;
import com.influy.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="question_category_id")
    private QuestionCategory questionCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="seller_id")
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @NotBlank
    private String content;

    @Builder.Default
    private Boolean isAnswered=false;
}
