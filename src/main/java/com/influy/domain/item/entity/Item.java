package com.influy.domain.item.entity;

import com.influy.domain.answer.entity.Answer;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.itemCategory.entity.ItemCategory;
import com.influy.domain.like.entity.Like;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
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

    @Builder.Default
    private Long regularPrice = 0L;

    @Builder.Default
    private Long salePrice = 0L;

    @Builder.Default
    private String tagline = "";

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @NotNull
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

    @Builder.Default
    private String marketLink = "";

    @Builder.Default
    @Lob
    private String comment = "";

    @Builder.Default
    @Setter
    private String talkBoxComment = "";

    @Builder.Default
    @Setter
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
                            Long regularPrice, Long salePrice, String marketLink, Integer itemPeriod, String comment, Boolean isArchived,
                            ItemStatus itemStatus, Boolean isDateUndefined) {
        this.name = name != null ? name : this.name;
        this.tagline = tagline != null ? tagline : this.tagline;
        this.regularPrice = regularPrice != null ? regularPrice : this.regularPrice;
        this.salePrice = salePrice != null ? salePrice : this.salePrice;
        this.marketLink = marketLink != null ? marketLink : this.marketLink;
        this.itemPeriod = itemPeriod != null ? itemPeriod : this.itemPeriod;
        this.comment = comment != null ? comment : this.comment;
        this.isArchived = isArchived != null ? isArchived : this.isArchived;
        this.itemStatus = itemStatus != null ? itemStatus : this.itemStatus;
        this.isDateUndefined = isDateUndefined != null ? isDateUndefined : this.isDateUndefined;

        if (this.isDateUndefined) {
            if (!this.isArchived) throw new GeneralException(ErrorStatus.ITEM_INFO_REQUIRED);
            // 기간 설정을 했다가 지웠거나 아예 안 한경우 -> startDate, endDate을 null로 저장
            this.startDate = null;
            this.endDate = null;

        } else {
            // 기간 설정 되어있는 것을 수정하거나 새로 정하거나 아예 수정하지 않은 경우 -> startDate, endDate 값 있으면 넣고 아니면 null로 저장
            this.startDate = startDate != null ? startDate : this.startDate;
            this.endDate = endDate != null ? endDate : this.endDate;
        }
    }
}