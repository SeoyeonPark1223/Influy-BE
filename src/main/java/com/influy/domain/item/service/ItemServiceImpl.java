package com.influy.domain.item.service;

import com.influy.domain.category.entity.Category;
import com.influy.domain.category.repository.CategoryRepository;
import com.influy.domain.image.converter.ImageConverter;
import com.influy.domain.image.entity.Image;
import com.influy.domain.item.converter.ItemConverter;
import com.influy.domain.item.dto.ItemRequestDto;
import com.influy.domain.item.dto.ItemResponseDto;
import com.influy.domain.item.dto.jpql.TalkBoxInfoPairDto;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.entity.ItemStatus;
import com.influy.domain.item.entity.TalkBoxInfoPair;
import com.influy.domain.item.entity.TalkBoxOpenStatus;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.itemCategory.converter.ItemCategoryConverter;
import com.influy.domain.itemCategory.entity.ItemCategory;
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
import org.hibernate.query.SortDirection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Override
    @Transactional
    public Item create(CustomUserDetails userDetails, ItemRequestDto.DetailDto request) {
        SellerProfile seller = memberService.checkSeller(userDetails);
        Item item = ItemConverter.toItem(seller, request);
        item = itemRepository.save(item);

        createImageList(request, item);
        createItemCategoryList(request, item);

        seller.getItemList().add(item);

        return item;
    }

    @Override
    @Transactional(readOnly = true)
    public Item getDetail(Long sellerId, Long itemId) {
        if (!sellerRepository.existsById(sellerId)) {
            throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        }

        return itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));
    }

    @Override
    @Transactional
    public void delete(CustomUserDetails userDetails, Long itemId) {
        SellerProfile seller = memberService.checkSeller(userDetails);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        seller.getItemList().remove(item);
        itemRepository.delete(item);
    }

    @Override
    @Transactional
    public Item update(CustomUserDetails userDetails, Long itemId, ItemRequestDto.DetailDto request) {
        memberService.checkSeller(userDetails);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        if (request.getName() != null) item.setName(request.getName());
        if (request.getStartDate() != null) item.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) item.setEndDate(request.getEndDate());
        if (request.getTagline() != null) item.setTagline(request.getTagline());
        if (request.getRegularPrice() != null) item.setRegularPrice(request.getRegularPrice());
        if (request.getSalePrice() != null) item.setSalePrice(request.getSalePrice());
        if (request.getMarketLink() != null) item.setMarketLink(request.getMarketLink());
        if (request.getItemPeriod() != null) item.setItemPeriod(request.getItemPeriod());
        if (request.getComment() != null) item.setComment(request.getComment());
        if (request.getIsArchived() != null) item.setIsArchived(request.getIsArchived());

        if (request.getItemImgList() != null) {
            item.getImageList().clear();
            createImageList(request, item);
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
        memberService.checkSeller(userDetails);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        if (request.getArchiveRecommended() != null) item.setArchiveRecommended(request.getArchiveRecommended());
        if (request.getSearchAvailable() != null) item.setSearchAvailable(request.getSearchAvailable());

        return item;
    }

    @Override
    @Transactional
    public Item setStatus(CustomUserDetails userDetails, Long itemId, ItemRequestDto.StatusDto request) {
        memberService.checkSeller(userDetails);

        Item item = itemRepository.findById(itemId)
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
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if (!sellerRepository.existsById(sellerId)) {
            throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        }

        MemberRole memberRole = MemberRole.SELLER;

        if (member.getRole() == MemberRole.USER) memberRole = MemberRole.USER;
        List<Long> likeItems = itemRepository.findLikedItemIdsByMember(member);

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
            itemPage = itemRepository.findOngoingItems(sellerId, LocalDateTime.now(), pageable);
        } else if (!isArchived & !isOnGoing) {
            // 보관 상품 아닌 것 중에서 진행 중 상품 필터 미적용
            itemPage = itemRepository.findBySellerIdAndIsArchivedFalse(sellerId, pageable);
        } else if (isArchived) {
            // 보관 상품
            itemPage = itemRepository.findBySellerIdAndIsArchivedTrue(sellerId, pageable);
        } else {
            throw new GeneralException(ErrorStatus.UNSUPPORTED_SORT_TYPE);
        }

        TalkBoxInfoPair talkBoxInfoPair = getTalkBoxInfoPair(itemPage.getContent());

        return ItemConverter.toDetailPreviewPageDto(itemPage, likeItems, memberRole, talkBoxInfoPair.waitingCntMap(), talkBoxInfoPair.completedCntMap());
    }

    private void createImageList(ItemRequestDto.DetailDto request, Item item) {
        List<String> imageLinkList = request.getItemImgList();
        for (int i = 0; i < imageLinkList.size(); i++) {
            boolean isMain = (i == 0);
            Image image = ImageConverter.toImage(item, imageLinkList.get(i), isMain);
            item.getImageList().add(image);
        }
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
        memberService.checkSeller(userDetails);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        if (openStatus == TalkBoxOpenStatus.INITIAL) throw new GeneralException((ErrorStatus.INVALID_TALKBOX_REQUEST));
        item.setTalkBoxOpenStatus(openStatus);

        return ItemConverter.toTalkBoxOpenStatusDto(itemId, openStatus);
    }

    @Override
    @Transactional
    public ItemResponseDto.ResultDto updateTalkBoxComment(CustomUserDetails userDetails, Long itemId, ItemRequestDto.TalkBoxCommentDto request) {
        memberService.checkSeller(userDetails);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        item.setTalkBoxComment(request.getTalkBoxComment());
        return ItemConverter.toResultDto(itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto.ViewTalkBoxCommentDto getTalkBoxComment(CustomUserDetails userDetails, Long itemId) {
        SellerProfile seller = memberService.checkSeller(userDetails);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));
        return ItemConverter.toViewTalkBoxCommentDto(seller, item);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto.TalkBoxOpenedListDto getTalkBoxOpened(CustomUserDetails userDetails) {
        SellerProfile seller = memberService.checkSeller(userDetails);
        List<Item> itemList = itemRepository.findAllBySellerIdAndTalkBoxOpenStatus(seller.getId());

        TalkBoxInfoPair talkBoxInfoPair = getTalkBoxInfoPair(itemList);

        // 아이템 기준 미확인 질문 개수
        Map<Long, Integer> uncheckedCntMap = itemList.stream()
                .collect(Collectors.toMap(
                        Item::getId,
                        item -> questionRepository.countQuestionsByItemIdAndIsChecked(item.getId(), false)
                ));

        return ItemConverter.toTalkBoxOpenedListDto(itemList, talkBoxInfoPair.waitingCntMap(), talkBoxInfoPair.completedCntMap(), uncheckedCntMap);
    }

    @Override
    @Transactional(readOnly = true)
    public Item findById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));
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

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto.HomeItemViewPageDto getCloseDeadline(CustomUserDetails userDetails, PageRequestDto pageRequest) {
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        if (member.getRole() == MemberRole.SELLER) throw new GeneralException(ErrorStatus.NOT_OWNER);

        // 남은 마감 시간이 24시간 이내 (SOLD_OUT & 마감일이 이미 지난 것 제외)
        LocalDateTime threshold = LocalDateTime.now().plusHours(24);
        Pageable pageable = pageRequest.toPageable(Sort.by(Sort.Direction.ASC, "endDate"));
        Page<Item> itemPage = itemRepository.findAllByEndDateAndItemStatus(LocalDateTime.now(), threshold, pageable);
        List<Long> likeItems = itemRepository.findLikedItemIdsByMember(member);

        return ItemConverter.toHomeItemViewPageDto(itemPage, likeItems);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto.HomeItemViewPageDto getPopular(CustomUserDetails userDetails, PageRequestDto pageRequest) {
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        if (member.getRole() == MemberRole.SELLER) throw new GeneralException(ErrorStatus.NOT_OWNER);

        // 질문 개수 top 3 (SOLD_OUT & 마감일이 이미 지난 것 제외)
        Pageable pageable = pageRequest.toPageable();
        Page<Item> itemPage = itemRepository.findTop3ByQuestionCnt(LocalDateTime.now(), pageable);
        List<Long> likeItems = itemRepository.findLikedItemIdsByMember(member);

        return ItemConverter.toHomeItemViewPageDto(itemPage, likeItems);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto.HomeItemViewPageDto getRecommended(CustomUserDetails userDetails, PageRequestDto pageRequest, Long categoryId) {
        Member member = memberRepository.findById(userDetails.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        if (member.getRole() == MemberRole.SELLER) throw new GeneralException(ErrorStatus.NOT_OWNER);

        Pageable pageable = pageRequest.toPageable();
        Page<Item> itemPage;
        if (categoryId != null) {
            if (!categoryRepository.existsById(categoryId)) throw new GeneralException(ErrorStatus.ITEM_CATEGORY_NOT_FOUND);
            itemPage = itemRepository.findAllByCategoryId(categoryId, pageable, LocalDateTime.now());
        } else {
            itemPage = itemRepository.findAllNow(pageable, LocalDateTime.now());
        }
        List<Long> likeItems = itemRepository.findLikedItemIdsByMember(member);

        return ItemConverter.toHomeItemViewPageDto(itemPage, likeItems);
    }
}
