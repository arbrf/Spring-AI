package com.spring.ai.Spring.AI.Project.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;


@Entity
@Data
@Table(name = "documents")
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Column(columnDefinition = "vector(6)")
    private float[] embedding;
}
