package com.influy.domain.item.service;

import com.influy.domain.category.entity.Category;
import com.influy.domain.category.repository.CategoryRepository;
import com.influy.domain.image.converter.ImageConverter;
import com.influy.domain.image.entity.Image;
import com.influy.domain.item.converter.ItemConverter;
import com.influy.domain.item.dto.ItemRequestDto;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.itemCategory.converter.ItemCategoryConverter;
import com.influy.domain.itemCategory.entity.ItemCategory;
import com.influy.domain.sellerProfile.entity.ItemSortType;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.sellerProfile.repository.SellerProfileRepository;
import com.influy.domain.sellerProfile.service.SellerProfileServiceImpl;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.common.PageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final SellerProfileRepository sellerRepository;
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final SellerProfileServiceImpl sellerService;

    @Override
    @Transactional
    public Item createItem(Long sellerId, ItemRequestDto.DetailDto request) {
        SellerProfile seller = sellerService.getSeller(sellerId);

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
    public void deleteItem(Long sellerId, Long itemId) {
        SellerProfile seller = sellerService.getSeller(sellerId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        seller.getItemList().remove(item);
        itemRepository.delete(item);
    }

    @Override
    @Transactional
    public Item updateItem(Long sellerId, Long itemId, ItemRequestDto.DetailDto request) {
        if (!sellerRepository.existsById(sellerId)) {
            throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        }

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
    public Item setAccess(Long sellerId, Long itemId, ItemRequestDto.AccessDto request) {
        if (!sellerRepository.existsById(sellerId)) {
            throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        if (request.getArchiveRecommended() != null) item.setArchiveRecommended(request.getArchiveRecommended());
        if (request.getSearchAvailable() != null) item.setSearchAvailable(request.getSearchAvailable());

        return item;
    }

    @Override
    @Transactional
    public Item setStatus(Long sellerId, Long itemId, ItemRequestDto.StatusDto request) {
        if (!sellerRepository.existsById(sellerId)) {
            throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        }

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
    public Page<Item> getDetailPreviewPage(Long sellerId, Boolean isArchived, PageRequestDto pageRequest, ItemSortType sortType, Boolean isOnGoing) {
        if (!sellerRepository.existsById(sellerId)) {
            throw new GeneralException(ErrorStatus.SELLER_NOT_FOUND);
        }

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

        if (!isArchived & isOnGoing) {
            // 보관 상품 아닌 것 중에서 진행 중 상품 필터 적용
            return itemRepository.findOngoingItems(sellerId, LocalDateTime.now(), pageable);
        } else if (!isArchived & !isOnGoing) {
            // 보관 상품 아닌 것 중에서 진행 중 상품 필터 미적용
            return itemRepository.findBySellerIdAndIsArchivedFalse(sellerId, pageable);
        } else if (isArchived) {
            // 보관 상품
            return itemRepository.findBySellerIdAndIsArchivedTrue(sellerId, pageable);
        } else {
            throw new GeneralException(ErrorStatus.UNSUPPORTED_SORT_TYPE);
        }
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



}
