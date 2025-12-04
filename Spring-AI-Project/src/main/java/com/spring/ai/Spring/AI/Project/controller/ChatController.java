package com.spring.ai.Spring.AI.Project.controller;



import com.spring.ai.Spring.AI.Project.response.ChatResponse;
import com.spring.ai.Spring.AI.Project.service.GroqService;
import com.spring.ai.Spring.AI.Project.service.HuggingFace;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final HuggingFace hfService;


    private final GroqService groqService;

    public ChatController(HuggingFace hfService,GroqService groqService) {
        this.hfService = hfService;

        this.groqService=groqService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody Map<String, String> payload) {
        String message = payload.get("message");
        System.out.println("📩 Message received from Angular: " + message);

        try {
            // ✅ Call your service method synchronously (block if it returns Mono)
            Map<String, Object> response = hfService.chat(message); // 👈 blocking call (OK in MVC)

            String reply = "(no response)";
            if (response != null && response.containsKey("reply")) {
                reply = (String) response.get("reply");
            }

            ChatResponse chatResponse = new ChatResponse(reply);
            System.out.println("🧠 Model says: " + chatResponse.toString());

            return ResponseEntity.ok(chatResponse);

        } catch (Exception e) {
            ChatResponse errorResponse = new ChatResponse("Failed to call Hugging Face: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

   /* @GetMapping("/chatclient")
    public Map<String, String> chat(@RequestParam String question) {
        String response = chatClient.prompt()
                .user(question)
                .call()
                .content();
        return Map.of("question", question, "answer", response);
    }*/

    @GetMapping("/chatclient")
    public Map<String, String> groqChat(@RequestParam String question,@RequestParam String sessionId) {
        String response =groqService.chat(question,sessionId);
        return Map.of("question", question, "answer", response);
    }
}
