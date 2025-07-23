package com.influy.domain.like.controller;

import com.influy.domain.like.converter.LikeConverter;
import com.influy.domain.like.dto.LikeResponseDto;
import com.influy.domain.like.entity.Like;
import com.influy.domain.like.service.LikeService;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.common.PageRequestDto;
import com.influy.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "찜", description = "찜 관련 API")
@RestController
@RequiredArgsConstructor
public class LikeRestController {
    private final LikeService likeService;

    @PostMapping("seller/{sellerId}/likes")
    @Operation(summary = "멤버가 셀러에 찜 추가")
    public ApiResponse<LikeResponseDto.SellerLikeDto> addSellerLike(@PathVariable("sellerId") Long sellerId,
                                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        Like like = likeService.toAddSellerLike(sellerId, userDetails.getId());
        return ApiResponse.onSuccess(LikeConverter.toSellerLikeDto(like));
    }

    @PostMapping("seller/{sellerId}/items/{itemId}/likes")
    @Operation(summary = "멤버가 아이템에 찜 추가")
    public ApiResponse<LikeResponseDto.ItemLikeDto> addItemLike(@PathVariable("sellerId") Long sellerId,
                                                                @PathVariable("itemId") Long itemId,
                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Like like = likeService.toAddItemLike(sellerId, itemId, userDetails.getId());
        return ApiResponse.onSuccess(LikeConverter.toItemLikeDto(like));
    }

    @PatchMapping("seller/{sellerId}/dislikes")
    @Operation(summary = "멤버가 셀러 찜 취소")
    public ApiResponse<LikeResponseDto.SellerLikeDto> cancelSellerLike(@PathVariable("sellerId") Long sellerId,
                                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        Like like = likeService.toCancelSellerLike(sellerId, userDetails.getId());
        return ApiResponse.onSuccess(LikeConverter.toSellerLikeDto(like));
    }


    @PatchMapping("seller/{sellerId}/items/{itemId}/dislikes")
    @Operation(summary = "멤버가 아이템 찜 취소")
    public ApiResponse<LikeResponseDto.ItemLikeDto> cancelItemLike(@PathVariable("sellerId") Long sellerId,
                                                                   @PathVariable("itemId") Long itemId,
                                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        Like like = likeService.toCancelItemLike(sellerId, itemId, userDetails.getId());
        return ApiResponse.onSuccess(LikeConverter.toItemLikeDto(like));
    }

    @GetMapping("seller/{sellerId}/count-likes")
    @Operation(summary = "셀러 찜 개수 조회")
    public ApiResponse<LikeResponseDto.LikeCountDto> countSellerLikes(@PathVariable("sellerId") Long sellerId) {
        return ApiResponse.onSuccess(likeService.toCountSellerLikes(sellerId));
    }

    @GetMapping("seller/{sellerId}/items/{itemId}/count-likes")
    @Operation(summary = "아이템 찜 개수 조회")
    public ApiResponse<LikeResponseDto.LikeCountDto> countItemLikes(@PathVariable("sellerId") Long sellerId,
                                                                    @PathVariable("itemId") Long itemId) {
        return ApiResponse.onSuccess(likeService.toCountItemLikes(sellerId, itemId));
    }

    @GetMapping("home/seller-likes")
    @Operation(summary = "멤버의 셀러 찜 리스트 조회")
    public ApiResponse<LikeResponseDto.SellerLikePageDto> getSellerLikePage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                            @Valid @ParameterObject PageRequestDto pageRequest) {
        Page<Like> likePage = likeService.toGetSellerLikePage(userDetails.getId(), pageRequest);
        return ApiResponse.onSuccess(LikeConverter.toSellerLikePageDto(likePage));
    }

    @GetMapping("home/item-likes")
    @Operation(summary = "멤버의 아이템 찜 리스트 조회")
    public ApiResponse<LikeResponseDto.ItemLikePageDto> getItemLikePage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                        @Valid @ParameterObject PageRequestDto pageRequest) {
        return ApiResponse.onSuccess(likeService.toGetItemLikePage(userDetails, pageRequest));
    }
}
