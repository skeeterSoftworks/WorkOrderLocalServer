package com.skeeterSoftworks.WorkOrderLocal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ConfigService {

    private final WebClient webClient;

    @Value("${central.url}")
    private String centralUrl;

    public ConfigService(@Qualifier("centralWebClient") WebClient webClient) {
        this.webClient = webClient;
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
