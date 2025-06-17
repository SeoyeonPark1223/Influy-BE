package com.influy.domain.item.controller;

import com.influy.domain.item.converter.ItemConverter;
import com.influy.domain.item.dto.ItemRequestDto;
import com.influy.domain.item.dto.ItemResponseDto;
import com.influy.domain.item.entity.Item;
import com.influy.domain.item.service.ItemService;
import com.influy.domain.seller.entity.ItemSortType;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.common.PageRequestDto;
import com.influy.global.validation.annotation.CheckPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "셀러 아이템", description = "셀러 아이템 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/items")
public class ItemRestController {
    private final ItemService itemService;

    @PostMapping
    @Operation(summary = "셀러 상품 상세정보 작성 후 생성")
    public ApiResponse<ItemResponseDto.ResultDto> createItem(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                             @RequestBody @Valid ItemRequestDto.DetailDto request) {
        Item item = itemService.createItem(sellerId, request);
        return ApiResponse.onSuccess(ItemConverter.toResultDto(item));
    }

    @GetMapping
    @Operation(summary = "셀러의 상품 상세정보 프리뷰 리스트 조회 (공개/아카이브 여부 선택 가능)")
    public ApiResponse<ItemResponseDto.DetailPreviewPageDto> getDetailPreviewPage(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                                  @RequestParam(name = "archive", defaultValue = "false") Boolean isArchived,
                                                                                  @Valid @ParameterObject PageRequestDto pageRequest,
                                                                                  @RequestParam(name = "sortType", defaultValue = "END_DATE") ItemSortType sortType) {
        Page<Item> itemPage = itemService.getDetailPreviewPage(sellerId, isArchived, pageRequest, sortType);
        return ApiResponse.onSuccess(ItemConverter.toDetailPreviewPageDto(itemPage));
    }

    @GetMapping("/{itemId}")
    @Operation(summary = "개별 상품 상세정보 조회")
    public ApiResponse<ItemResponseDto.DetailViewDto> getDetail(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                @PathVariable("itemId") Long itemId) {
        Item item = itemService.getDetail(sellerId, itemId);
        return ApiResponse.onSuccess(ItemConverter.toDetailViewDto(item));
    }

    @DeleteMapping("/{itemId}")
    @Operation(summary = "개별 상품 삭제")
    public ApiResponse<ItemResponseDto.ResultDto> deleteItem(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                             @PathVariable("itemId") Long itemId) {
        itemService.deleteItem(sellerId, itemId);
        return ApiResponse.onSuccess(ItemConverter.toResultDto(itemId));
    }

    @PutMapping("/{itemId}")
    @Operation(summary = "개별 상품 상세정보 수정")
    public ApiResponse<ItemResponseDto.DetailViewDto> updateItem(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                 @PathVariable("itemId") Long itemId,
                                                                 @RequestBody @Valid ItemRequestDto.DetailDto request) {
        Item item = itemService.updateItem(sellerId, itemId, request);
        return ApiResponse.onSuccess(ItemConverter.toDetailViewDto(item));
    }

    @PatchMapping("/{itemId}/access")
    @Operation(summary = "개별 상품 공개 범위 설정")
    public ApiResponse<ItemResponseDto.ResultDto> setAccess(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                            @PathVariable("itemId") Long itemId,
                                                            @RequestBody @Valid ItemRequestDto.AccessDto request) {
        Item item = itemService.setAccess(sellerId, itemId, request);
        return ApiResponse.onSuccess(ItemConverter.toResultDto(item));
    }

    @PatchMapping("/{itemId}/status")
    @Operation(summary = "개별 상품 표기 상태 설정 | DEFAULT, EXTEND, SOLD_OUT")
    public ApiResponse<ItemResponseDto.ResultDto> setStatus(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                            @PathVariable("itemId") Long itemId,
                                                            @RequestBody @Valid ItemRequestDto.StatusDto request) {
        Item item = itemService.setStatus(sellerId, itemId, request);
        return ApiResponse.onSuccess(ItemConverter.toResultDto(item));
    }

    @GetMapping("/count")
    @Operation(summary = "상품 공개/보관 개수 조회")
    public ApiResponse<ItemResponseDto.CountDto> getCount(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                          @RequestParam(name = "isArchived", defaultValue = "false") Boolean isArchived) {
        Integer count = itemService.getCount(sellerId, isArchived);
        return ApiResponse.onSuccess(ItemConverter.toCountDto(sellerId, count));
    }
}
