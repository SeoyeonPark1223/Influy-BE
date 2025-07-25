package com.influy.domain.search.service;

import com.influy.domain.item.dto.ItemResponseDto;
import com.influy.domain.search.dto.SearchResponseDto;
import com.influy.global.common.PageRequestDto;
import com.influy.global.jwt.CustomUserDetails;

public interface SearchService {
    SearchResponseDto.SellerPageResultDto searchSeller(CustomUserDetails userDetails, String query, PageRequestDto pageRequest);
    ItemResponseDto.HomeItemViewPageDto searchItem(CustomUserDetails userDetails, String query, PageRequestDto pageRequest);
}
