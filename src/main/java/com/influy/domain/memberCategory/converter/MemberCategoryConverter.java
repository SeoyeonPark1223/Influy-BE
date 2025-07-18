package com.influy.domain.memberCategory.converter;

import com.influy.domain.category.entity.Category;
import com.influy.domain.member.entity.Member;
import com.influy.domain.memberCategory.entity.MemberCategory;

public class MemberCategoryConverter {
    public static MemberCategory toMemberCategory(Member member, Category category) {
        return MemberCategory.builder()
                .member(member)
                .category(category)
                .build();
    }
}
