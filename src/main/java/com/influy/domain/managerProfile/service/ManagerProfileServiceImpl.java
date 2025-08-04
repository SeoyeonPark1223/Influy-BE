package com.influy.domain.managerProfile.service;

import com.influy.domain.managerProfile.converter.ManagerProfileConverter;
import com.influy.domain.managerProfile.entity.ManagerProfile;
import com.influy.domain.managerProfile.repository.ManagerProfileRepository;
import com.influy.domain.member.entity.Member;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManagerProfileServiceImpl implements ManagerProfileService {

    private final ManagerProfileRepository managerProfileRepository;

    @Override
    @Transactional
    public ManagerProfile joinManager(Member member) {
        return ManagerProfileConverter.toManagerProfile(member);
    }

    @Override
    public ManagerProfile getManagerProfileByMemberId(Long id) {
        return managerProfileRepository.findById(id).orElseThrow(()->new GeneralException(ErrorStatus.MANAGER_NOT_FOUND));
    }
}
