package com.influy.domain.seller.entity;

import com.influy.domain.announcement.entity.Announcement;
import com.influy.domain.answer.entity.Answer;
import com.influy.domain.answerCard.entity.AnswerCard;
import com.influy.domain.item.entity.Item;
import com.influy.domain.profileLink.entity.ProfileLink;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionCard.entity.QuestionCard;
import com.influy.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seller extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nickname;

    @Builder.Default
    private Boolean isPublic = true;

    private String profileImg;

    private String backgroundImg;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ItemSortType itemSortType = ItemSortType.CREATE_DATE;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Announcement> announcementList = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ProfileLink> profileLinkList = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Answer> answerList = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Item> itemList = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Question> questionList = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @Builder.Default
    private List<QuestionCard> questionCardList = new ArrayList<>();
}
