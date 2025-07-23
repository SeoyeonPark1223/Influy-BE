package com.influy.domain.item.service;

import com.influy.domain.answer.dto.AnswerResponseDto;
import com.influy.domain.faqCard.dto.FaqCardResponseDto;
import com.influy.domain.item.dto.ItemRequestDto;
import com.influy.domain.item.dto.ItemResponseDto;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.entity.TalkBoxOpenStatus;
import com.influy.domain.sellerProfile.entity.ItemSortType;
import com.influy.global.apiPayload.code.status.SuccessStatus;
import com.influy.global.common.PageRequestDto;
import com.influy.global.jwt.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ItemService {
    Item create(CustomUserDetails userDetails, ItemRequestDto.DetailDto request);
    Item getDetail(Long sellerId, Long itemId);
    void delete(CustomUserDetails userDetails, Long itemId);
    Item update(CustomUserDetails userDetails, Long itemId, ItemRequestDto.DetailDto request);
    Item setAccess(CustomUserDetails userDetails, Long itemId, ItemRequestDto.AccessDto request);
    Item setStatus(CustomUserDetails userDetails, Long itemId, ItemRequestDto.StatusDto request);
    Integer getCount(Long sellerId, Boolean isArchived);
    ItemResponseDto.DetailPreviewPageDto getDetailPreviewPage(Long sellerId, Boolean isArchived, PageRequestDto pageRequest, ItemSortType sortType, Boolean isOnGoing, Long memberId);
    ItemResponseDto.ItemOverviewDto getItemOverview(Long sellerId, Long itemId);
    ItemResponseDto.TalkBoxOpenStatusDto changeOpenStatus(CustomUserDetails userDetails, Long itemId, TalkBoxOpenStatus openStatus);
    ItemResponseDto.ResultDto updateTalkBoxComment(CustomUserDetails userDetails, Long itemId, ItemRequestDto.TalkBoxCommentDto request);
    ItemResponseDto.ViewTalkBoxCommentDto getTalkBoxComment(CustomUserDetails userDetails, Long itemId);
    Item findById(Long itemId);
}
