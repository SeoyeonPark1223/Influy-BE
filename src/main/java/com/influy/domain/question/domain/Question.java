package com.influy.domain.question.domain;

import com.influy.domain.answer.domain.Answer;
import com.influy.domain.questionCategory.domain.QuestionCategory;
import com.influy.domain.seller.domain.Seller;
import com.influy.domain.user.domain.User;
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

    @OneToOne(fetch = FetchType.LAZY)
    private User from;

    @OneToOne(fetch = FetchType.LAZY)
    private Seller to;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="question_category_id")
    private QuestionCategory category;

    //재고려 필요
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn
    private Answer answer;

    @NotBlank
    private String content;

    @Builder.Default
    private Boolean isQuestionCard = false;

    @Builder.Default
    private Boolean isAnswered=false;



}
