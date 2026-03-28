package com.skeeterSoftworks.WorkOrderLocal.service;

import com.skeeterSoftworks.WorkOrderLocal.to.objects.ProductQualityInfoUpdateTO;
import com.skeeterSoftworks.WorkOrderLocal.to.objects.ProductTO;
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
public class CentralProductsProxyService {

    private final WebClient webClient;

    @Value("${central.url}")
    private String centralUrl;

    public CentralProductsProxyService(@Qualifier("centralWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public List<ProductTO> fetchProductsForMachine(long machineId) {
        Mono<List<ProductTO>> response = webClient.get()
                .uri(centralUrl + "/products/for-machine/{machineId}", machineId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ProductTO>>() {
                })
                .doOnError(e -> log.error("Central products for machine fetch failed: {}", e.getMessage()));
        List<ProductTO> list = response.block();
        return list != null ? list : Collections.emptyList();
    }

    public ProductTO replaceQualityInfoSteps(long productId, long machineId, List<QualityInfoStepTO> steps) {
        ProductQualityInfoUpdateTO body = new ProductQualityInfoUpdateTO(machineId, steps);
        return webClient.put()
                .uri(centralUrl + "/products/{productId}/quality-info-steps", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(ProductTO.class)
                .block();
    }
}
