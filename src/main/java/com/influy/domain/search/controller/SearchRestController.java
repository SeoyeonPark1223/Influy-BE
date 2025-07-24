package com.influy.domain.search.controller;

import com.influy.domain.search.dto.SearchResponseDto;
import com.influy.domain.search.service.SearchService;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.common.PageRequestDto;
import com.influy.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "검색", description = "검색 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchRestController {
    private final SearchService searchService;

    @GetMapping()
    @Operation(summary = "검색", description = "Request param으로 쿼리 내용을 String으로 주세요")
    public ApiResponse<SearchResponseDto.SearchResultDto> search(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @RequestParam("query") String query,
                                                                 @ModelAttribute("sellerPageRequest") @Valid PageRequestDto sellerPageRequest,
                                                                 @ModelAttribute("itemPageRequest") @Valid PageRequestDto itemPageRequest) {
        return ApiResponse.onSuccess(searchService.search(userDetails, query, sellerPageRequest, itemPageRequest));
    }
}
