package com.influy.domain.search.controller;

import com.influy.domain.search.dto.SearchRequestDto;
import com.influy.domain.search.dto.SearchResponseDto;
import com.influy.domain.search.service.SearchService;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.common.PageRequestDto;
import com.influy.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "검색", description = "검색 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchRestController {
    private final SearchService searchService;

    public ApiResponse<SearchResponseDto.SearchResultDto> search(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @RequestBody @Valid SearchRequestDto.SearchDto,
                                                                 @Valid @ParameterObject PageRequestDto pageRequest)
}
