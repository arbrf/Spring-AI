package com.spring.ai.Spring.AI.Project.controller;



import com.spring.ai.Spring.AI.Project.response.ChatResponse;
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
    public Mono<ResponseEntity<ChatResponse>> chat(@RequestBody Map<String, String> payload) {
        String message = payload.get("message");

        System.out.println("Message recieved from HuggingFace");
        Mono<ResponseEntity<ChatResponse>> responseEntityMono = hfService.chat(message)
                .map(response -> {
                    // ✅ Extract the reply from the Map (since hfService returns Mono<Map>)
                    String reply = "";
                    if (response != null && response.containsKey("reply")) {
                        reply = (String) response.get("reply");
                    } else {
                        reply = "(no response)";
                    }

                    ChatResponse chatResponse = new ChatResponse(reply);

                    // ✅ Print to console
                    System.out.println("🧠 Model says: " + chatResponse.toString());

                    return ResponseEntity.ok(chatResponse);
                })
                .onErrorResume(e -> {
                    ChatResponse errorResponse = new ChatResponse("Failed to call Hugging Face: " + e.getMessage());
                    return Mono.just(ResponseEntity.status(500).body(errorResponse));
                });
        System.out.println(responseEntityMono);
        return responseEntityMono;
    }
}
