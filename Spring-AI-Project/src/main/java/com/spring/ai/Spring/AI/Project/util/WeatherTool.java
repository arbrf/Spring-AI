package com.spring.ai.Spring.AI.Project.util;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class WeatherTool {

    private static final String API_KEY = "9d330177f90040b7b0b162327252411";
    private static final String BASE_URL = "https://api.weatherapi.com/v1/current.json";
    private final RestTemplate restTemplate = new RestTemplate();

    @Tool(name = "getWeather", description = "get real weather details for given city")
    public String getWeather(@JsonPropertyDescription("City name") String city)
    {
        System.out.println("The LLM uses my tool");

        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("key", API_KEY)
                .queryParam("q", city)
                .toUriString();

        try {
            String response = restTemplate.getForObject(url, String.class);
            System.out.println(response);
            return response;
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }


}
