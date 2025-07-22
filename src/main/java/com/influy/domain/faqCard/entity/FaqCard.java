package com.influy.domain.faqCard.entity;

import com.influy.domain.answer.entity.Answer;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.member.entity.Member;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.global.common.BaseEntity;
import jakarta.annotation.Nullable;
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

    @Builder.Default
    private Boolean adjustImg = false;

    @Builder.Default
    @Column(length = 300)
    private String answerContent = "";

    @Column
    private String backgroundImageLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faq_category_id")
    private FaqCategory faqCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private SellerProfile seller;
}

