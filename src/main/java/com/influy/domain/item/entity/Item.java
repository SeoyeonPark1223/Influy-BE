package com.influy.domain.item.entity;

import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.itemCategory.entity.ItemCategory;
import com.influy.domain.seller.entity.Seller;
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
    private Seller seller;

    @NotBlank
    private String name;

    @NotBlank
    private Integer regularPrice;

    @NotBlank
    private Integer salePrice;

    private String tagline;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Builder.Default
    private Boolean archiveRecommended = true;

    @Builder.Default
    private Boolean searchAvailable = true;

    @Builder.Default
    private Integer itemPeriod = 1;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ItemStatus currentStatus = ItemStatus.DEFAULT;  //표기 상태: [기본, 완판]

    @NotBlank
    private String marketUrl;

    private String comment;

    @Builder.Default
    private Boolean isArchived = false; //보관 여부

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @Builder.Default
    private List<FaqCategory> faqCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ItemCategory> itemCategoryList = new ArrayList<>();

}
