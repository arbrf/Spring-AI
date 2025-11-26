package com.spring.ai.Spring.AI.Project.service.Impl;

import com.spring.ai.Spring.AI.Project.service.GroqService;
import com.spring.ai.Spring.AI.Project.util.WeatherTool;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroqServiceImpl implements GroqService {
    @Autowired
    private ChatClient chatClient;


    @Override
    public String chat(String sessionId, String prompt) {

        StringBuilder sb=new StringBuilder();
        ToolCallback[] weatherTools = ToolCallbacks.from(new WeatherTool());


        sb.append("User question: ").append(prompt);
        String finalPrompt=sb.toString();
        System.out.println(finalPrompt);
        String response = chatClient
                .prompt()
              //  .system("Alway use the getWeather for any information related to weather")
                .user(finalPrompt)
                .tools(weatherTools)
                .call()
                .content();

        return response;
    }
}
