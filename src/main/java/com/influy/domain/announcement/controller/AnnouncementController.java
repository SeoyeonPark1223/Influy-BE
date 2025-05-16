package com.influy.domain.announcement.controller;

import com.influy.domain.announcement.converter.AnnouncementConverter;
import com.influy.domain.announcement.dto.AnnouncementResponseDTO;
import com.influy.domain.announcement.entity.Announcement;
import com.influy.domain.announcement.service.AnnouncementService;
import com.influy.domain.seller.entity.Seller;
import com.influy.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/announcement")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    //공지 리스트 조회
    @GetMapping("/{sellerId}")//로그인 구현 후 id 제거
    public ApiResponse<Page<AnnouncementResponseDTO.General>> getAnnouncements(@PathVariable("sellerId") Long sellerId, @ParameterObject Pageable pageable) {

        Seller seller = new Seller(2L,"나",true,null,null,null,null,null,
                "string",null,null,null,null,null,null,null);
        Page<Announcement> announcements = announcementService.getAnnouncementsOf(seller,pageable);

        Page<AnnouncementResponseDTO.General> body = announcements.map(AnnouncementConverter::toGeneralDTO);

        return ApiResponse.onSuccess(body);
    }
}
