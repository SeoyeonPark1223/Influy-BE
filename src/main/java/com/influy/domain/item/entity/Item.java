package com.influy.domain.item.entity;

import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.image.entity.Image;
import com.influy.domain.itemCategory.entity.ItemCategory;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.seller.entity.Seller;
import com.influy.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @NotBlank
    private String name;

    private Long regularPrice;

    private Long salePrice;

    private String tagline;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @Builder.Default
    private Boolean archiveRecommended = true;

    @Builder.Default
    private Boolean searchAvailable = true;

    @Builder.Default
    private Integer itemPeriod = 1;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus = ItemStatus.DEFAULT;  //표기 상태: [기본, 연장, 완판]

    @NotBlank
    private String marketLink;

    private String comment;

    @Builder.Default
    private Boolean isArchived = false; //보관 여부

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FaqCategory> faqCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemCategory> itemCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<QuestionCategory> questionCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Image> imageList = new ArrayList<>();

}
