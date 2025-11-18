package com.spring.ai.Spring.AI.Project.service.Impl;

import com.spring.ai.Spring.AI.Project.DocumentService;
import com.spring.ai.Spring.AI.Project.entity.DocumentEntity;

import com.spring.ai.Spring.AI.Project.repository.DocumentsRepository;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentsRepository repository;

    @Autowired
    private OpenAiEmbeddingModel embeddingModel;

    // Convert text to vector using OpenAI
    private float[] embed(String text) {
        float[] response = embeddingModel.embed(text);
        return response;
    }

    // Save Document + its embedding
    public DocumentEntity saveDocument(String content) {
        float[] vector = embed(content);

        DocumentEntity entity = new DocumentEntity();
        entity.setContent(content);
        entity.setEmbedding(vector);

        return repository.save(entity);
    }

    // Search similar documents
    public List<DocumentEntity> search(String query, int limit) {
        float[] queryVector = embed(query);
        return repository.findSimilar(queryVector, limit);
    }
}
