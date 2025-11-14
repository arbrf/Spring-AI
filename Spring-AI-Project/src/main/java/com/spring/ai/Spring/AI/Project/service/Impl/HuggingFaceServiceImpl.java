package com.spring.ai.Spring.AI.Project.service.Impl;

import com.spring.ai.Spring.AI.Project.service.HuggingFace;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class HuggingFaceServiceImpl implements HuggingFace {

    private final WebClient webClient;

    public HuggingFaceServiceImpl(@Value("${HF_API_KEY:}") String apiKeyFromProps) {
        String apiKey = System.getenv("HF_API_KEY");
        if (apiKey == null || apiKey.isBlank()) apiKey = apiKeyFromProps;
        System.out.println("Bearer "+apiKey);
        this.webClient = WebClient.builder()
                .baseUrl("https://router.huggingface.co")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public Map<String, Object> chat(String userMessage) {
        System.out.println("🧠 Message: " + userMessage);

        Map<String, Object> body = Map.of(
                "model", "deepseek-ai/DeepSeek-R1:fastest",
                "messages", List.of(Map.of("role", "user", "content", userMessage))
        );

        try {
            Map<String, Object> response = webClient.post()
                    .uri("/v1/chat/completions")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(); // ✅ Blocking call (OK for MVC)

            // Extract reply
            var choices = (List<Map<String, Object>>) response.get("choices");
            var message = (Map<String, Object>) choices.get(0).get("message");
            return Map.of("reply", message.get("content"));

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("reply", "❌ Error: " + e.getMessage());
        }
    }
}
