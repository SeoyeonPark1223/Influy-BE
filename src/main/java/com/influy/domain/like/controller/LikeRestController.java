package com.influy.domain.like.controller;

import com.influy.domain.like.converter.LikeConverter;
import com.influy.domain.like.dto.LikeResponseDto;
import com.influy.domain.like.entity.Like;
import com.influy.domain.like.service.LikeService;
import com.influy.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ApiResponse<LikeResponseDto.SellerLikeDto> addSellerLike(@PathVariable("sellerId") Long sellerId,
                                                                    @RequestParam(value="memberId", defaultValue = "1") Long memberId) {
        Like like = likeService.toAddSellerLike(sellerId, memberId);
        return ApiResponse.onSuccess(LikeConverter.toSellerLikeDto(like));
    }

    @PostMapping("/items/{itemId}/likes")
    @Operation(summary = "멤버가 아이템에 찜 추가")
    public ApiResponse<LikeResponseDto.ItemLikeDto> addItemLike(@PathVariable("sellerId") Long sellerId,
                                                                @PathVariable("itemId") Long itemId,
                                                                @RequestParam(value="memberId", defaultValue = "1") Long memberId) {
        Like like = likeService.toAddItemLike(sellerId, itemId, memberId);
        return ApiResponse.onSuccess(LikeConverter.toItemLikeDto(like));
    }

    @PatchMapping("/dislikes")
    @Operation(summary = "멤버가 셀러 찜 취소")
    public ApiResponse<LikeResponseDto.SellerLikeDto> cancelSellerLike(@PathVariable("sellerId") Long sellerId,
                                                                       @RequestParam(value="memberId", defaultValue = "1") Long memberId) {
        Like like = likeService.toCancelSellerLike(sellerId, memberId);
        return ApiResponse.onSuccess(LikeConverter.toSellerLikeDto(like));
    }


    @PatchMapping("/items/{itemId}/dislikes")
    @Operation(summary = "멤버가 아이템 찜 취소")
    public ApiResponse<LikeResponseDto.ItemLikeDto> cancelItemLike(@PathVariable("sellerId") Long sellerId,
                                                                   @PathVariable("itemId") Long itemId,
                                                                   @RequestParam(value="memberId", defaultValue = "1") Long memberId) {
        Like like = likeService.toCancelItemLike(sellerId, itemId, memberId);
        return ApiResponse.onSuccess(LikeConverter.toItemLikeDto(like));
    }

    @GetMapping("/count-likes")
    @Operation(summary = "멤버의 셀러 찜 개수 조회")
    public ApiResponse<LikeResponseDto.LikeCountDto> countSellerLikes(@PathVariable("sellerId") Long sellerId) {
        return ApiResponse.onSuccess(likeService.toCountSellerLikes(sellerId));
    }

    @GetMapping("/items/{itemId}/count-likes")
    @Operation(summary = "멤버의 아이템 찜 개수 조회")
    public ApiResponse<LikeResponseDto.LikeCountDto> countItemLikes(@PathVariable("sellerId") Long sellerId,
                                                                    @PathVariable("itemId") Long itemId) {
        return ApiResponse.onSuccess(likeService.toCountItemLikes(sellerId, itemId));
    }


}
