package com.spring.ai.Spring.AI.Project.controller;



import com.spring.ai.Spring.AI.Project.service.HuggingFace;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final HuggingFace hfService;

    public ChatController(HuggingFace hfService) {
        this.hfService = hfService;
    }

    @PostMapping("/chat")
    public Mono<ResponseEntity<Map>> chat(@RequestBody Map<String, String> payload) {
        String message = payload.get("message");
        if (message == null || message.isBlank()) {
            return Mono.just(ResponseEntity.badRequest().body(Map.of("error", "message is required")));
        }
        System.out.println("Message recieved from HuggingFace");
        return hfService.chat(message)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.status(500)
                        .body(Map.of("error", "Failed to call Hugging Face", "details", e.getMessage()))));
    }
}
