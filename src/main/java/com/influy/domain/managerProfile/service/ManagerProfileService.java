package com.influy.domain.managerProfile.service;

import com.influy.domain.managerProfile.entity.ManagerProfile;
import com.influy.domain.member.entity.Member;

public interface ManagerProfileService {
    ManagerProfile joinManager(Member member);
    ManagerProfile getManagerProfileByMemberId(Long id);

}
