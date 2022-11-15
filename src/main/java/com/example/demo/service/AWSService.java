package com.example.demo.service;

import com.example.demo.model.IPRanges;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AWSService {

    private final WebClient webClient;

    public AWSService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://ip-ranges.amazonaws.com/ip-ranges.json").build();
    }

    public Mono<IPRanges> someRestCall() {
        return this.webClient.get().retrieve().bodyToMono(IPRanges.class);
    }

}
