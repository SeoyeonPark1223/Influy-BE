package com.influy.domain.faqCategory.entity;

import com.influy.domain.faqCard.entity.FaqCard;
import com.influy.domain.item.entity.Item;
import com.influy.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FaqCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private String category;

    private Integer categoryOrder;

    @OneToMany(mappedBy = "faqCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FaqCard> faqCardList = new ArrayList<>();
}
