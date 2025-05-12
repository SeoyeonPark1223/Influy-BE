package com.influy.domain.seller.entity;

import com.influy.global.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

}
