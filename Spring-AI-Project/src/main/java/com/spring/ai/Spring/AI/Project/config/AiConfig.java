package com.spring.ai.Spring.AI.Project.config;

import com.spring.ai.Spring.AI.Project.tools.DateTimeTools;
import com.spring.ai.Spring.AI.Project.tools.GreetingTool;
import com.spring.ai.Spring.AI.Project.tools.WeatherTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AiConfig {
    @Bean
    public ChatClient chatClient(
            OpenAiChatModel model,
            WeatherTool weatherTool,
            DateTimeTools dateTimeTool,
            GreetingTool greetingTool
    ) {
        return ChatClient.builder(model)
                .defaultTools(weatherTool, dateTimeTool,greetingTool)   // ⭐ multiple tools here
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
