package com.influy.domain.admin.controller;

import com.influy.domain.admin.dto.AdminRequestDTO;
import com.influy.domain.admin.service.AdminService;
import com.influy.domain.item.converter.ItemConverter;
import com.influy.domain.item.dto.ItemResponseDto;
import com.influy.domain.item.entity.Item;
import com.influy.domain.managerProfile.entity.ManagerProfile;
import com.influy.domain.managerProfile.service.ManagerProfileService;
import com.influy.domain.member.dto.MemberRequestDTO;
import com.influy.domain.member.entity.Member;
import com.influy.domain.member.entity.MemberRole;
import com.influy.domain.member.service.MemberService;
import com.influy.domain.sellerProfile.dto.SellerProfileResponseDTO;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.auth.TokenPair;
import com.influy.global.auth.converter.AuthConverter;
import com.influy.global.auth.dto.AuthResponseDTO;
import com.influy.global.auth.service.AuthService;
import com.influy.global.jwt.CookieUtil;
import com.influy.global.jwt.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name="어드민")
public class AdminController {

    private final MemberService memberService;
    private final AdminService adminService;
    private final AuthService authService;
    private final ManagerProfileService managerService;



    @PostMapping("/register")
    @Operation(summary = "어드민으로 승급", description = "회원 아이디로 어드민 지정")
    public ApiResponse<List<AuthResponseDTO.SellerIdAndToken>> registerAdmin(@RequestBody List<Long> memberIds){
        return null;
    }

    //본인 아이템 인가
    @PostMapping("copy/items")
    @Operation(summary = "어드민이 본인 아이템 인가")
    public ApiResponse<List<ItemResponseDto.ResultDto>> copyItemToSeller(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                   @RequestBody AdminRequestDTO.CopyItemToSeller request){
        if(userDetails.getMember().getRole()!= MemberRole.ADMIN){
            throw new GeneralException(ErrorStatus.ADMIN_REQUIRED);
        }

        List<Item> newItems = adminService.copyItemToSeller(request.getItemIds(), request.getSellerId());

        List<ItemResponseDto.ResultDto> body  = newItems.stream().map(ItemConverter::toResultDto).toList();

        return ApiResponse.onSuccess(body);
    }
    //가짜 셀러 생성
    //다른 사람에게 셀러 위임
    //
}
