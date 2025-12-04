package com.spring.ai.Spring.AI.Project.tools;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;
@Component
public class GreetingTool {

    @Tool(
            name = "greetUser",
            description = "Greets the user with a friendly message containing the provided name"
    )
    public String greetUser(@JsonPropertyDescription("User name") String name) {

        System.out.println("The LLM used the greeting tool");

        if (name == null || name.isBlank()) {
            return "Hello! Please provide a valid name.";
        }

        return "Hello " + name + "! Hope you're having a wonderful day 😊";
    }

}
