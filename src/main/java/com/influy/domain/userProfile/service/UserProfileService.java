package com.influy.domain.userProfile.service;

import com.influy.domain.member.entity.Member;
import com.influy.domain.userProfile.entity.UserProfile;

public interface UserProfileService {
    UserProfile createUserProfile(Member member);
}
