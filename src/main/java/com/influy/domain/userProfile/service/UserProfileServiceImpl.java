package com.influy.domain.userProfile.service;

import com.influy.domain.member.entity.Member;
import com.influy.domain.userProfile.converter.UserProfileConverter;
import com.influy.domain.userProfile.entity.UserProfile;
import com.influy.domain.userProfile.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    @Transactional
    public UserProfile createUserProfile(Member member) {

        UserProfile userProfile = UserProfileConverter.toUserProfile(member);

        return userProfileRepository.save(userProfile);

    }
}
