package com.influy.domain.announcement.controller;

import com.influy.domain.announcement.converter.AnnouncementConverter;
import com.influy.domain.announcement.dto.AnnouncementRequestDTO;
import com.influy.domain.announcement.dto.AnnouncementResponseDTO;
import com.influy.domain.announcement.entity.Announcement;
import com.influy.domain.announcement.service.AnnouncementService;
import com.influy.domain.announcement.service.AnnouncementServiceImpl;
import com.influy.global.apiPayload.ApiResponse;
import com.influy.global.common.PageRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "공지")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    //공지 리스트 조회
    @GetMapping("/seller/{sellerId}/announcements")
    @Operation(summary = "공지 리스트 조회",description ="셀러가 본인의 공지 리스트 조회" )
    public ApiResponse<AnnouncementResponseDTO.GeneralList> getAnnouncements(@PathVariable("sellerId") Long sellerId,
                                                                             @Valid @ParameterObject PageRequestDto pageable) {

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
    @GetMapping("/seller/{sellerId}/announcements/primary-announcement")
    @Operation(summary = "최상단 공지 조회", description = "최상단 공지가 없으면 가장 최신 등록된 공지 반환")
    public ApiResponse<AnnouncementResponseDTO.PinnedAnnouncement> getPrimaryAnnouncement(@PathVariable("sellerId") Long sellerId) {

        Optional<Announcement> announcement = announcementService.getPrimaryAnnouncementOf(sellerId);
        Integer totalAnnouncements = announcementService.getTotalAnnouncementsOf(sellerId);
        AnnouncementResponseDTO.PinnedAnnouncement body = AnnouncementConverter.toPinnedAnnouncementDTO(announcement,totalAnnouncements);

        return ApiResponse.onSuccess(body);
    }

    //공지 추가
    @PostMapping("/seller/announcements")
    @Operation(summary = "공지 추가",description ="셀러가 공지 추가" )
    public ApiResponse<AnnouncementResponseDTO.General> addAnnouncement(@RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                        @RequestBody AnnouncementRequestDTO requestDTO) {
        Announcement announcement = announcementService.addAnnouncementOf(sellerId,requestDTO);

        AnnouncementResponseDTO.General body = AnnouncementConverter.toGeneralDTO(announcement);
        return ApiResponse.onSuccess(body);
    }

    //공지 수정
    @PatchMapping("/seller/announcements/{announcementId}") //로그인 구현 후 sellerId 제거
    @Operation(summary = "공지 수정",description ="셀러가 개별 공지 수정" )
    public ApiResponse<AnnouncementResponseDTO.General> updateAnnouncement(@PathVariable("announcementId") Long announcementId,
                                                                           @RequestParam(value="sellerId",defaultValue = "1") Long sellerId,
                                                                           @RequestParam(value="isPrimary", required = false) Boolean isPrimary,
                                                                           @RequestBody(required = false) AnnouncementRequestDTO requestDTO) {

        Announcement announcement = announcementService.updateAnnouncement(announcementId, requestDTO, sellerId,isPrimary);
        AnnouncementResponseDTO.General body = AnnouncementConverter.toGeneralDTO(announcement);
        return ApiResponse.onSuccess(body);
    }


    //공지 삭제
    @DeleteMapping("/seller/announcements/{announcementId}") //로그인 구현 후 sellerId 제거
    @Operation(summary = "공지 삭제",description ="셀러가 개별 공지 삭제" )
    public ApiResponse<String> deleteAnnouncement(@PathVariable("announcementId") Long announcementId,
                                                  @RequestParam(value="sellerId",defaultValue = "1") Long sellerId) {

        announcementService.deleteAnnouncement(sellerId, announcementId);

        return ApiResponse.onSuccess("삭제에 성공했습니다.");
    }

}
