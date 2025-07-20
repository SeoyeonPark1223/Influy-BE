package com.influy.domain.answer.service;

import com.influy.domain.answer.entity.Answer;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.entity.TalkBoxOpenStatus;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.member.service.MemberService;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final ItemRepository itemRepository;
    private final MemberService memberService;

    @Override
    @Transactional
    public Answer createIndividualAnswer(CustomUserDetails userDetails, Long itemId, Long questionCategoryId, Long questionTagId, Long questionId) {
        checkTalkBoxOpenStatus(itemId);


        return null;
    }

    private void checkTalkBoxOpenStatus(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));
        if (item.getTalkBoxOpenStatus() != TalkBoxOpenStatus.OPENED) throw new GeneralException(ErrorStatus.TALKBOX_CLOSED);
    }
}
