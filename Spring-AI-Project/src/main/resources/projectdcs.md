# 🧠 Spring AI Chatbot — Hugging Face Integration

A Spring Boot (WebFlux) project that integrates with Hugging Face’s Router API to create an AI-powered chatbot using the DeepSeek R1 model.

The project demonstrates reactive programming with Mono, secure API key usage, and clean separation between controller, service, and response layers.

---

## 🚀 Features

✅ Reactive and non-blocking API using Spring WebFlux
✅ Integration with Hugging Face Inference Router (`/v1/chat/completions`)
✅ Model: `deepseek-ai/DeepSeek-R1:fastest`
✅ Configurable API key via environment variables
✅ Clean JSON response using custom `ChatResponse` POJO
✅ Console logging for understanding Mono execution flow

## ⚙️ Tech Stack

| Layer            | Technology                         |
| ---------------- | ---------------------------------- |
| Language         | Java 17+                           |
| Framework        | Spring Boot (WebFlux)              |
| HTTP Client      | Spring WebClient                   |
| Reactive Type    | Mono (Project Reactor)             |
| AI Model         | DeepSeek-R1 (via Hugging Face API) |
| Response Mapping | Custom `ChatResponse`              |
| Build Tool       | Maven / Gradle                     |



## 🧩 Project Structure

```
Spring-AI-Project/
├── src/main/java/com/spring/ai/Spring/AI/Project/
│   ├── controller/
│   │   └── ChatController.java
│   ├── response/
│   │   └── ChatResponse.java
│   ├── service/
│   │   ├── HuggingFace.java        # interface
│   │   └── Impl/
│   │       └── HuggingFaceServiceImpl.java
│   └── SpringAiApplication.java
└── src/main/resources/
    └── application.properties
```

---

## ⚙️ Configuration

### `application.properties`

```properties
spring.application.name=Spring-AI

# ✅ Hugging Face API Key
# Prefer environment variable HF_API_KEY; fallback to here for local dev
HF_API_KEY=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

# Default model
app.huggingface.model=gpt2

# Server port
server.port=8080
```



## 🧠 Code Breakdown

### 🟢 1. ChatController.java

Handles the `/api/chat` REST endpoint.

```java
@PostMapping("/chat")
public Mono<ResponseEntity<ChatResponse>> chat(@RequestBody Map<String, String> payload) {
    String message = payload.get("message");
    System.out.println("Message received from HuggingFace");

    return hfService.chat(message)
        .map(response -> {
            String reply = (String) response.getOrDefault("reply", "(no response)");
            ChatResponse chatResponse = new ChatResponse(reply);
            System.out.println("🧠 Model says: " + chatResponse.toString());
            return ResponseEntity.ok(chatResponse);
        })
        .onErrorResume(e -> {
            ChatResponse errorResponse = new ChatResponse("Failed to call Hugging Face: " + e.getMessage());
            return Mono.just(ResponseEntity.status(500).body(errorResponse));
        });
}
```

🔹 Responsibilities:

Receives input JSON like `{ "message": "Hello" }`
Passes message to `HuggingFaceService`
Returns AI-generated reply in JSON format:

  ```json
  { "reply": "Hi there! How can I help?" }
  ```
Handles API or parsing errors gracefully

---

### 🟢 2. ChatResponse.java

A clean POJO for chatbot responses.

```java
public class ChatResponse {
    private String reply;

    @Override
    public String toString() {
        return reply != null
            ? reply.replaceAll("(?s)<think>.?</think>", "").trim()
            : "";
    }
}
```

🔹 Responsibilities:

Encapsulates chatbot output
Strips `<think>...</think>` reasoning text from Hugging Face replies for readability

---

### 🟢 3. HuggingFaceServiceImpl.java

Handles actual communication with Hugging Face’s Router API.

