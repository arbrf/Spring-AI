package com.spring.ai.Spring.AI.Project.service;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface HuggingFace {
    public Mono<Map> chat(String userMessage);
}
