package com.spring.ai.Spring.AI.Project.controller;

import com.spring.ai.Spring.AI.Project.outputparser.Pair;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("prompt-converter")
public class PromptConverterController {

    private final ChatClient chatClient;

    public PromptConverterController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/bean-converter")
    public Pair getBeanOutPutConverter(@RequestParam String question){
        BeanOutputConverter<Pair> converter = new BeanOutputConverter<>(Pair.class);
        String format = converter.getFormat();
        String templateText = """
            Answer the following question:

            {question}

            Return the response strictly in this format:
            {format}
            """;

        PromptTemplate promptTemplate=new PromptTemplate(templateText);
        Prompt prompt = promptTemplate.create(Map.of("format", format,"question",question));
        String content = chatClient.prompt(prompt).call().content();

        return converter.convert(content);
    }
    @GetMapping("/map-converter")
    public Map<String, Object> getMapOutPutConverter(@RequestParam String question){
        MapOutputConverter converter = new MapOutputConverter();
        String format = converter.getFormat();
        String templateText = """
            Answer the following question:

            {question}

            Return the response strictly in this format:
            {format}
            """;
        PromptTemplate promptTemplate=new PromptTemplate(templateText);
        Prompt prompt = promptTemplate.create(Map.of("format", format,"question",question));
        String content = chatClient.prompt(prompt).call().content();

        return converter.convert(content);
    }


    @GetMapping("/list-converter")
    public List<String> getListOutPutConverter(@RequestParam String question){
        ListOutputConverter converter = new ListOutputConverter(new DefaultConversionService());
        String format = converter.getFormat();
        String templateText = """
            Answer the following question:

            {question}

            Return the response strictly in this format:
            {format}
            """;

        PromptTemplate promptTemplate=new PromptTemplate(templateText);
        Prompt prompt = promptTemplate.create(Map.of("format", format,"question",question));
        String content = chatClient.prompt(prompt).call().content();

        return converter.convert(content);
    }


}
