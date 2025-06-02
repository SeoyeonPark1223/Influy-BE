package com.influy.domain.faqCard.entity;

import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.seller.entity.Seller;
import com.influy.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FaqCard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String questionContent;

    @Builder.Default
    private Boolean isPinned = false;

    @Column(length = 300)
    private String answerContent;

    @Column
    private String backgroundColor;

    @Column
    private String backgroundImageLink;

    @Column
    private String textColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faq_category_id")
    private FaqCategory faqCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;
}

