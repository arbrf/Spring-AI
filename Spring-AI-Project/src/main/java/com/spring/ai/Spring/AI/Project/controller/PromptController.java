package com.spring.ai.Spring.AI.Project.controller;

import org.springframework.core.io.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prompt")
public class PromptController {
    private final ChatClient chatClient;

    @Value("classpath:docs/info.txt")
    private Resource infoFile;

    @Value("classpath:prompt/question.st")
    private Resource questionTemplateFile;


    public PromptController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("chatclient")
    public Map<String, String> prompt(@RequestParam String question) {
        Prompt prompt=new Prompt(question);
        String response = chatClient.prompt(prompt).call().content();
        return Map.of("question", question, "answer", response);
    }
    @GetMapping("/system-message")
    public Map<String, String> promptSystemMessage(@RequestParam String question) {
        SystemMessage systemMessage=new SystemMessage("Tell only dad jokes" +"If the user asks for anything that is not a dad joke, refuse by saying: 'I can only tell dad jokes.'");
        UserMessage userMessage=new UserMessage(question);
        Prompt prompt=new Prompt(
                 List.of(systemMessage,userMessage)
        );
        String response = chatClient.prompt(prompt).call().content();
        return Map.of("question", question, "answer", response);
    }
    @GetMapping("/promptTemplate")
    public Map<String, String> promptTemplate(@RequestParam String question,
                                              @RequestParam(defaultValue = "tech") String genre) {
        PromptTemplate promptTemplate=new PromptTemplate(question);
        Prompt prompt=promptTemplate.create(Map.of("genre",genre));
        String response = chatClient.prompt(prompt).call().content();
        return Map.of("question", question, "answer", response);
    }

    @GetMapping("")
    public void multiTurnPrompt(@RequestParam String question) {

        //String question = "I want to learn microservices. Where should I start? my name is davoodh";
        Prompt prompt = new Prompt(
                List.of(
                        new SystemMessage("You are a friendly assistant. my name is davoodh"),
                        new UserMessage(question),
                        new AssistantMessage("Start with Spring Boot basics, then move into service registry and API gateways.")

                )
        );

        String response = chatClient.prompt(prompt).call().content();
        System.out.println(response);
    }

    @GetMapping("/olympics-2026")
    public String get2026OlympicData(@RequestParam(value = "message",defaultValue = "Hi waht is your name") String message,
                                     @RequestParam boolean stuffit) throws IOException {
        Map<String ,Object> map=new HashMap<>();

        String contextText=stuffit?infoFile.getContentAsString(Charset.defaultCharset()):"";
        map.put("question",message);
        map.put("context", contextText);

        String contentAsString = questionTemplateFile.getContentAsString(Charset.defaultCharset());
        PromptTemplate promptTemplate = new PromptTemplate(contentAsString);
        Prompt prompt = promptTemplate.create(map);
        System.out.println("Map Values are "+String.valueOf(map));
        System.out.println(contentAsString);
        System.out.println(contextText);
       String content = chatClient.prompt(prompt).call().content();
       // String content="Hello";
        System.out.println("The response is :"+content);
        return content;
    }

}
