package com.spring.ai.Spring.AI.Project.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {

    public String extractPdfText(MultipartFile file);
    public void processPdfAndSendToEmbedding(String sessionId, MultipartFile pdfFile);
}
