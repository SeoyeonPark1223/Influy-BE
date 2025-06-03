package com.influy.domain.questionCategory.service;

import com.influy.domain.item.entity.Item;
import com.influy.domain.item.repository.ItemRepository;
import com.influy.domain.question.converter.QuestionConverter;
import com.influy.domain.question.dto.JPQLQuestionDTO;
import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.repository.QuestionRepository;
import com.influy.domain.questionCategory.converter.QuestionCategoryConverter;
import com.influy.domain.questionCategory.dto.QuestionCategoryRequestDTO;
import com.influy.domain.questionCategory.dto.QuestionCategoryResponseDTO;
import com.influy.domain.questionCategory.entity.QuestionCategory;
import com.influy.domain.questionCategory.repository.QuestionCategoryRepository;
import com.influy.domain.seller.entity.Seller;
import com.influy.domain.seller.service.SellerService;
import com.influy.domain.user.repository.UserRepository;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionCategoryServiceIml implements QuestionCategoryService{

    private final SellerService sellerService;
    private final ItemRepository itemRepository;
    private final QuestionCategoryRepository questionCategoryRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public QuestionCategory createCategory(Long sellerId, Long itemId, QuestionCategoryRequestDTO.Create request) {
        //자격 검증
        Seller seller = sellerService.getSeller(sellerId);
        Item item = itemRepository.findById(itemId).orElseThrow(()->new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        if(!item.getSeller().equals(seller)) {
            throw new GeneralException(ErrorStatus.FORBIDDEN);
        }

        //생성 로직
        QuestionCategory category = QuestionCategoryConverter.toEntity(item, request);

        return questionCategoryRepository.save(category);

        //item.getQuestionCategoryList().add(category)

    }

    @Override
    public Page<QuestionCategory> getCategoryList(Long sellerId, Long itemId, Pageable pageable) {
        //자격 검증
        Seller seller = sellerService.getSeller(sellerId);
        Item item = itemRepository.findById(itemId).orElseThrow(()->new GeneralException(ErrorStatus.ITEM_NOT_FOUND));

        if(!item.getSeller().equals(seller)) {
            throw new GeneralException(ErrorStatus.FORBIDDEN);
        }

        return questionCategoryRepository.findAllByItem(item,pageable);
    }

    @Override
    public List<QuestionCategoryResponseDTO.Preview> getPreviewDTO(Page<QuestionCategory> categories,
                                                                   Long itemId) {

        /**
         * step 1: 하나의 쿼리로 카테고리 정보(id, 이름, answered 수, pending 수) 생성
         */
        //포함 항목: isAnswered와 categoryId로 group by 해서 isAnswered, categoryId, count, categoryName.
        //answered 수와 pending 수를 함께 반환할 수 없으므로 카테고리 하나당 answered true인 객체 하나와 false인 객체 하나씩으로 반환됨
        List<JPQLQuestionDTO> queryResult = questionRepository
                .countQuestionsGroupedByCategoryAndAnswerStatus(itemId);

        //카테고리 당 객체 두개를 하나로 합치기 위한 HashMap. CategoryId 기준으로 저장
        Map<Long, QuestionCategoryResponseDTO.Preview> categoryMap = new HashMap<>();

        //JPQL 결과를 돌면서, 카테고리 id 별로 dto map 생성
        for(JPQLQuestionDTO result : queryResult) {

            categoryMap.putIfAbsent(
                    result.getCategoryId(),
                    QuestionCategoryConverter.toPreviewDTO(result)
            );

            //------여기서부터는 모든 categoryId는 Map에 무조건 존재----
            // 해시맵에서 현재 카테고리 id에 해당하는 dto 찾아서 pending, answered 개수 세팅
            if(result.getIsAnswered()!=null){
                QuestionCategoryResponseDTO.Preview previewDTO = categoryMap.get(result.getCategoryId());
                previewDTO.setCount(result.getIsAnswered(),result.getCount());
            }
        }

        /**
         * step 2: 각 카테고리 별 최신 질문(답변 완료 제외) 두개 조회해서 dto 에 추가
         */
        List<Object[]> rows = questionRepository.get2QuestionsInCategory(itemId);
        //포함 항목: 순서대로 질문 id, 질문자 id, 질문 내용, 생성일, 몇회차에 질문했는지, 카테고리 id
        for(Object[] row : rows) {

            Long categoryId = (Long) row[5]; //카테고리 id는 map 에서 dto 찾기위한 용도 외에는 사용되지 않음

            if(row[0]!=null){//질문이 존재하면 dto의 questionList에 추가해준다.
                QuestionCategoryResponseDTO.Preview dto = categoryMap.get(categoryId);

                String nickname = userRepository.findById((Long) row[1]).orElseThrow(()->new GeneralException(ErrorStatus.SELLER_NOT_FOUND)).getNickname();

                dto.getQuestions().add(QuestionConverter.toGeneralDTO(row,nickname));
            }

        }


        return categoryMap.values().stream().toList();
    }

}
