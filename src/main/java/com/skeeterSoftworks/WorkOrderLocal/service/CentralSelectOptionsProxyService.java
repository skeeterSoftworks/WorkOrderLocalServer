package com.skeeterSoftworks.WorkOrderLocal.service;

import com.skeeterSoftworks.WorkOrderLocal.to.objects.SelectOptionsTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CentralSelectOptionsProxyService {

    private final WebClient webClient;

    @Value("${central.url}")
    private String centralUrl;

    public CentralSelectOptionsProxyService(@Qualifier("centralWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public SelectOptionsTO fetchSelectOptions() {
        try {
            Mono<SelectOptionsTO> response = webClient.get()
                    .uri(centralUrl + "/config/select-options")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(SelectOptionsTO.class)
                    .doOnError(e -> log.error("Central select-options fetch failed: {}", e.getMessage()));
            SelectOptionsTO body = response.block();
            if (body == null) {
                return new SelectOptionsTO();
            }
            if (body.getMeasuringTools() == null) {
                body.setMeasuringTools(new java.util.ArrayList<>());
            }
            if (body.getDeliveryTerms() == null) {
                body.setDeliveryTerms(new java.util.ArrayList<>());
            }
            if (body.getRejectCauses() == null) {
                body.setRejectCauses(new java.util.ArrayList<>());
            }
            return body;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
