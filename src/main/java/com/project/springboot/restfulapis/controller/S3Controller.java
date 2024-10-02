package com.project.springboot.restfulapis.controller;

import com.project.springboot.restfulapis.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class S3Controller {

    private final S3Service s3Service;

    @RequestMapping(value = "/upload3", method = RequestMethod.POST)
    public ResponseEntity<String> uploadFiles2(
            @RequestParam("bucketName") String bucketName,
            @RequestPart("file") List<MultipartFile> files,
            @RequestParam(value = "isReadPublicly", defaultValue = "false") boolean isReadPublicly) {
        List<String> successfulUploads = new ArrayList<>();
        List<String> failedUploads = new ArrayList<>();
        for (MultipartFile file : files) {
            boolean isUploaded = s3Service.uploadFiles(bucketName, files, isReadPublicly);
            if (isUploaded) {
                successfulUploads.add(file.getOriginalFilename());
            } else {
                failedUploads.add(file.getOriginalFilename());
            }
        }
        if (failedUploads.isEmpty()) {
            return ResponseEntity.ok("Files uploaded successfully: " + String.join(", ", successfulUploads));
        } else {
            String message = "Failed to upload files: " + String.join(", ", failedUploads);
            return ResponseEntity.status(500).body(message);
        }
    }
}