package com.influy.domain.question.entity;

import com.influy.domain.answer.entity.Answer;
import com.influy.domain.member.entity.Member;
import com.influy.domain.questionTag.entity.QuestionTag;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.global.common.BaseEntity;
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
public class Question extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="question_tag_id")
    private QuestionTag questionTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="seller_id")
    private SellerProfile seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="member_id")
    private Member member;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Answer> answerList = new ArrayList<>();

    @NotBlank
    private String content;

    @Builder.Default
    @Setter
    private Boolean isAnswered=false;

    @Builder.Default
    private Integer itemPeriod=1;

    @Setter
    @Builder.Default
    private Boolean isHidden = false;

    public Question updateTag(QuestionTag questionTag) {
        this.questionTag = questionTag;
        return this;
    }
}
