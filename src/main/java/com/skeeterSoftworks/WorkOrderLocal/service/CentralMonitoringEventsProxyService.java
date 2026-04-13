package com.skeeterSoftworks.WorkOrderLocal.service;

import com.skeeterSoftworks.WorkOrderLocal.to.objects.MonitoringClientEventTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CentralMonitoringEventsProxyService {

    private final WebClient webClient;

    @Value("${central.url}")
    private String centralUrl;

    public CentralMonitoringEventsProxyService(@Qualifier("centralWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public void publishMonitoringEvent(MonitoringClientEventTO body) {
        webClient.post()
                .uri(centralUrl + "/monitoring/events")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
