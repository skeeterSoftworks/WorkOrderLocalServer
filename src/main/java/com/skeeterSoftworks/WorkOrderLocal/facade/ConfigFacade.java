package com.skeeterSoftworks.WorkOrderLocal.facade;


import com.skeeterSoftworks.WorkOrderLocal.service.ConfigService;
import com.skeeterSoftworks.WorkOrderLocal.to.objects.StationConfigTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/config")
public class ConfigFacade {

    @Value("${machine.name:machineHead}")
    private String machineName;

    @Autowired
    ConfigService configService;

    @GetMapping("/station-config")
    public ResponseEntity<?> getStationConfig() {

        log.debug("Facade call: getStationConfig()");

        try {
            StationConfigTO stationConfigTO = new StationConfigTO();
            stationConfigTO.setMachineName(machineName);
            stationConfigTO.setWoPreconditionsJSON(configService.getWorkOrderPreconditions());
            return ResponseEntity.ok(stationConfigTO);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
