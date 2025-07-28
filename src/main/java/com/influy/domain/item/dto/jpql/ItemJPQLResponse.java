package com.influy.domain.item.dto.jpql;

import com.influy.domain.item.entity.Item;
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
}
