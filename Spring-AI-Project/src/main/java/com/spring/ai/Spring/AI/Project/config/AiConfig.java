package com.spring.ai.Spring.AI.Project.config;

import com.spring.ai.Spring.AI.Project.util.DateTimeTools;
import com.spring.ai.Spring.AI.Project.util.WeatherTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AiConfig {
    @Bean
    public ChatClient chatClient(
            OpenAiChatModel model,
            WeatherTool weatherTool,
            DateTimeTools dateTimeTool
    ) {
        return ChatClient.builder(model)
                .defaultTools(weatherTool, dateTimeTool)   // ⭐ multiple tools here
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
