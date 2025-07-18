package com.influy.domain.memberCategory.entity;

import com.influy.domain.category.entity.Category;
import com.influy.domain.member.entity.Member;
import com.influy.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberCategory extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
}
