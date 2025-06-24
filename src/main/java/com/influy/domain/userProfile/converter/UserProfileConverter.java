package com.influy.domain.userProfile.converter;

import com.influy.domain.member.entity.Member;
import com.influy.domain.userProfile.entity.UserProfile;

public class UserProfileConverter {

    public static UserProfile toUserProfile(Member member){
        return UserProfile.builder()
                .member(member)
                .build();
    }

}
