package com.influy.domain.image.converter;

import com.influy.domain.image.dto.ImageResponseDto;
import com.influy.domain.image.entity.Image;
import com.influy.domain.item.entity.Item;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.time.Duration;

public class ImageConverter {
    public static Image toImage(Item item, String imgLink, Boolean isMainImg) {
        return Image.builder()
                .item(item)
                .imageLink(imgLink)
                .isMainImg(isMainImg)
                .build();
    }

    public static ImageResponseDto.UploadResultDto toUploadResultDto(URL imgURL) {
        return ImageResponseDto.UploadResultDto.builder()
                .imgUrl(imgURL.toString())
                .build();
    }

    public static PutObjectRequest toPutObjectRequest(String bucket, String fullPath, String extension) {
        return PutObjectRequest.builder()
                .bucket(bucket)
                .key(fullPath)
                .contentType("image/" + extension)
                .build();
    }

    public static PutObjectPresignRequest toPutObjectPresignRequest(PutObjectRequest putObjectRequest) {
        return PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5)) // PresignedURL 유효기간 5분
                .putObjectRequest(putObjectRequest)
                .build();
    }
}
