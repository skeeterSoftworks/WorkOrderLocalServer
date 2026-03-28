package com.skeeterSoftworks.WorkOrderLocal.service;

import com.skeeterSoftworks.WorkOrderLocal.to.objects.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
@Slf4j
@Service
public class CentralWorkSessionsProxyService {

    private final WebClient webClient;

    @Value("${central.url}")
    private String centralUrl;

    public CentralWorkSessionsProxyService(@Qualifier("centralWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public WorkSessionResponseTO openSession(WorkSessionOpenRequestTO body) {
        return webClient.post()
                .uri(centralUrl + "/work-sessions/open")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(WorkSessionResponseTO.class)
                .block();
    }

    public WorkSessionResponseTO endSession(long id) {
        return webClient.patch()
                .uri(centralUrl + "/work-sessions/{id}/end", id)
                .retrieve()
                .bodyToMono(WorkSessionResponseTO.class)
                .block();
    }

    public WorkSessionResponseTO incrementProductCount(long id, ProductCountDeltaRequestTO body) {
        return webClient.post()
                .uri(centralUrl + "/work-sessions/{id}/product-count-delta", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(WorkSessionResponseTO.class)
                .block();
    }

    public WorkSessionResponseTO addControlProduct(long id, ControlProductCreateRequestTO body) {
        return webClient.post()
                .uri(centralUrl + "/work-sessions/{id}/control-products", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(WorkSessionResponseTO.class)
                .block();
    }

    public WorkSessionResponseTO addFaultyProduct(long id, FaultyProductCreateRequestTO body) {
        return webClient.post()
                .uri(centralUrl + "/work-sessions/{id}/faulty-products", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(WorkSessionResponseTO.class)
                .block();
    }

    public WorkSessionResponseTO getById(long id) {
        return webClient.get()
                .uri(centralUrl + "/work-sessions/{id}", id)
                .retrieve()
                .bodyToMono(WorkSessionResponseTO.class)
                .block();
    }

}
