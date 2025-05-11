package com.influy.domain.item.domain;

import com.influy.domain.seller.domain.Seller;
import com.influy.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @NotBlank
    private String name;

    @NotBlank
    private Integer regularPrice;

    @NotBlank
    private Integer salePrice;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Builder.Default
    private Boolean archiveRecommended = true;

    @Builder.Default
    private Boolean searchAvailable = true;

    @Builder.Default
    private Integer reRunNum = 1;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ItemStatus status = ItemStatus.DEFAULT;  //표기 상태: [기본, 특수, 완판]

    @NotBlank
    private String marketUrl;

    private String comment;

    @Builder.Default
    private Boolean isArchived = false; //보관 여부

}
