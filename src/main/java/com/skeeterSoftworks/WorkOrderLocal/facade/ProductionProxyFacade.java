package com.skeeterSoftworks.WorkOrderLocal.facade;

import com.skeeterSoftworks.WorkOrderLocal.service.CentralMachinesService;
import com.skeeterSoftworks.WorkOrderLocal.service.CentralProductsProxyService;
import com.skeeterSoftworks.WorkOrderLocal.service.CentralWorkOrdersProxyService;
import com.skeeterSoftworks.WorkOrderLocal.service.WorkstationMachineConfigService;
import com.skeeterSoftworks.WorkOrderLocal.to.objects.QualityInfoStepTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/production")
@CrossOrigin(origins = "*")
public class ProductionProxyFacade {

    @Autowired
    private WorkstationMachineConfigService workstationMachineConfigService;

    @Autowired
    private CentralMachinesService centralMachinesService;

    @Autowired
    private CentralWorkOrdersProxyService centralWorkOrdersProxyService;

    @Autowired
    private CentralProductsProxyService centralProductsProxyService;

    /**
     * Work orders scheduled on the machine bound in {@code workstation-machine.json}
     * (uses {@code machineId} when present, otherwise resolves {@code machineName} via central).
     */
    @GetMapping("/work-orders")
    public ResponseEntity<?> listWorkOrdersForBoundMachine() {
        log.debug("Facade call: listWorkOrdersForBoundMachine()");
        Optional<Long> machineId = workstationMachineConfigService.readMachineId();
        if (machineId.isEmpty()) {
            machineId = workstationMachineConfigService.readMachineName()
                    .flatMap(centralMachinesService::findMachineIdByMachineName);
        }
        if (machineId.isEmpty()) {
            return ResponseEntity.badRequest().body("WORKSTATION_MACHINE_NOT_CONFIGURED");
        }
        try {
            return ResponseEntity.ok(centralWorkOrdersProxyService.fetchWorkOrdersForMachine(machineId.get()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(502).body("CENTRAL_WORK_ORDERS_UNAVAILABLE");
        }
    }

    @GetMapping("/work-orders/{workOrderId}/quality-info-steps")
    public ResponseEntity<?> getQualityInfoSteps(@PathVariable Long workOrderId) {
        if (workOrderId == null || workOrderId <= 0) {
            return ResponseEntity.badRequest().body("INVALID_WORK_ORDER_ID");
        }
        try {
            return ResponseEntity.ok(centralWorkOrdersProxyService.fetchQualityInfoStepsForWorkOrder(workOrderId));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(502).body("CENTRAL_QUALITY_INFO_STEPS_UNAVAILABLE");
        }
    }

    @GetMapping("/bound-machine/products")
    public ResponseEntity<?> listProductsForBoundMachine() {
        Optional<Long> machineId = workstationMachineConfigService.readMachineId();
        if (machineId.isEmpty()) {
            machineId = workstationMachineConfigService.readMachineName()
                    .flatMap(centralMachinesService::findMachineIdByMachineName);
        }
        if (machineId.isEmpty()) {
            return ResponseEntity.badRequest().body("WORKSTATION_MACHINE_NOT_CONFIGURED");
        }
        try {
            return ResponseEntity.ok(centralProductsProxyService.fetchProductsForMachine(machineId.get()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(502).body("CENTRAL_PRODUCTS_UNAVAILABLE");
        }
    }

    @PutMapping("/bound-machine/products/{productId}/quality-info-steps")
    public ResponseEntity<?> replaceQualityInfoStepsForBoundMachine(
            @PathVariable Long productId,
            @RequestBody List<QualityInfoStepTO> steps
    ) {
        if (productId == null || productId <= 0) {
            return ResponseEntity.badRequest().body("INVALID_PRODUCT_ID");
        }
        Optional<Long> machineId = workstationMachineConfigService.readMachineId();
        if (machineId.isEmpty()) {
            machineId = workstationMachineConfigService.readMachineName()
                    .flatMap(centralMachinesService::findMachineIdByMachineName);
        }
        if (machineId.isEmpty()) {
            return ResponseEntity.badRequest().body("WORKSTATION_MACHINE_NOT_CONFIGURED");
        }
        try {
            List<QualityInfoStepTO> payload = steps != null ? steps : Collections.emptyList();
            return ResponseEntity.ok(
                    centralProductsProxyService.replaceQualityInfoSteps(productId, machineId.get(), payload)
            );
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(502).body("CENTRAL_PRODUCT_UPDATE_UNAVAILABLE");
        }
    }
}
