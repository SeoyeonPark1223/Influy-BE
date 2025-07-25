package com.influy.domain.search.controller;

import com.influy.domain.item.dto.ItemResponseDto;
import com.influy.domain.search.dto.SearchResponseDto;
import com.influy.domain.search.service.SearchService;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.common.PageRequestDto;
import com.influy.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "검색", description = "검색 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchRestController {
    private final SearchService searchService;

    @GetMapping("/seller")
    @Operation(summary = "셀러 검색", description = "Request param으로 쿼리 내용을 String으로 주세요")
    public ApiResponse<SearchResponseDto.SellerPageResultDto> searchSeller(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @RequestParam("query") String query,
                                                                 @ParameterObject @Valid PageRequestDto pageRequest) {
        return ApiResponse.onSuccess(searchService.searchSeller(userDetails, query, pageRequest));
    }

    @GetMapping("/item")
    @Operation(summary = "아이템 검색", description = "Request param으로 쿼리 내용을 String으로 주세요")
    public ApiResponse<ItemResponseDto.HomeItemViewPageDto> searchItem(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                       @RequestParam("query") String query,
                                                                       @ParameterObject @Valid PageRequestDto pageRequest) {
        return ApiResponse.onSuccess(searchService.searchItem(userDetails, query, pageRequest));
    }
}
