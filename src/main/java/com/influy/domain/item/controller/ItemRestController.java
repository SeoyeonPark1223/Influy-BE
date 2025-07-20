package com.influy.domain.item.controller;

import com.influy.domain.item.converter.ItemConverter;
import com.influy.domain.item.dto.ItemRequestDto;
import com.influy.domain.item.dto.ItemResponseDto;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.service.ItemService;
import com.influy.domain.sellerProfile.entity.ItemSortType;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.common.PageRequestDto;
import com.influy.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "셀러 아이템", description = "셀러 아이템 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class ItemRestController {
    private final ItemService itemService;

    @PostMapping("/items")
    @Operation(summary = "셀러 상품 상세정보 작성 후 생성")
    public ApiResponse<ItemResponseDto.ResultDto> create(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @RequestBody @Valid ItemRequestDto.DetailDto request) {
        Item item = itemService.create(userDetails, request);
        return ApiResponse.onSuccess(ItemConverter.toResultDto(item));
    }

    @GetMapping("/{sellerId}/items")
    @Operation(summary = "셀러의 상품 상세정보 프리뷰 리스트 조회 (공개/아카이브/진행중 여부 선택 가능)", description = "memberId는 이후 AuthenticationPrincipal로 받을 것")
    public ApiResponse<ItemResponseDto.DetailPreviewPageDto> getDetailPreviewPage(@PathVariable("sellerId") Long sellerId,
                                                                                  @RequestParam(name = "archive", defaultValue = "false") Boolean isArchived,
                                                                                  @Valid @ParameterObject PageRequestDto pageRequest,
                                                                                  @RequestParam(name = "sortType", defaultValue = "END_DATE") ItemSortType sortType,
                                                                                  @RequestParam(name = "onGoing", defaultValue = "false") Boolean isOnGoing,
                                                                                  @RequestParam(value = "memberId", required = false) Long memberId) {

        return ApiResponse.onSuccess(itemService.getDetailPreviewPage(sellerId, isArchived, pageRequest, sortType, isOnGoing, memberId));
    }

    @GetMapping("/{sellerId}/items/{itemId}")
    @Operation(summary = "개별 상품 상세정보 조회")
    public ApiResponse<ItemResponseDto.DetailViewDto> getDetail(@PathVariable("sellerId") Long sellerId,
                                                                @PathVariable("itemId") Long itemId) {
        Item item = itemService.getDetail(sellerId, itemId);
        return ApiResponse.onSuccess(ItemConverter.toDetailViewDto(item));
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "개별 상품 삭제")
    public ApiResponse<ItemResponseDto.ResultDto> delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @PathVariable("itemId") Long itemId) {
        itemService.delete(userDetails, itemId);
        return ApiResponse.onSuccess(ItemConverter.toResultDto(itemId));
    }

    @PutMapping("/items/{itemId}")
    @Operation(summary = "개별 상품 상세정보 수정")
    public ApiResponse<ItemResponseDto.DetailViewDto> update(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @PathVariable("itemId") Long itemId,
                                                                 @RequestBody @Valid ItemRequestDto.DetailDto request) {
        Item item = itemService.update(userDetails, itemId, request);
        return ApiResponse.onSuccess(ItemConverter.toDetailViewDto(item));
    }

    @PatchMapping("/items/{itemId}/access")
    @Operation(summary = "개별 상품 공개 범위 설정")
    public ApiResponse<ItemResponseDto.ResultDto> setAccess(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @PathVariable("itemId") Long itemId,
                                                            @RequestBody @Valid ItemRequestDto.AccessDto request) {
        Item item = itemService.setAccess(userDetails, itemId, request);
        return ApiResponse.onSuccess(ItemConverter.toResultDto(item));
    }

    @PatchMapping("/items/{itemId}/status")
    @Operation(summary = "개별 상품 표기 상태 설정 | DEFAULT, EXTEND, SOLD_OUT")
    public ApiResponse<ItemResponseDto.ResultDto> setStatus(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @PathVariable("itemId") Long itemId,
                                                            @RequestBody @Valid ItemRequestDto.StatusDto request) {
        Item item = itemService.setStatus(userDetails, itemId, request);
        return ApiResponse.onSuccess(ItemConverter.toResultDto(item));
    }

    @GetMapping("/{sellerId}/items/count-access")
    @Operation(summary = "상품 공개/보관 개수 조회")
    public ApiResponse<ItemResponseDto.CountDto> getCount(@PathVariable("sellerId") Long sellerId,
                                                          @RequestParam(name = "isArchived", defaultValue = "false") Boolean isArchived) {
        Integer count = itemService.getCount(sellerId, isArchived);
        return ApiResponse.onSuccess(ItemConverter.toCountDto(sellerId, count));
    }
}
