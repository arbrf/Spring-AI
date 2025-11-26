package com.spring.ai.Spring.AI.Project.config;


import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class RagConfiguration {

    @Value("classpath:docs/olympic-faq.txt")
    private Resource olympicFaq;

    private final String vectorFileName = "vector.json";


    @Bean
    SimpleVectorStore getSimpleVectorStore(EmbeddingModel embeddingModel) throws IOException {
        System.out.println("🟩 EMBEDDING MODEL USED: " + embeddingModel.getClass().getName());
        System.out.println("🟦 RAG INIT STARTED");
        SimpleVectorStore simpleVectoreStore = SimpleVectorStore.builder(embeddingModel).build();
        File vectorStoreFile=getVectorFilePath();
        System.out.println("🔍 Vector store file path: " + vectorStoreFile.getAbsolutePath());
        if(vectorStoreFile.exists()){
            System.out.println("Vector File exisits");
            simpleVectoreStore.load(vectorStoreFile);
            System.out.println("📂 Vector store loaded successfully.");
        }
        else{
            System.out.println("❌ Vector file does NOT exist → creating new vector store...");
            System.out.println("📘 Reading FAQ text file: " + olympicFaq.getFilename());

            TextReader textReader=new TextReader(olympicFaq);
            textReader.getCustomMetadata().put("filename","olympic-faq.txt");
            List<Document> documents = textReader.get();

            System.out.println("📄 Total documents read: " + documents.size());
            System.out.println("📄 Content example: ");
            System.out.println(documents.get(0).getFormattedContent().substring(0, Math.min(200, documents.get(0).getFormattedContent().length())));
            System.out.println();

            TokenTextSplitter tokenTextSplitter=new TokenTextSplitter();
            List<Document> splitDocuments = tokenTextSplitter.apply(documents);
            System.out.println("✂️ Document splitting completed.");
            System.out.println("✂️ Total chunks created: " + splitDocuments.size());

            for (int i = 0; i < splitDocuments.size(); i++) {
                System.out.println("➡️ Chunk " + (i + 1) + " size: "
                        + splitDocuments.get(i).getFormattedContent().length() + " chars");
            }

            // STEP 3: Add to vector store
            System.out.println("🧬 Generating embeddings & adding chunks to vector store...");

            simpleVectoreStore.add(splitDocuments);
            simpleVectoreStore.save(vectorStoreFile);

            System.out.println("💾 Vector store saved to: " + vectorStoreFile.getAbsolutePath());

        }
        return simpleVectoreStore;
    }

    private File getVectorFilePath() {
        Path path = Paths.get("src", "main", "resources", "data");
        String absolutePath = path.toFile().getAbsolutePath() + "/" + vectorFileName;
        return  new File(absolutePath);
    }


}