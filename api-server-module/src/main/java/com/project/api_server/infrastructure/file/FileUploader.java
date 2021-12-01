package com.project.api_server.infrastructure.file;

import org.springframework.web.multipart.MultipartFile;


public interface FileUploader {
    String imageUpload(MultipartFile file) throws Exception;
}
