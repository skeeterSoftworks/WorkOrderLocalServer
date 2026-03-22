package com.skeeterSoftworks.WorkOrderLocal.service;

import com.skeeterSoftworks.WorkOrderLocal.to.objects.CentralMachineTO;
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
import java.util.Optional;

@Slf4j
@Service
public class CentralMachinesService {

    private WebClient webClient;

    @Value("${central.url}")
    private String centralUrl;

    @PostConstruct
    private void init() {
        webClient = WebClient.create();
    }

    public List<CentralMachineTO> fetchAllMachines() {
        try {
            Mono<List<CentralMachineTO>> response = webClient.get()
                    .uri(centralUrl + "/machines/all")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<CentralMachineTO>>() {
                    })
                    .doOnError(e -> log.error("Central machines fetch failed: {}", e.getMessage()));
            List<CentralMachineTO> list = response.block();
            return list != null ? list : Collections.emptyList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public Optional<Long> findMachineIdByMachineName(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        String target = name.trim();
        return fetchAllMachines().stream()
                .filter(m -> m.getMachineName() != null && target.equalsIgnoreCase(m.getMachineName().trim()))
                .map(CentralMachineTO::getId)
                .filter(id -> id != null && id > 0)
                .findFirst();
    }
}
