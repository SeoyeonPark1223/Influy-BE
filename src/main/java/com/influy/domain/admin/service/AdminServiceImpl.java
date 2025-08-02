package com.influy.domain.admin.service;

import com.influy.domain.admin.dto.AdminRequestDTO;
import com.influy.domain.category.entity.Category;
import com.influy.domain.category.repository.CategoryRepository;
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

import java.util.ArrayList;
import java.util.List;

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
                String newImageURL = imageService.duplicateImg(imageURL);
                newItem.getImageList().add(newImageURL);
            }

            //카테고리 관계 설정
            List<Category> categoryList = categoryRepository.findAllByItemId(item.getId());
            List<ItemCategory> newItemCategoryList = categoryList.stream().map(category->ItemCategoryConverter.toItemCategory(category,newItem)).toList();
            newItem.getItemCategoryList().addAll(newItemCategoryList);

            newItems.add(itemRepository.save(newItem));
            seller.getItemList().add(newItem);
        }

        return newItems;
    }
}
