package com.influy.domain.admin.service;

import com.influy.domain.admin.dto.AdminRequestDTO;
import com.influy.domain.category.entity.Category;
import com.influy.domain.category.repository.CategoryRepository;
import com.influy.domain.faqCard.converter.FaqCardConverter;
import com.influy.domain.faqCard.dto.jpql.FaqCardJPQLResult;
import com.influy.domain.faqCard.entity.FaqCard;
import com.influy.domain.faqCard.repository.FaqCardRepository;
import com.influy.domain.faqCategory.converter.FaqCategoryConverter;
import com.influy.domain.faqCategory.entity.FaqCategory;
import com.influy.domain.faqCategory.repository.FaqCategoryRepository;
import com.influy.domain.image.service.ImageService;
import com.influy.domain.item.converter.ItemConverter;
import com.influy.domain.item.dto.ItemRequestDto;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.item.service.ItemService;
import com.influy.domain.item.service.ItemServiceImpl;
import com.influy.domain.itemCategory.converter.ItemCategoryConverter;
import com.influy.domain.itemCategory.entity.ItemCategory;
import com.influy.domain.managerProfile.entity.ManagerProfile;
import com.influy.domain.member.converter.MemberConverter;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.entity.MemberRole;
import com.influy.domain.member.service.MemberService;
import com.influy.domain.sellerProfile.converter.SellerProfileConverter;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import com.influy.domain.sellerProfile.repository.SellerProfileRepository;
import com.influy.domain.sellerProfile.service.SellerProfileService;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final MemberService memberService;
    private final SellerProfileService sellerProfileService;
    private final SellerProfileRepository sellerProfileRepository;
    private final ItemRepository itemRepository;
    private final ImageService imageService;
    private final CategoryRepository categoryRepository;
    private final FaqCategoryRepository faqCategoryRepository;
    private final FaqCardRepository faqCardRepository;

    @Override
    @Transactional
    public Member joinAdmin(MemberRequestDTO.SellerJoin request) {
        if(sellerProfileRepository.existsByEmail(request.getEmail())){
            throw new GeneralException(ErrorStatus.EMAIL_ALREADY_EXISTS);
        }
        if(sellerProfileRepository.existsByInstagram(request.getInstagram())){
            throw new GeneralException(ErrorStatus.INSTAGRAM_ALREADY_EXISTS);
        }
        //어드민으로 가입
        Member member = memberService.joinUser(request.getUserInfo(), MemberRole.ADMIN);

        SellerProfile sellerProfile = sellerProfileService.createSellerProfile(member,request);
        member.setSellerProfile(sellerProfile);

        return member;
    }

    @Override
    @Transactional
    public List<Item> copyItemToSeller(List<Long> itemIds, Long sellerId) {
        List<Item> items = itemRepository.findAllById(itemIds);

        //길이가 다르면 없는 상품 존재하는 것
        if(items.size()!=itemIds.size()){
            throw new GeneralException(ErrorStatus.ITEM_NOT_FOUND);
        }
        SellerProfile seller = sellerProfileRepository.findById(sellerId).orElseThrow(() -> new GeneralException(ErrorStatus.SELLER_NOT_FOUND));

        List<Item> newItems = new ArrayList<>();
        for(Item item:items){
            //내용 복사하여 새 아이템 생성
            Item newItem = ItemConverter.duplicateItem(seller,item);

            //이미지 복제해서 저장
            for(String imageURL : item.getImageList()){
                String newImageURL = imageService.duplicateImg(imageURL,seller.getMember().getId());
                newItem.getImageList().add(newImageURL);
            }
            if(!newItem.getImageList().isEmpty()) newItem.setMainImg(newItem.getImageList().getFirst());

            //카테고리 관계 설정
            List<Category> categoryList = categoryRepository.findAllByItemId(item.getId());
            List<ItemCategory> newItemCategoryList = categoryList.stream().map(category->ItemCategoryConverter.toItemCategory(category,newItem)).toList();
            newItem.getItemCategoryList().addAll(newItemCategoryList);

            //faq 카드 설정
            // 1. 기존 FAQ 카테고리 가져오기
            List<FaqCategory> faqCategoryList = faqCategoryRepository.findAllByItem(item);

            // 2. 기존 FAQ 카테고리 ID 리스트 추출
            List<Long> faqCategoryIds = faqCategoryList.stream()
                    .map(FaqCategory::getId)
                    .collect(Collectors.toList());

            // 3. 해당 카테고리 ID에 속한 카드들 가져오기
            List<FaqCardJPQLResult.WithCategoryId> faqCardList = faqCardRepository.findAllByFaqCategoryIds(faqCategoryIds);

            // 4. 카테고리 ID로 카드들 그룹핑
            Map<Long, List<FaqCard>> cardMap = faqCardList.stream()
                    .collect(Collectors.groupingBy(
                            FaqCardJPQLResult.WithCategoryId::getFaqCategoryId,
                            Collectors.mapping(FaqCardJPQLResult.WithCategoryId::getFaqCard, Collectors.toList())
                    ));

            // 5. 새로 복제된 카테고리들을 저장할 리스트
            List<FaqCategory> newFaqCategories = new ArrayList<>();

            // 6. 카테고리 복제 + 카드 복제 + 연관관계 설정
            for (FaqCategory oldCategory : faqCategoryList) {
                // 카테고리 복제 (newItem과 연관 설정 포함)
                FaqCategory newCategory = FaqCategoryConverter.duplicate(oldCategory,newItem);

                // 관련 카드 가져오기
                List<FaqCard> cards = cardMap.getOrDefault(oldCategory.getId(), Collections.emptyList());

                // 카드 복제 및 카테고리 연관 설정
                List<FaqCard> newCards = cards.stream()
                        .map(card -> {
                            String newBgImg = "";
                            if (!Objects.equals(card.getBackgroundImageLink(), "")) {
                                newBgImg = imageService.duplicateImg(card.getBackgroundImageLink(), seller.getMember().getId());
                            }
                            return FaqCardConverter.duplicate(card,newCategory, newItem.getSeller(), newBgImg);
                        })
                        .toList();

                newCategory.getFaqCardList().addAll(newCards); // 양방향 연관 설정

                newFaqCategories.add(newCategory);
            }

            // 7. 새 카테고리들 아이템에 연결(양방향)
            newItem.getFaqCategoryList().addAll(newFaqCategories);

            newItems.add(itemRepository.save(newItem));
            seller.getItemList().add(newItem);
        }

        return newItems;
    }
}
