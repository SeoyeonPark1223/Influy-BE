package com.influy.domain.questionCategory.entity;

import com.influy.domain.item.entity.Item;
import com.influy.domain.questionTag.entity.QuestionTag;
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
public class QuestionCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @NotBlank
    @Setter
    private String name;

    @OneToMany(mappedBy = "questionCategory", cascade = CascadeType.ALL)
    @Builder.Default
    private List<QuestionTag> questionTagList = new ArrayList<>();
}
