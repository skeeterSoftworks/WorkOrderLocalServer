package com.skeeterSoftworks.WorkOrderLocal.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ConfigService {

    private WebClient webClient;

    @Value("${central.url}")
    private String centralUrl;

    @PostConstruct
    private void init() {
        webClient = WebClient.create();
    }


    public String getWorkOrderPreconditions() {

        Mono<String> response = webClient.get()
                .uri(centralUrl + "/config/get-wo-preconditions")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<String>() {
                })
                .doOnError(throwable ->
                        log.error(throwable.getMessage(), throwable)
                );

        return response.block();
    }

}
