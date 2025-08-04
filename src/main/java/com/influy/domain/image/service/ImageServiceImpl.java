package com.influy.domain.image.service;

import com.influy.domain.image.converter.ImageConverter;
import com.influy.domain.image.dto.ImageRequestDto;
import com.influy.domain.image.dto.ImageResponseDto;
import com.influy.global.apiPayload.code.status.ErrorStatus;
import com.influy.global.apiPayload.exception.GeneralException;
import com.influy.global.jwt.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.region}")
    private String region;

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    @Override
    @Transactional
    public ImageResponseDto.UploadResultDto uploadImg(CustomUserDetails userDetails, ImageRequestDto.UploadDto request) {
        String original = request.getImgName(); // influy.png
        String baseName = original.substring(0, original.lastIndexOf('.')); // influy
        String extension = original.substring(original.lastIndexOf('.') + 1); // png

        String fullPath = "image-src/" + UUID.randomUUID() + "-" + baseName + "." + extension; // image-src/123e4567-influy.png

        PutObjectRequest objectRequest = ImageConverter.toPutObjectRequest(bucket, fullPath, extension);
        PutObjectPresignRequest presignRequest = ImageConverter.toPutObjectPresignRequest(objectRequest);
        PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

        String imageUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + fullPath;

        return ImageConverter.toUploadResultDto(presignedRequest.url(), imageUrl);
    }

    @Override
    @Transactional  //이미지 경로 리팩 제안
    public String duplicateImg(String sourceURL) {


        String keyPrefix = "https://" + bucket + ".s3."+region+".amazonaws.com/"; // URL 패턴

        if(!sourceURL.startsWith(keyPrefix)) {
            throw new GeneralException(ErrorStatus.INVALID_IMAGE_URL);
        }
        String sourceKey = sourceURL.replace(keyPrefix, ""); // "items/1234/image1.jpg"

        String original = sourceURL.substring(sourceURL.lastIndexOf('/') + 1);// 123e4567-influy.png
        String baseName = original.substring(0, original.lastIndexOf('.')); // influy
        String extension = original.substring(original.lastIndexOf('.') + 1); // png

        String destinationKey = "image-src/" + UUID.randomUUID() + "-" + baseName + "." + extension; // image-src/123e4567-influy.png
        String imageUrl = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + destinationKey;

        CopyObjectRequest copyReq = CopyObjectRequest.builder()
                .sourceBucket(bucket)
                .sourceKey(sourceKey)
                .destinationBucket(bucket)
                .destinationKey(destinationKey)
                .build();

        s3Client.copyObject(copyReq);

        return imageUrl;

    }
}
