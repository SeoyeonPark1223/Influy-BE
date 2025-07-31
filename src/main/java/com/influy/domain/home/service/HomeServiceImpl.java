package com.influy.domain.home.service;

import com.influy.domain.category.repository.CategoryRepository;
import com.influy.domain.home.converter.HomeConverter;
import com.influy.domain.home.dto.HomeResponseDto;
import com.influy.domain.item.dto.jpql.ItemJPQLResponse;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.like.repository.LikeRepository;
import com.influy.domain.member.entity.Member;
import com.influy.domain.questionCategory.dto.jpql.CategoryJPQLResult;
import com.influy.domain.questionCategory.repository.QuestionCategoryRepository;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.sellerProfile.repository.SellerProfileRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.common.PageRequestDto;
import com.influy.global.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
    private final ItemRepository itemRepository;
    private final SellerProfileRepository sellerRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionCategoryRepository questionCategoryRepository;
    private final LikeRepository likeRepository;


    @Override
    @Transactional(readOnly = true)
    public HomeResponseDto.HomeItemViewPageDto getCloseDeadline(CustomUserDetails userDetails, PageRequestDto pageRequest) {
        List<Long> likeItems = getLikeItems(userDetails);

        // 남은 마감 시간이 24시간 이내 (SOLD_OUT & 마감일이 이미 지난 것 제외)
        LocalDateTime threshold = LocalDateTime.now().plusHours(24);
        Pageable pageable = pageRequest.toPageable(Sort.by(Sort.Direction.ASC, "endDate"));
        Page<Item> itemPage = itemRepository.findAllByEndDateAndItemStatus(LocalDateTime.now(), threshold, pageable);

        return HomeConverter.toHomeItemViewPageDto(itemPage, likeItems);
    }

    @Override
    @Transactional(readOnly = true)
    public HomeResponseDto.HomeItemViewPageDto getPopular(CustomUserDetails userDetails, PageRequestDto pageRequest) {
        List<Long> likeItems = getLikeItems(userDetails);

        // 질문 개수 top 3 (SOLD_OUT & 마감일이 이미 지난 것 제외)
        Pageable pageable = pageRequest.toPageable();
        Page<Item> itemPage = itemRepository.findTop3ByQuestionCnt(LocalDateTime.now(), pageable);

        return HomeConverter.toHomeItemViewPageDto(itemPage, likeItems);
    }

    @Override
    @Transactional(readOnly = true)
    public HomeResponseDto.HomeItemViewPageDto getRecommended(CustomUserDetails userDetails, PageRequestDto pageRequest, Long categoryId) {
        List<Long> likeItems = getLikeItems(userDetails);

        Pageable pageable = pageRequest.toPageable();
        Page<Item> itemPage;
        if (categoryId != null) {
            if (!categoryRepository.existsById(categoryId)) throw new GeneralException(ErrorStatus.ITEM_CATEGORY_NOT_FOUND);
            itemPage = itemRepository.findAllByCategoryId(categoryId, pageable, LocalDateTime.now());
        } else {
            itemPage = itemRepository.findAllNow(pageable, LocalDateTime.now());
        }

        return HomeConverter.toHomeItemViewPageDto(itemPage, likeItems);
    }

    @Override
    public HomeResponseDto.SellerHomeItemPageDTO getSellerHomeItemWithQuestionStatus(SellerProfile seller, PageRequestDto pageRequestDto) {
        Pageable pageable = pageRequestDto.toPageable();

        //아이템 조회(1순위 isChecked=false 많은 순, 2순위 pendingQuestions 많은 순)
        Page<ItemJPQLResponse.ItemWithQuestionStatus> items = itemRepository.getItemsWithQuestionStatus(seller.getId(), pageable);
        List<Long> itemIds = items.getContent().stream().map(item->item.getItem().getId()).toList();

        //현재 top3
        List<CategoryJPQLResult.TopNCategories> topNCategories;
        Map<Long,List<String>> topNCategoryMap = new HashMap<>();

        if(!itemIds.isEmpty()){
            topNCategories = questionCategoryRepository.getTopNCategoriesByNewQuestion(itemIds,3);

            for(CategoryJPQLResult.TopNCategories category : topNCategories) {
                topNCategoryMap.computeIfAbsent(category.getItemId(),k->new ArrayList<>()).add(category.getCategoryName());
            }

        }

        return HomeConverter.toSellerHomeItemPageDTO(items, topNCategoryMap);
    }

    @Override
    @Transactional(readOnly = true)
    public HomeResponseDto.SellerThumbnailListDto getTrendingSeller() {
        List<SellerProfile> sellerList = sellerRepository.findTop10ByIsPublicTrue();
        return HomeConverter.toSellerThumbnailListDto(sellerList);
    }

    @Override
    @Transactional(readOnly = true)
    public HomeResponseDto.SellerPick3Dto getPicked(Long sellerId) {
        SellerProfile seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SELLER_NOT_FOUND));
        List<Item> itemList = itemRepository.findTop3BySellerId(sellerId);
        return HomeConverter.toSellerPick3Dto(seller, itemList);
    }

    @Transactional
    @Override
    public List<Long> getLikeItems(CustomUserDetails userDetails) {
        List<Long> likeItems = new ArrayList<>();
        if (userDetails != null) {
            Member member = userDetails.getMember();
            likeItems = likeRepository.findLikedItemIdsByMember(member);
        }
        return likeItems;
    }
}
