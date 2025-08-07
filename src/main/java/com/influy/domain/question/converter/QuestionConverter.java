package com.influy.domain.question.converter;

import com.influy.domain.answer.converter.AnswerConverter;
import com.influy.domain.answer.dto.jpql.AnswerJPQLResult;
import com.influy.domain.answer.dto.AnswerResponseDto;
import com.influy.domain.answer.entity.Answer;
import com.influy.domain.item.dto.jpql.ItemJPQLResponse;
import com.influy.domain.item.entity.Item;
import com.influy.domain.member.entity.Member;
import com.influy.domain.question.dto.QuestionResponseDTO;
import com.influy.domain.question.dto.jpql.QuestionJPQLResult;
import com.influy.domain.question.entity.Question;
import com.influy.domain.questionCategory.dto.jpql.CategoryJPQLResult;
import com.influy.domain.questionTag.entity.QuestionTag;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QuestionConverter {

    public static Question toQuestion(Item item, Member from, String content, QuestionTag questionTag){
        return Question.builder()
                .item(item)
                .member(from)
                .content(content)
                .questionTag(questionTag)
                .build();
    }

    public static QuestionResponseDTO.CreationResult toCreationResult(Question question,String categoryName){

        return QuestionResponseDTO.CreationResult.builder()
                .id(question.getId())
                .content(question.getContent())
                .categoryName(categoryName)
                .createdAt(question.getCreatedAt())
                .build();

    }

    public static QuestionResponseDTO.SellerViewQuestion toSellerViewDTO(QuestionJPQLResult.SellerViewQuestion question, Long nthQuestion) {
        return QuestionResponseDTO.SellerViewQuestion.builder()
                .questionId(question.getId())
                .memberId(question.getMemberId())
                .profileImg(question.getProfileImg())
                .nickname(question.getNickname())
                .content(question.getContent())
                .username(question.getUsername())
                .tagName(question.getTagName())
                .tagId(question.getTagId())
                .isNew(!question.getIsChecked())
                .nthQuestion(nthQuestion)
                .createdAt(question.getCreatedAt().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                .build();
    }



    public static QuestionResponseDTO.SellerViewPage toSellerViewPageDTO(Page<QuestionJPQLResult.SellerViewQuestion> questions, Map<Long, Long> countMap, Long newQuestions) {
        List<QuestionResponseDTO.SellerViewQuestion> questionDTOs = questions != null ?
                questions.getContent().stream().map(question -> toSellerViewDTO(question, countMap.get(question.getMemberId()))).toList()
                : Collections.emptyList();

        return QuestionResponseDTO.SellerViewPage.builder()
                .questions(questionDTOs)
                .newQuestionCnt(newQuestions)
                .totalPage(questions == null ? 0: questions.getTotalPages())
                .totalElements(questions == null ? 0: questions.getTotalElements())
                .isFirst(questions == null || questions.isFirst())
                .isLast(questions == null || questions.isLast())
                .listSize(questionDTOs.size())
                .build();
    }

    public static QuestionResponseDTO.UserViewQNA toUserViewDTO(AnswerJPQLResult.UserViewQNAInfo question, String username) {
        return QuestionResponseDTO.UserViewQuestion.builder()
                .type(question.getType())
                .id(question.getId())
                .categoryName(question.getCategoryName())
                .content(question.getContent())
                .createdAt(question.getCreatedAt())
                .username(username)
                .build();
    }

    public static QuestionResponseDTO.UserViewQNAPage toUserViewQNAPage(Page<AnswerJPQLResult.UserViewQNAInfo> userQNAList,
                                                                        String greeting, String username) {
        List<QuestionResponseDTO.UserViewQNA> content = new ArrayList<>(userQNAList != null ?
                userQNAList.getContent().stream().map(
                        qna -> {
                            if (qna.getType().equals("Q")) {
                                return toUserViewDTO(qna, username);
                            } else {
                                return AnswerConverter.toUserViewDTO(qna);
                            }
                        }
                ).toList()
                : Collections.emptyList());

        if(greeting!=null){
            AnswerResponseDto.UserViewGreeting message = AnswerConverter.toUserViewGreetingDTO(greeting);
            content.add(message);
        }

        return QuestionResponseDTO.UserViewQNAPage.builder()
                .chatList(content)
                .listSize(content.size())
                .totalPage(userQNAList == null ? 0: userQNAList.getTotalPages())
                .totalElements(userQNAList == null ? 0: userQNAList.getTotalElements())
                .isFirst(userQNAList == null || userQNAList.isFirst())
                .isLast(userQNAList == null || userQNAList.isLast())
                .build();
    }

    public static QuestionResponseDTO.IsAnsweredCntDTO toIsAnsweredCntDTO(CategoryJPQLResult.IsAnswered isAnsweredCnt) {

        return QuestionResponseDTO.IsAnsweredCntDTO.builder()
                .categoryName(isAnsweredCnt.getCategoryName())
                .waitingCnt(isAnsweredCnt.getWaitingCnt())
                .completedCnt(isAnsweredCnt.getCompletedCnt())
                .build();
    }

    public static QuestionResponseDTO.QnAListDto toQnAListDto(Question question, List<Answer> answerList, Long nth) {
        QuestionResponseDTO.QuestionViewDto questionDto = QuestionConverter.toQuestionViewDto(question, nth);
        AnswerResponseDto.AnswerViewListDto answerListDto = AnswerConverter.toAnswerViewListDto(answerList);

        return QuestionResponseDTO.QnAListDto.builder()
                .questionDto(questionDto)
                .answerListDto(answerListDto)
                .build();
    }

    private static QuestionResponseDTO.QuestionViewDto toQuestionViewDto(Question question, Long nth) {
        Member member = question.getMember();
        return QuestionResponseDTO.QuestionViewDto.builder()
                .questionId(question.getId())
                .memberId(member.getId())
                .username(member.getUsername())
                .content(question.getContent())
                .nthQuestion(nth)
                .createdAt(question.getCreatedAt())
                .tagName(question.getQuestionTag().getName())
                .tagId(question.getQuestionTag().getId())
                .profileImg(question.getMember().getProfileImg())
                .build();
    }

    public static QuestionResponseDTO.DeleteResultDto toDeleteResultDto(List<Long> questionList) {
        return QuestionResponseDTO.DeleteResultDto.builder()
                .questionIdList(questionList)
                .build();
    }

    public static QuestionResponseDTO.UserTalkBoxItemDTO toUserTalkBoxItemDTO(ItemJPQLResponse.ItemWithSellerInfo itemSeller,
                                                                              String lastChatContent,
                                                                              LocalDateTime lastChatTime,
                                                                              Integer uncheckedCnt){

        return QuestionResponseDTO.UserTalkBoxItemDTO.builder()
                .itemId(itemSeller.getItemId())
                .itemTitle(itemSeller.getItemTitle())
                .itemMainPic(itemSeller.getItemMainImg())
                .sellerId(itemSeller.getSellerId())
                .sellerNickname(itemSeller.getSellerNickname())
                .sellerProfilePic(itemSeller.getSellerProfileImg())
                .lastChatContent(lastChatContent)
                .lastChatTime(lastChatTime)
                .uncheckedCnt(uncheckedCnt)
                .build();
    }

    public static QuestionResponseDTO.UserTalkBoxItemPageDTO toUserTalkBoxItemPageDTO(Page<QuestionJPQLResult.ItemWithRecentChat> recentChatPage,
                                                                                      Map<Long,Integer> uncheckedCounts,
                                                                                      Map<Long, ItemJPQLResponse.ItemWithSellerInfo> itemSellerInfos) {

        List<QuestionResponseDTO.UserTalkBoxItemDTO> list = recentChatPage.getContent().stream()
                .map(recentChat -> {
                    Long itemId = recentChat.getItemId();
                    ItemJPQLResponse.ItemWithSellerInfo itemSeller = itemSellerInfos.get(itemId);
                    Integer uncheckedCnt = uncheckedCounts.get(itemId);

                    return toUserTalkBoxItemDTO(itemSeller,recentChat.getContent(),recentChat.getCreatedAt(),uncheckedCnt);
                }).toList();
        return QuestionResponseDTO.UserTalkBoxItemPageDTO.builder()
                .talkboxList(list)
                .isFirst(recentChatPage.isFirst())
                .isLast(recentChatPage.isLast())
                .listSize(list.size())
                .totalPage(recentChatPage.getTotalPages())
                .totalElements(recentChatPage.getTotalElements())
                .build();
    }
}
