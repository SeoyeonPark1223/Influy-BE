package com.influy.domain.question;

import com.influy.domain.answer.Answer;
import com.influy.domain.questionCategory.QuestionCategory;
import com.influy.domain.seller.Seller;
import com.influy.domain.user.User;
import com.influy.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Question extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User from;

    @OneToOne(fetch = FetchType.LAZY)
    private Seller to;

    @ManyToOne(fetch = FetchType.LAZY)
    private QuestionCategory category;

    //재고려 필요
    @ManyToOne(fetch = FetchType.LAZY)
    private Answer answer;

    @NotBlank
    private String content;

    @Builder.Default
    private Boolean isQuestionCard = false;

    @Builder.Default
    private Boolean isAnswered=false;



}
