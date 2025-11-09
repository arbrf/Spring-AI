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

        String bearerToken = "Bearer " + this.apiKey;
        System.out.println(bearerToken);
        this.webClient = WebClient.builder()
                .baseUrl("https://router.huggingface.co")
                .defaultHeader(HttpHeaders.AUTHORIZATION, bearerToken)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public Mono<Map> chat(String userMessage) {
        System.out.println("1️⃣ chat() called with message: " + userMessage);
        // Build the chat request
        Map<String, Object> body = Map.of(
                "model", "deepseek-ai/DeepSeek-R1:fastest",
                "messages", List.of(
                        Map.of("role", "user", "content", userMessage)
                )
        );

        System.out.println("2️⃣ Request body prepared: " + body);

        // Send request to Hugging Face router
        return webClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    try {
                        System.out.println("Response From Hugging face "+response);
                        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                        System.out.println("6️⃣ choices: " + choices);
                        if (choices != null && !choices.isEmpty()) {
                            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                            System.out.println("7️⃣ message: " + message);
                            if (message != null) {
                                String content = (String) message.get("content");
                                System.out.println("8️⃣ Final model reply: " + content);
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