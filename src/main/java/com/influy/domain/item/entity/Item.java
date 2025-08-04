package com.influy.domain.item.entity;

import com.influy.domain.answer.entity.Answer;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.itemCategory.entity.ItemCategory;
import com.influy.domain.like.entity.Like;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private SellerProfile seller;

    @NotBlank
    private String name;

    private Long regularPrice;

    private Long salePrice;

    private String tagline;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @NotNull
    @Builder.Default
    private boolean isDateUndefined = false;

    @Builder.Default
    private Boolean isDateUndefined = false;

    @Builder.Default
    @Setter
    private Boolean archiveRecommended = true;

    @Builder.Default
    @Setter
    private Boolean searchAvailable = true;

    @Builder.Default
    private Integer itemPeriod = 1;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Setter
    private ItemStatus itemStatus = ItemStatus.DEFAULT;  //표기 상태: [기본, 연장, 완판]

    @NotBlank
    private String marketLink;

    private String comment;

    @Builder.Default
    @Setter
    private String talkBoxComment = "";

    @Builder.Default
    private Boolean isArchived = false; //보관 여부

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Answer> answerList = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Question> questionList = new ArrayList<>();

    @Builder.Default
    @Setter
    private String mainImg = "";

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Setter
    private TalkBoxOpenStatus talkBoxOpenStatus = TalkBoxOpenStatus.INITIAL; // [INITIAL, OPENED, CLOSED]

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FaqCategory> faqCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemCategory> itemCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<QuestionCategory> questionCategoryList = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "item_image", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "image_url")
    @OrderColumn(name = "image_order")
    @Builder.Default
    private List<String> imageList = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Like> likeList = new ArrayList<>();

    public void updateItem (String name, LocalDateTime startDate, LocalDateTime endDate, String tagline,
                            Long regularPrice, Long salePrice, String marketLink, Integer itemPeriod, String comment, Boolean isArchived) {
        this.name = name != null ? name : this.name;
        this.startDate = startDate != null ? startDate : this.startDate;
        this.endDate = endDate != null ? endDate : this.endDate;
        this.tagline = tagline != null ? tagline : this.tagline;
        this.regularPrice = regularPrice != null ? regularPrice : this.regularPrice;
        this.salePrice = salePrice != null ? salePrice : this.salePrice;
        this.marketLink = marketLink != null ? marketLink : this.marketLink;
        this.itemPeriod = itemPeriod != null ? itemPeriod : this.itemPeriod;
        this.comment = comment != null ? comment : this.comment;
        this.isArchived = isArchived != null ? isArchived : this.isArchived;
    }
}