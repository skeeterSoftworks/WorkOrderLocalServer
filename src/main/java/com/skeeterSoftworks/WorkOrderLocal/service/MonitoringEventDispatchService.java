package com.skeeterSoftworks.WorkOrderLocal.service;

import com.skeeterSoftworks.WorkOrderLocal.to.objects.MonitoringClientEventTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class MonitoringEventDispatchService {

    private final CentralMonitoringEventsProxyService centralMonitoringEventsProxyService;
    private final WorkstationMachineConfigService workstationMachineConfigService;

    public MonitoringEventDispatchService(
            CentralMonitoringEventsProxyService centralMonitoringEventsProxyService,
            WorkstationMachineConfigService workstationMachineConfigService) {
        this.centralMonitoringEventsProxyService = centralMonitoringEventsProxyService;
        this.workstationMachineConfigService = workstationMachineConfigService;
    }

    public void publish(
            String eventType,
            Long workSessionId,
            Long workOrderId,
            Long goodProductsCount,
            String details) {
        try {
            MonitoringClientEventTO event = new MonitoringClientEventTO();
            event.setEventType(eventType);
            event.setMachineId(workstationMachineConfigService.readMachineId().orElse(null));
            event.setMachineName(workstationMachineConfigService.readMachineName().orElse(null));
            event.setWorkSessionId(workSessionId);
            event.setWorkOrderId(workOrderId);
            event.setGoodProductsCount(goodProductsCount);
            event.setDetails(details);
            event.setTimestamp(LocalDateTime.now());
            centralMonitoringEventsProxyService.publishMonitoringEvent(event);
        } catch (Exception e) {
            log.warn("Failed to publish monitoring event {}: {}", eventType, e.getMessage());
        }
    }
}
