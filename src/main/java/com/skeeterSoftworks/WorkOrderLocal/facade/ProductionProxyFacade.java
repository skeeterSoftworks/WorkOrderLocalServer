package com.skeeterSoftworks.WorkOrderLocal.facade;

import com.skeeterSoftworks.WorkOrderLocal.service.CentralMachinesService;
import com.skeeterSoftworks.WorkOrderLocal.service.CentralWorkOrdersProxyService;
import com.skeeterSoftworks.WorkOrderLocal.service.WorkstationMachineConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
