package com.skeeterSoftworks.WorkOrderLocal.service;

import com.skeeterSoftworks.WorkOrderLocal.to.objects.CentralWorkOrderTO;
import com.skeeterSoftworks.WorkOrderLocal.to.objects.QualityInfoStepTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private final WebClient webClient;

    @Value("${central.url}")
    private String centralUrl;

    public CentralWorkOrdersProxyService(@Qualifier("centralWebClient") WebClient webClient) {
        this.webClient = webClient;
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

    public List<QualityInfoStepTO> fetchQualityInfoStepsForWorkOrder(Long workOrderId) {
        try {
            Mono<List<QualityInfoStepTO>> response = webClient.get()
                    .uri(centralUrl + "/workorders/{id}/quality-info-steps", workOrderId)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<QualityInfoStepTO>>() {
                    })
                    .doOnError(e -> log.error("Central quality info steps fetch failed: {}", e.getMessage()));
            List<QualityInfoStepTO> list = response.block();
            return list != null ? list : Collections.emptyList();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
