package com.project.springboot.restfulapis.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {

    boolean uploadFiles(String bucketName, List<MultipartFile> files, boolean isReadPublicly);
}
