package com.influy.domain.image.service;

import com.influy.domain.image.converter.ImageConverter;
import com.influy.domain.image.dto.ImageRequestDto;
import com.influy.domain.image.dto.ImageResponseDto;
import com.influy.domain.item.entity.Item;
import com.influy.global.jwt.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.region}")
    private String region;

    private final S3Presigner s3Presigner;

    @Override
    @Transactional
    public ImageResponseDto.UploadResultDto uploadImg(CustomUserDetails userDetails, ImageRequestDto.UploadDto request) {
        String original = request.getImgName(); // influy.png
        String baseName = original.substring(0, original.lastIndexOf('.')); // influy
        String extension = original.substring(original.lastIndexOf('.') + 1); // png

        String fullPath = "image-src/user-" + userDetails.getId() + "/" + UUID.randomUUID() + "-" + baseName + "." + extension; // image-src/user-1/123e4567-influy.png

        PutObjectRequest objectRequest = ImageConverter.toPutObjectRequest(bucket, fullPath, extension);
        PutObjectPresignRequest presignRequest = ImageConverter.toPutObjectPresignRequest(objectRequest);
        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        String imageUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fullPath;

        return ImageConverter.toUploadResultDto(presignedRequest.url(), imageUrl);
    }
}