```java
@Service
public class HuggingFaceServiceImpl implements HuggingFace {

    private final WebClient webClient;

    public HuggingFaceServiceImpl(@Value("${HF_API_KEY:}") String apiKeyFromProps) {
        String envKey = System.getenv("HF_API_KEY");
        String apiKey = (envKey != null && !envKey.isBlank()) ? envKey : apiKeyFromProps;
        this.webClient = WebClient.builder()
                .baseUrl("https://router.huggingface.co")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public Mono<Map> chat(String userMessage) {
        System.out.println("1️⃣ chat() called with message: " + userMessage);

        Map<String, Object> body = Map.of(
                "model", "deepseek-ai/DeepSeek-R1:fastest",
                "messages", List.of(
                        Map.of("role", "user", "content", userMessage)
                )
        );

        System.out.println("2️⃣ Request body prepared: " + body);

        return webClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    System.out.println("🧩 Response From Hugging Face: " + response);
                    List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                    if (choices != null && !choices.isEmpty()) {
                        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                        if (message != null) {
                            String content = (String) message.get("content");
                            System.out.println("✅ Final model reply: " + content);
                            return Map.of("reply", content);
                        }
                    }
                    return Map.of("reply", "(no response from model)");
                });
    }
}
```

🔹 Responsibilities:

Builds the HTTP request payload
Sends async POST to:

  ```
  https://router.huggingface.co/v1/chat/completions
  ```
Logs the entire execution lifecycle using `System.out.println`
Extracts AI-generated text from Hugging Face JSON

---

## 🔁 Sample Flow

### Request:

```bash
POST http://localhost:8080/api/chat
Content-Type: application/json

{
  "message": "How is your day?"
}
```

### Console Output:

```
1️⃣ chat() called with message: How is your day?
2️⃣ Request body prepared: {model=deepseek-ai/DeepSeek-R1:fastest, messages=[{role=user, content=How is your day?}]}
🧩 Response From Hugging Face: {choices=[{message={role=assistant, content=<think>...</think>I'm doing great!}}]}
✅ Final model reply: <think>...</think>I'm doing great!
🧠 Model says: I'm doing great!
```

### Response:

```json
{
  "reply": "I'm doing great!"
}
```

---

## ⚙️ Running the Project

### Prerequisites

Java 17+
Maven 3.8+ or Gradle
Internet access
Hugging Face API key

### Run the app

```bash
mvn spring-boot:run
```

or

```bash
./mvnw spring-boot:run
```

Server starts at:

```
http://localhost:8080
```

---

## 🧪 Testing with Postman

Endpoint:

```
POST http://localhost:8080/api/chat
```

Headers:

```
Content-Type: application/json
```

Body:

```json
{
  "message": "Tell me a joke"
}
```

Expected Output:

```json
{
  "reply": "Why did the computer get cold? Because it forgot to close its Windows!"
}
```

---

## 🧠 Internal Execution Timeline (Reactive)

| Step | Description                              |
| ---- | ---------------------------------------- |
| 1️⃣  | `chat()` called in service               |
| 2️⃣  | Request body prepared                    |
| 3️⃣  | WebClient builds Mono pipeline (lazy)    |
| 4️⃣  | Spring WebFlux subscribes automatically  |
| 5️⃣  | Hugging Face request sent                |
| 6️⃣  | Response received                        |
| 7️⃣  | `.map()` extracts `"reply"`              |
| 8️⃣  | Controller converts Map → `ChatResponse` |
| 9️⃣  | Mono completes, JSON returned to client  |

---

## 🔒 Security Best Practices

✅ Never commit API keys
✅ Use environment variables (`HF_API_KEY`)
✅ Add `src/main/resources/application.properties` to `.gitignore`
✅ Rotate Hugging Face tokens periodically

---

## 📦 Future Enhancements

🔄 Add model selection dynamically via request payload
🧠 Support streaming responses (`Flux<String>`)
🧩 Add caching layer for repeated prompts
🌐 Build Angular front-end for interactive UI
🧰 Integrate OpenAI or Anthropic models

---

## 🧾 License

This project is for educational and internal use.
All AI responses are powered by Hugging Face Router API and subject to its [Terms of Service](https://huggingface.co/terms).

