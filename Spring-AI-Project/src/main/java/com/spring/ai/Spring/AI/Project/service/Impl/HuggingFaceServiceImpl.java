package com.spring.ai.Spring.AI.Project.service.Impl;

import com.spring.ai.Spring.AI.Project.service.HuggingFace;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class HuggingFaceServiceImpl implements HuggingFace {

    private final WebClient webClient;
    private final String apiKey;

    public HuggingFaceServiceImpl(@Value("${HF_API_KEY:}") String apiKeyFromProps) {
        String envKey = System.getenv("HF_API_KEY");
        this.apiKey = (envKey != null && !envKey.isBlank()) ? envKey : apiKeyFromProps;
        System.out.println(this.apiKey);
        this.webClient = WebClient.builder()
                .baseUrl("https://router.huggingface.co")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + this.apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public Mono<Map> chat(String userMessage) {
        // Build the chat request
        Map<String, Object> body = Map.of(
                "model", "deepseek-ai/DeepSeek-R1:fastest",
                "messages", List.of(
                        Map.of("role", "user", "content", userMessage)
                )
        );

        // Send request to Hugging Face router
        return webClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    try {
                        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                        if (choices != null && !choices.isEmpty()) {
                            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                            if (message != null) {
                                String content = (String) message.get("content");
                                return Map.of("reply", content);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return Map.of("reply", "(no response from model)");
                });
    }
}