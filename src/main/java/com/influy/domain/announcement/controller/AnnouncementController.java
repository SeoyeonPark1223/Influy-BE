package com.influy.domain.announcement.controller;

import com.influy.domain.announcement.converter.AnnouncementConverter;
import com.influy.domain.announcement.dto.AnnouncementRequestDTO;
import com.influy.domain.announcement.dto.AnnouncementResponseDTO;
import com.influy.domain.announcement.entity.Announcement;
import com.influy.domain.announcement.service.AnnouncementService;
import com.influy.domain.seller.entity.Seller;
import com.influy.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "공지")
@RequestMapping("/seller/announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    //공지 리스트 조회
    @GetMapping//로그인 구현 후 id 제거
    @Operation(summary = "공지 리스트 조회",description ="셀러가 본인의 공지 리스트 조회" )
    public ApiResponse<AnnouncementResponseDTO.GeneralList> getAnnouncements(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                             @ParameterObject @PageableDefault(sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Announcement> announcements = announcementService.getAnnouncementsOf(sellerId,pageable);

        Page<AnnouncementResponseDTO.General> dto = announcements.map(AnnouncementConverter::toGeneralDTO);
        AnnouncementResponseDTO.GeneralList body = AnnouncementResponseDTO.GeneralList.builder()
                .announcements(dto.getContent())
                .listSize(dto.getSize())
                .totalElements(dto.getTotalElements())
                .isFirst(dto.isFirst())
                .isLast(dto.isLast())
                .totalPage(dto.getTotalPages())
                .build();

        return ApiResponse.onSuccess(body);
    }

    //최상단 공지 조회
    @GetMapping("/primary-announcement")
    @Operation(summary = "최상단 공지 조회", description = "최상단 공지가 없으면 가장 최신 등록된 공지 반환")
    public ApiResponse<AnnouncementResponseDTO.General> getPrimaryAnnouncement(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId) {

        Announcement announcement = announcementService.getPrimaryAnnouncementOf(sellerId);
        AnnouncementResponseDTO.General body = AnnouncementConverter.toGeneralDTO(announcement);

        return ApiResponse.onSuccess(body);
    }

    //공지 추가
    @PostMapping //로그인 구현 후 id 제거
    @Operation(summary = "공지 추가",description ="셀러가 공지 추가" )
    public ApiResponse<AnnouncementResponseDTO.General> addAnnouncement(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                        @RequestBody AnnouncementRequestDTO requestDTO) {
        Announcement announcement = announcementService.addAnnouncementOf(sellerId,requestDTO);

        AnnouncementResponseDTO.General body = AnnouncementConverter.toGeneralDTO(announcement);
        return ApiResponse.onSuccess(body);
    }

    //공지 수정
    @PatchMapping("/{announcementId}") //로그인 구현 후 sellerId 제거
    @Operation(summary = "공지 수정",description ="셀러가 개별 공지 수정" )
    public ApiResponse<AnnouncementResponseDTO.General> updateAnnouncement(@PathVariable("announcementId") Long announcementId,
                                                                           @RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                           @RequestBody AnnouncementRequestDTO requestDTO) {

        Announcement announcement = announcementService.updateAnnouncement(announcementId, requestDTO, sellerId);
        AnnouncementResponseDTO.General body = AnnouncementConverter.toGeneralDTO(announcement);
        return ApiResponse.onSuccess(body);
    }


    //공지 삭제
    @DeleteMapping("/{announcementId}") //로그인 구현 후 sellerId 제거
    @Operation(summary = "공지 삭제",description ="셀러가 개별 공지 삭제" )
    public ApiResponse<String> deleteAnnouncement(@PathVariable("announcementId") Long announcementId,
                                                  @RequestParam(value="sellerId",defaultValue = "1") Long sellerId) {

        announcementService.deleteAnnouncement(sellerId, announcementId);

        return ApiResponse.onSuccess("삭제에 성공했습니다.");
    }

}
