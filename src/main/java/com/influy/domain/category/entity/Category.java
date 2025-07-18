package com.influy.domain.category.entity;

import com.influy.domain.itemCategory.entity.ItemCategory;
import com.influy.domain.memberCategory.entity.MemberCategory;
import com.influy.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<ItemCategory> itemCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<MemberCategory> memberCategories = new ArrayList<>();
}
