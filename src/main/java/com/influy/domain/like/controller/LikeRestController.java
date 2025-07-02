package com.influy.domain.like.controller;

import com.influy.domain.like.converter.LikeConverter;
import com.influy.domain.like.dto.LikeResponseDto;
import com.influy.domain.like.entity.Like;
import com.influy.domain.like.service.LikeService;
import com.influy.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "찜", description = "찜 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("seller/{sellerId}")
public class LikeRestController {
    private final LikeService likeService;

    @PostMapping("/likes")
    @Operation(summary = "멤버가 셀러에 찜 추가")
    public ApiResponse<LikeResponseDto.AddSellerLikeDto> AddSellerLike(@PathVariable("sellerId") Long sellerId,
                                                                             @RequestParam(value="memberId", defaultValue = "1") Long memberId) {
        Like like = likeService.toAddSellerLike(sellerId, memberId);
        return ApiResponse.onSuccess(LikeConverter.toAddSellerLikeDto(like));
    }

    @PostMapping("/items/{itemId}/likes")
    @Operation(summary = "멤버가 아이템에 찜 추가")
    public ApiResponse<LikeResponseDto.AddItemLikeDto> AddItemLike(@PathVariable("sellerId") Long sellerId,
                                                                         @PathVariable("itemId") Long itemId,
                                                                         @RequestParam(value="memberId", defaultValue = "1") Long memberId) {
        Like like = likeService.toAddItemLike(sellerId, itemId, memberId);
        return ApiResponse.onSuccess(LikeConverter.toAddItemLikeDto(like));
    }
}
