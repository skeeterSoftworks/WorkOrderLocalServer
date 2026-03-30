package com.skeeterSoftworks.WorkOrderLocal.facade;


import com.skeeterSoftworks.WorkOrderLocal.service.CentralSelectOptionsProxyService;
import com.skeeterSoftworks.WorkOrderLocal.service.ConfigService;
import com.skeeterSoftworks.WorkOrderLocal.service.WorkstationMachineConfigService;
import com.skeeterSoftworks.WorkOrderLocal.to.objects.SelectOptionsTO;
import com.skeeterSoftworks.WorkOrderLocal.to.objects.StationConfigTO;
import com.skeeterSoftworks.WorkOrderLocal.to.objects.WorkstationMachineConfigTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/config")
@CrossOrigin(origins = "*")
public class ConfigFacade {

    @Autowired
    private ConfigService configService;

    @Autowired
    private WorkstationMachineConfigService workstationMachineConfigService;

    @Autowired
    private CentralSelectOptionsProxyService centralSelectOptionsProxyService;

    @GetMapping("/station-config")
    public ResponseEntity<?> getStationConfig() {

        log.debug("Facade call: getStationConfig()");

        try {
            StationConfigTO stationConfigTO = new StationConfigTO();
            stationConfigTO.setMachineName(workstationMachineConfigService.readMachineName().orElse(null));
            stationConfigTO.setMachineId(workstationMachineConfigService.readMachineId().orElse(null));
            stationConfigTO.setWoPreconditionsJSON(configService.getWorkOrderPreconditions());
            return ResponseEntity.ok(stationConfigTO);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/workstation-machine")
    public ResponseEntity<WorkstationMachineConfigTO> getWorkstationMachine() {
        log.debug("Facade call: getWorkstationMachine()");
        WorkstationMachineConfigTO to = new WorkstationMachineConfigTO();
        to.setMachineName(workstationMachineConfigService.readMachineName().orElse(null));
        to.setMachineId(workstationMachineConfigService.readMachineId().orElse(null));
        return ResponseEntity.ok(to);
    }

    @PostMapping("/workstation-machine")
    public ResponseEntity<?> saveWorkstationMachine(@RequestBody WorkstationMachineConfigTO body) {
        log.debug("Facade call: saveWorkstationMachine()");
        if (body == null || body.getMachineName() == null || body.getMachineName().isBlank()) {
            return ResponseEntity.badRequest().body("MACHINE_NAME_REQUIRED");
        }
        try {
            workstationMachineConfigService.save(body);
            return ResponseEntity.ok(body);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().body("FAILED_TO_SAVE_WORKSTATION_MACHINE_CONFIG");
        }
    }

    @GetMapping("/select-options")
    public ResponseEntity<SelectOptionsTO> getSelectOptions() {
        log.debug("Facade call: getSelectOptions() (proxied to central)");
        try {
            return ResponseEntity.ok(centralSelectOptionsProxyService.fetchSelectOptions());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
