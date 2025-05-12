package com.influy.domain.answerCard.entity;

import com.influy.domain.questionCard.entity.QuestionCard;
import com.influy.domain.seller.entity.Seller;
import com.influy.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Builder @Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerCard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="question_card_id")
    private QuestionCard questionCard;

    private String backgroundImage;

    private String content;

    private String textColorCode;

}
