package com.spring.ai.Spring.AI.Project.controller;

import com.spring.ai.Spring.AI.Project.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
@RestController
public class UploadFileController {
    @Autowired
    private UploadService uploadService;


    @PostMapping("/embed-pdf")
    public ResponseEntity<String> embedPdf(
            @RequestParam("sessionId") String sessionId,
            @RequestParam("file") MultipartFile file
    ) {
        uploadService.processPdfAndSendToEmbedding(sessionId, file);
        return ResponseEntity.ok("PDF text sent to embedding API successfully");
    }
}
