package com.spring.ai.Spring.AI.Project.service.Impl;

import com.spring.ai.Spring.AI.Project.service.UploadService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class UploadServiceImpl implements UploadService {


    @Value("${embedding.api.base-url}")
    private String baseUrl;

    String embeddingApiUrl = baseUrl + "/vector-doc/embed";
    private final RestTemplate restTemplate = new RestTemplate();

    public void processPdfAndSendToEmbedding(String sessionId, MultipartFile pdfFile) {
        // 1. extract text
        String text = extractPdfText(pdfFile);

        // 2. call your existing API
        sendToEmbeddingApi(sessionId, text);
    }


    public String extractPdfText(MultipartFile file) {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract PDF text", e);
        }
    }

    private void sendToEmbeddingApi(String sessionId, String text) {
        Map<String, Object> body = new HashMap<>();
        body.put("sessionId", sessionId);
        body.put("content", text); // full PDF text

        // POST http://localhost:8082/vector-doc/embed
        restTemplate.postForEntity(embeddingApiUrl, body, String.class);
    }


}