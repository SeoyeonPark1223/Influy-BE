package com.influy.domain.item.dto.jpql;

import com.influy.domain.item.entity.Item;
import com.influy.domain.member.entity.Member;
import com.influy.domain.sellerProfile.entity.SellerProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ItemJPQLResponse {
    
    public interface IsArchivedItemCount {
        Boolean getIsArchived();
        Long getCount();
    }

    public interface ItemWithQuestionStatus {
        Item getItem();
        Long getNewQuestions();
        Long getPendingQuestions();
    }

    public interface  ItemWithSellerInfo{
        Long getItemId();
        String getItemTitle();
        String getItemMainImg();
        String getSellerNickname();
        String getSellerProfileImg();

    }
}
