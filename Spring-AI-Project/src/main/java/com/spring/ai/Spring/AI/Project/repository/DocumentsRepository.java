package com.spring.ai.Spring.AI.Project.repository;

import com.spring.ai.Spring.AI.Project.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DocumentsRepository extends JpaRepository<DocumentEntity, Long> {

    @Query(value = """
        SELECT * FROM documents
        ORDER BY embedding <-> :vector
        LIMIT :limit
        """, nativeQuery = true)
    public List<DocumentEntity> findSimilar(
            @Param("vector") float[] vector,
            @Param("limit") int limit
    );
}
