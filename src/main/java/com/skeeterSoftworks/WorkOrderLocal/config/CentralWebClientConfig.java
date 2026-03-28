package com.skeeterSoftworks.WorkOrderLocal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CentralWebClientConfig {

    /**
     * Default 16 MiB — central responses such as {@code /workorders/{id}/quality-info-steps} include
     * Base64 image payloads; WebClient's default 256 KiB buffer triggers {@code DataBufferLimitException}.
     */
    @Bean(name = "centralWebClient")
    public WebClient centralWebClient(
            @Value("${central.webclient.max-in-memory-size-bytes:16777216}") int maxInMemoryBytes) {
        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(maxInMemoryBytes))
                        .build())
                .build();
    }
}
