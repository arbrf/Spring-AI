package com.spring.ai.Spring.AI.Project.controller;

import com.spring.ai.Spring.AI.Project.DocumentService;
import com.spring.ai.Spring.AI.Project.entity.DocumentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService service;

    @PostMapping("/add")
    public DocumentEntity addDocument(@RequestBody String content) {
        return service.saveDocument(content);
    }

    @GetMapping("/search")
    public List<DocumentEntity> search(@RequestParam String q) {
        return service.search(q, 5);   // return top 5 results
    }
}
