package com.skeeterSoftworks.WorkOrderLocal.service;

import com.skeeterSoftworks.WorkOrderLocal.to.objects.CentralWorkOrderTO;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class CentralWorkOrdersProxyService {

    private WebClient webClient;

    @Value("${central.url}")
    private String centralUrl;

    @PostConstruct
    private void init() {
        webClient = WebClient.create();
    }

    public List<CentralWorkOrderTO> fetchWorkOrdersForMachine(Long machineId) {
        try {
            Mono<List<CentralWorkOrderTO>> response = webClient.get()
                    .uri(centralUrl + "/workorders/for-machine/{machineId}", machineId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<CentralWorkOrderTO>>() {
                    })
                    .doOnError(e -> log.error("Central work orders fetch failed: {}", e.getMessage()));
            List<CentralWorkOrderTO> list = response.block();
            return list != null ? list : Collections.emptyList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
