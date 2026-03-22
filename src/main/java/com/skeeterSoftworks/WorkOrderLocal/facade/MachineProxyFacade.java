package com.skeeterSoftworks.WorkOrderLocal.facade;

import com.skeeterSoftworks.WorkOrderLocal.service.CentralMachinesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/machines")
@CrossOrigin(origins = "*")
public class MachineProxyFacade {

    @Autowired
    private CentralMachinesService centralMachinesService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllFromCentral() {
        log.debug("Facade call: proxy GET /machines/all -> central");
        try {
            return ResponseEntity.ok(centralMachinesService.fetchAllMachines());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(502).body("CENTRAL_MACHINES_UNAVAILABLE");
        }
    }
}
