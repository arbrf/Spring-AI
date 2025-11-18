package com.spring.ai.Spring.AI.Project;

import com.spring.ai.Spring.AI.Project.entity.DocumentEntity;

import java.util.List;

public interface DocumentService {
    public DocumentEntity saveDocument(String content);
    public List<DocumentEntity> search(String query, int limit);
}
