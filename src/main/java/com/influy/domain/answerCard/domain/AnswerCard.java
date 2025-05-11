package com.influy.domain.answerCard.domain;

import com.influy.domain.question.domain.Question;
import com.influy.domain.seller.domain.Seller;
import com.influy.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Builder @Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerCard extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="question_id")
    private Question question;

    private String backgroundImage; //String으로 저장 or Image FK?

    private String content;

    private String textColorCode;

}
