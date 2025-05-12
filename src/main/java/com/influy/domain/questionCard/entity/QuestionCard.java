package com.influy.domain.questionCard.entity;

import com.influy.domain.faqCategory.entity.FaqCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faq_category_id")
    private FaqCategory faqCategory;

    private String content;

    @Builder.Default
    private Boolean isPinned = false;

    @Builder.Default
    private Boolean isFrequent = false;
}
