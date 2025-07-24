package com.influy.domain.search.service;

import com.influy.domain.search.dto.SearchRequestDto;
import com.influy.domain.search.dto.SearchResponseDto;
import com.influy.global.common.PageRequestDto;
import com.influy.global.jwt.CustomUserDetails;

public interface SearchService {
    SearchResponseDto.SearchResultDto search(CustomUserDetails userDetails, String query, PageRequestDto sellerPageRequest, PageRequestDto itemPageRequest);
}
