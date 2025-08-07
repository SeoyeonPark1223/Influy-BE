package com.influy.domain.item.service;

import com.influy.domain.category.entity.Category;
import com.influy.domain.category.repository.CategoryRepository;
import com.influy.domain.image.service.ImageService;
import com.influy.domain.item.converter.ItemConverter;
import com.influy.domain.item.dto.ItemRequestDto;
import com.influy.domain.item.dto.ItemResponseDto;
import com.influy.domain.item.dto.jpql.TalkBoxInfoPairDto;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.entity.TalkBoxInfoPair;
import com.influy.domain.item.entity.TalkBoxOpenStatus;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.itemCategory.converter.ItemCategoryConverter;
import com.influy.domain.itemCategory.entity.ItemCategory;
import com.influy.domain.like.entity.LikeStatus;
import com.influy.domain.like.entity.TargetType;
import com.influy.domain.like.repository.LikeRepository;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.entity.MemberRole;
import com.influy.domain.member.repository.MemberRepository;
import com.influy.domain.member.service.MemberService;
import com.influy.domain.question.repository.QuestionRepository;
import com.influy.domain.sellerProfile.entity.ItemSortType;
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
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final SellerProfileRepository sellerRepository;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final QuestionRepository questionRepository;
    private final LikeRepository likeRepository;
    private final ImageService imageService;

    @Override
    @Transactional
    public Item create(CustomUserDetails userDetails, ItemRequestDto.DetailDto request) {
        SellerProfile seller = memberService.checkSeller(userDetails);
        if (!request.getIsArchived() && (request.getItemImgList() == null || request.getIsDateUndefined() || request.getItemCategoryIdList() == null)) {
            // 아이템 게시할때 필수 필드: 사진, 제목(게시, 보관 공통), 기간 (undefined=false여야함), 아이템 카테고리
                throw new GeneralException(ErrorStatus.ITEM_INFO_REQUIRED);
        }

        Item item = ItemConverter.toItem(seller, request);

        if (!CollectionUtils.isEmpty(request.getItemImgList())) createItemImgList(request, item);
        if (!CollectionUtils.isEmpty(request.getItemCategoryIdList())) createItemCategoryList(request, item);

        item = itemRepository.save(item);
        seller.getItemList().add(item);

        return item;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto.DetailViewDto getDetail(CustomUserDetails userDetails, Long sellerId, Long itemId) {
        if (!sellerRepository.existsById(sellerId)) {
            throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        boolean isLiked = false;
        boolean isUnchecked = false;

        if (userDetails != null) {
            Member member = memberRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

            isLiked = likeRepository.findByMemberIdAndItemIdAndTargetType(
                    member.getId(), itemId, TargetType.ITEM
            ).filter(like -> like.getLikeStatus() == LikeStatus.LIKE).isPresent();

            if (member.getRole() == MemberRole.SELLER) {
                isUnchecked = questionRepository.existsByItemIdAndIsCheckedFalse(itemId);
            }
        }

        return ItemConverter.toDetailViewDto(item, isLiked, isUnchecked);
    }

    @Override
    @Transactional
    public void delete(CustomUserDetails userDetails, Long itemId) {
        SellerProfile seller = memberService.checkSeller(userDetails);
        Item item = itemRepository.findByIdAndSeller(itemId, seller)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        imageService.deleteImg(item.getImageList());

        seller.getItemList().remove(item);
        itemRepository.delete(item);
    }

    @Override
    @Transactional
    public Item update(CustomUserDetails userDetails, Long itemId, ItemRequestDto.DetailDto request) {
        SellerProfile seller = memberService.checkSeller(userDetails);

        Item item = itemRepository.findByIdAndSeller(itemId, seller)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        item.updateItem(request.getName(), request.getStartDate(), request.getEndDate(), request.getTagline(),
                request.getRegularPrice(), request.getSalePrice(), request.getMarketLink() , request.getItemPeriod(), request.getComment(), request.getIsArchived(),
                request.getStatus(), request.getIsDateUndefined());

        if (request.getItemImgList() != null) {
            List<String> curImgList = new ArrayList<>(item.getImageList());
            List<String> newImgList = request.getItemImgList();

            List<String> delImgList = curImgList.stream()
                    .filter(oldImg -> !newImgList.contains(oldImg))
                    .toList();

            imageService.deleteImg(delImgList);

            item.getImageList().clear();
            item.setMainImg("");
            createItemImgList(request, item);
        }

        if (request.getItemCategoryIdList() != null) {
            item.getItemCategoryList().clear();
            createItemCategoryList(request, item);
        }

        return item;
    }

    @Override
    @Transactional
    public Item setAccess(CustomUserDetails userDetails, Long itemId, ItemRequestDto.AccessDto request) {
        SellerProfile seller = memberService.checkSeller(userDetails);
        Item item = itemRepository.findByIdAndSeller(itemId, seller)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        if (request.getArchiveRecommended() != null) item.setArchiveRecommended(request.getArchiveRecommended());
        if (request.getSearchAvailable() != null) item.setSearchAvailable(request.getSearchAvailable());

        return item;
    }

    @Override
    @Transactional
    public Item setStatus(CustomUserDetails userDetails, Long itemId, ItemRequestDto.StatusDto request) {
        SellerProfile seller = memberService.checkSeller(userDetails);

        Item item = itemRepository.findByIdAndSeller(itemId, seller)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        if (request.getStatus() != null) item.setItemStatus(request.getStatus());

        return item;
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getCount(Long sellerId, Boolean isArchived) {
        if (!sellerRepository.existsById(sellerId)) {
            throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        }

        if (isArchived) return itemRepository.countBySellerIdAndIsArchivedTrue(sellerId);
        else return itemRepository.countBySellerIdAndIsArchivedFalse(sellerId);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto.DetailPreviewPageDto getDetailPreviewPage(CustomUserDetails userDetails, Long sellerId, Boolean isArchived, PageRequestDto pageRequest, ItemSortType sortType, Boolean isOnGoing) {
        MemberRole memberRole = MemberRole.SELLER;
        List<Long> likeItems = new ArrayList<>();

        if (userDetails != null) {
            Member member = memberRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
            if (member.getRole() == MemberRole.USER) memberRole = MemberRole.USER;
            likeItems = likeRepository.findLikedItemIdsByMember(member);

        }

        if (isArchived && (memberRole == MemberRole.USER || userDetails == null)) throw new GeneralException(ErrorStatus.NOT_OWNER);

        SellerProfile seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SELLER_NOT_FOUND));
        sortType = seller.getItemSortType();

        String sortField = switch (sortType) {
            case CREATE_DATE -> "createdAt";
            case END_DATE -> "endDate";
            default -> throw new GeneralException(ErrorStatus.UNSUPPORTED_SORT_TYPE);
        };
        Sort.Direction direction = switch (sortType) {
            case CREATE_DATE -> Sort.Direction.DESC; // 등록순
            case END_DATE -> Sort.Direction.ASC;     // 마감일 빠른순
        };

        Pageable pageable = pageRequest.toPageable(Sort.by(direction, sortField));
        Page<Item> itemPage;

        if (!isArchived & isOnGoing) {
            // 보관 상품 아닌 것 중에서 진행 중 상품 필터 적용
            if (sortType == ItemSortType.END_DATE) {
                // 마감일 빠른 순 -> endDate = null
                itemPage= itemRepository.findOngoingItemsSortedByEndDate(sellerId, LocalDateTime.now(), isArchived, pageable);
            } else {
                // 최신 생성 순
                itemPage = itemRepository.findOngoingItemsSortedByCreatedAt(sellerId, LocalDateTime.now(), isArchived, pageable);
            }
        } else if (!isArchived & !isOnGoing) {
            // 보관 상품 아닌 것 중에서 진행 중 상품 필터 미적용
            if (sortType == ItemSortType.END_DATE) {
                // 마감일 빠른 순 -> endDate = null -> endDate < now
                itemPage = itemRepository.findAllSortedByEndDate(sellerId, LocalDateTime.now(), false, pageable);
            } else {
                // sortType == ItemSortType.CREATE_DATE
                // 최신 생성 순 -> endDate = null -> endDate < now
                itemPage = itemRepository.findAllSortedByCreatedAt(sellerId, LocalDateTime.now(), false, pageable);
            }
        } else {
            // 보관 상품 (진행 중 상품 필터 없음)
            if (sortType == ItemSortType.END_DATE) {
                itemPage = itemRepository.findAllSortedByEndDate(sellerId, LocalDateTime.now(), true, pageable);
            } else {
                itemPage = itemRepository.findAllSortedByCreatedAt(sellerId, LocalDateTime.now(), true, pageable);
            }
        }

        TalkBoxInfoPair talkBoxInfoPair = getTalkBoxInfoPair(itemPage.getContent());

        return ItemConverter.toDetailPreviewPageDto(itemPage, likeItems, memberRole, talkBoxInfoPair.waitingCntMap(), talkBoxInfoPair.completedCntMap(), sortType);
    }

    private void createItemCategoryList(ItemRequestDto.DetailDto request, Item item) {
        List<Long> itemCategoryLongList = request.getItemCategoryIdList();
        for (Long lg : itemCategoryLongList) {
            Category category = categoryRepository.findById(lg)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_CATEGORY_NOT_FOUND));
            ItemCategory itemCategory = ItemCategoryConverter.toItemCategory(category, item);
            category.getItemCategoryList().add(itemCategory);
            item.getItemCategoryList().add(itemCategory);
        }
    }

    private void createItemImgList(ItemRequestDto.DetailDto request, Item item) {
        item.getImageList().addAll(request.getItemImgList());
        if(!CollectionUtils.isEmpty(request.getItemImgList())){
            item.setMainImg(item.getImageList().getFirst());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto.ItemOverviewDto getItemOverview(Long sellerId, Long itemId) {
        sellerRepository.findById(sellerId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SELLER_NOT_FOUND));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        return ItemConverter.toItemOverviewDto(item);
    }

    @Override
    @Transactional
    public ItemResponseDto.TalkBoxOpenStatusDto changeOpenStatus(CustomUserDetails userDetails, Long itemId, TalkBoxOpenStatus openStatus) {
        SellerProfile seller = memberService.checkSeller(userDetails);
        Item item = itemRepository.findByIdAndSeller(itemId, seller)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        if (openStatus == TalkBoxOpenStatus.INITIAL) throw new GeneralException((ErrorStatus.INVALID_TALKBOX_REQUEST));
        item.setTalkBoxOpenStatus(openStatus);

        return ItemConverter.toTalkBoxOpenStatusDto(itemId, openStatus);
    }

    @Override
    @Transactional
    public ItemResponseDto.ResultDto updateTalkBoxComment(CustomUserDetails userDetails, Long itemId, ItemRequestDto.TalkBoxCommentDto request) {
        SellerProfile seller = memberService.checkSeller(userDetails);
        Item item = itemRepository.findByIdAndSeller(itemId, seller)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        item.setTalkBoxComment(request.getTalkBoxComment());
        return ItemConverter.toResultDto(itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto.ViewTalkBoxCommentDto getTalkBoxComment(CustomUserDetails userDetails, Long itemId) {
        SellerProfile seller = memberService.checkSeller(userDetails);
        Item item = itemRepository.findByIdAndSeller(itemId, seller)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));
        return ItemConverter.toViewTalkBoxCommentDto(seller, item);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto.TalkBoxOpenedListDto getTalkBoxOpened(CustomUserDetails userDetails) {
        SellerProfile seller = memberService.checkSeller(userDetails);
        List<Item> itemList = itemRepository.findAllBySellerIdAndTalkBoxOpenStatus(seller.getId());
        boolean flag = true;
        TalkBoxInfoPair talkBoxInfoPair = null;
        Map<Long, Integer> uncheckedCntMap = new HashMap<>();


        if (!itemList.isEmpty()) {
            talkBoxInfoPair = getTalkBoxInfoPair(itemList);

            // 아이템 기준 미확인 질문 개수
            uncheckedCntMap = itemList.stream()
                    .collect(Collectors.toMap(
                            Item::getId,
                            item -> questionRepository.countQuestionsByItemIdAndIsChecked(item.getId(), false)
                    ));
        } else flag = itemRepository.existsBySellerId(seller.getId());

        return ItemConverter.toTalkBoxOpenedListDto(itemList, flag, talkBoxInfoPair != null ? talkBoxInfoPair.waitingCntMap() : null, talkBoxInfoPair != null ? talkBoxInfoPair.completedCntMap() : null, uncheckedCntMap);
    }

    @Override
    @Transactional(readOnly = true)
    public Item findById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));
    }

    @Override
    @Transactional
    public ItemResponseDto.ResultDto updateArchive(CustomUserDetails userDetails, Long itemId, Boolean isArchived) {
        SellerProfile seller = memberService.checkSeller(userDetails);
        Item item = itemRepository.findByIdAndSeller(itemId, seller)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));
        item.setIsArchived(isArchived);
        return ItemConverter.toResultDto(itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto.AccessDto getAccess(CustomUserDetails userDetails, Long itemId) {
        SellerProfile seller = memberService.checkSeller(userDetails);
        Item item = itemRepository.findByIdAndSeller(itemId, seller)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));
        return ItemConverter.toAccessDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public TalkBoxInfoPair getTalkBoxInfoPair(List<Item> itemList) {
        List<Long> itemIdList = itemList.stream().map(Item::getId).toList();

        Map<Long, Integer> waitingCntMap = new HashMap<>();
        Map<Long, Integer> completedCntMap = new HashMap<>();

        List<TalkBoxInfoPairDto> cntList = questionRepository.countByItemIdAndIsAnswered(itemIdList);

        for (TalkBoxInfoPairDto pair : cntList) {
            Long itemId = pair.getItemId();
            int cnt = pair.getCnt().intValue();

            if (Boolean.TRUE.equals(pair.getIsAnswered())) completedCntMap.put(itemId, cnt);
            else waitingCntMap.put(itemId, cnt);
        }

        return new TalkBoxInfoPair(waitingCntMap, completedCntMap);
    }
}
