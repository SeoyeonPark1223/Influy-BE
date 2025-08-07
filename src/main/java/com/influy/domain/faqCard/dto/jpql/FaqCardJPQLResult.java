package com.influy.domain.faqCard.dto.jpql;

import com.influy.domain.faqCard.entity.FaqCard;

public class FaqCardJPQLResult {
    public interface WithCategoryId{
        Long getFaqCategoryId();
        FaqCard getFaqCard();
    }
}
