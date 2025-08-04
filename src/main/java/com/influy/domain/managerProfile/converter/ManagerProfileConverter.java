package com.influy.domain.managerProfile.converter;

import com.influy.domain.managerProfile.entity.ManagerProfile;
import com.influy.domain.member.entity.Member;

public class ManagerProfileConverter {
    public static ManagerProfile toManagerProfile(Member member) {
        return ManagerProfile.builder()
                .member(member)
                .build();
    }
}
