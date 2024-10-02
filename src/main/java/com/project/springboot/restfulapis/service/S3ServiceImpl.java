package com.project.springboot.restfulapis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private static final Logger logger = LoggerFactory.getLogger(S3ServiceImpl.class);

    private final S3Client s3Client;

    public boolean uploadFiles(String bucketName, List<MultipartFile> files, boolean isReadPublicly) {
        boolean allUploadedSuccessfully = true;
        for (MultipartFile file : files) {
            log.info("Started uploading file '{}' to S3 Bucket '{}'", file.getOriginalFilename(), bucketName);
            PutObjectRequest putObjectRequest;
            if (isReadPublicly) {
                putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(file.getOriginalFilename())
                        .acl("public-read")
                        .build();
            } else {
                putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(file.getOriginalFilename())
                        .build();
            }
            try {
                s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
                log.info("Successfully uploaded file to S3. Bucket: {}, Key: {}", bucketName, file.getOriginalFilename());
            } catch (Exception e) {
                log.error("Failed to upload file to S3. Bucket: {}, Key: {}", bucketName, file.getOriginalFilename(), e);
                allUploadedSuccessfully = false; // Mark as failed if any file fails to upload
            }
        }
        return allUploadedSuccessfully;
    }
}
