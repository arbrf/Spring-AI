package com.spring.ai.Spring.AI.Project.service.Impl;

import com.spring.ai.Spring.AI.Project.service.GroqService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GroqServiceImpl implements GroqService {
    @Autowired
    private ChatClient chatClient;

    @Value("${embedding.api.base-url}")
    private String baseUrl;

    private static final Log logger = LogFactory.getLog(GroqServiceImpl.class);


    private final RestTemplate restTemplate = new RestTemplate();


    @Override
    public String chat(String prompt,String sessionId) {
        Object saved=addRelevantContentApi(prompt,sessionId);
        if(saved!=null) {
            logger.info("Saved to vector DB: ");
        }
        else{
            logger.info("Not Saved to vector DB: " + saved);
        }
        List<Map<String, Object>> context = getRelevantContentApi(prompt);
        StringBuilder sb = new StringBuilder();
        sb.append("Relevant previous messages:\n");
        for (Map<String, Object> m : context) {
            sb.append("- ").append(m.get("content")).append("\n");
        }
        sb.append("\nUser question: ").append(prompt);
        String finalPrompt = sb.toString();
        logger.info("Prompt sent to LLM:\n" + finalPrompt);

        String response = chatClient
                .prompt()
                .user(finalPrompt)
                .call()
                .content();


        return response;
    }

    private List<Map<String, Object>> getRelevantContentApi(String text) {
        String url = baseUrl + "/vector-doc/search?query={query}&limit={limit}";

        Map<String, Object> params = new HashMap<>();
        params.put("query", text);
        params.put("limit", 5);

        List<Map<String, Object>> response =
                restTemplate.getForObject(url, List.class, params);

        return response;
    }

    private Object addRelevantContentApi(String text,String sessionID) {
        String url = baseUrl + "/vector-doc/add";

        Map<String, String> body = new HashMap<>();
        body.put("sessionId", sessionID);
        body.put("content", text);

      return  restTemplate.postForObject(
                url,
                body,
                Object.class
        );
    }
}
