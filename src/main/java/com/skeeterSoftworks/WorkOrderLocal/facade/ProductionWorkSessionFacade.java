package com.skeeterSoftworks.WorkOrderLocal.facade;

import com.skeeterSoftworks.WorkOrderLocal.service.CentralWorkSessionsProxyService;
import com.skeeterSoftworks.WorkOrderLocal.service.WorkSessionGoodCountBufferService;
import com.skeeterSoftworks.WorkOrderLocal.to.objects.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@RestController
@RequestMapping("/production/work-sessions")
@CrossOrigin(origins = "*")
public class ProductionWorkSessionFacade {

    private final CentralWorkSessionsProxyService centralWorkSessionsProxyService;
    private final WorkSessionGoodCountBufferService workSessionGoodCountBufferService;

    @Autowired
    public ProductionWorkSessionFacade(
            CentralWorkSessionsProxyService centralWorkSessionsProxyService,
            WorkSessionGoodCountBufferService workSessionGoodCountBufferService) {
        this.centralWorkSessionsProxyService = centralWorkSessionsProxyService;
        this.workSessionGoodCountBufferService = workSessionGoodCountBufferService;
    }

    @PostMapping("/open")
    public ResponseEntity<?> open(@RequestBody WorkSessionOpenRequestTO body) {
        try {
            return ResponseEntity.ok(centralWorkSessionsProxyService.openSession(body));
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(502).body("CENTRAL_WORK_SESSION_UNAVAILABLE");
        }
    }

    @PostMapping("/{id}/end")
    public ResponseEntity<?> end(@PathVariable Long id) {
        try {
            workSessionGoodCountBufferService.flushSession(id);
            WorkSessionResponseTO ended = centralWorkSessionsProxyService.endSession(id);
            workSessionGoodCountBufferService.clearSession(id);
            return ResponseEntity.ok(ended);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(502).body("CENTRAL_WORK_SESSION_UNAVAILABLE");
        }
    }

    @PostMapping("/{id}/good-delta")
    public ResponseEntity<?> goodDelta(@PathVariable Long id, @RequestBody ProductCountDeltaRequestTO body) {
        try {
            if (body.getDelta() <= 0) {
                return ResponseEntity.badRequest().body("INVALID_DELTA");
            }
            workSessionGoodCountBufferService.recordDelta(id, body.getDelta(), body.getProductReferenceID());
            WorkSessionResponseTO updated = workSessionGoodCountBufferService.flushSessionReturningUpdated(id);
            if (updated == null) {
                updated = centralWorkSessionsProxyService.getById(id);
            }
            return ResponseEntity.ok(updated);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.internalServerError().body("ERROR_RECORDING_GOOD_DELTA");
        }
    }

    @PostMapping("/{id}/control-products")
    public ResponseEntity<?> controlProducts(@PathVariable Long id, @RequestBody ControlProductCreateRequestTO body) {
        try {
            return ResponseEntity.ok(centralWorkSessionsProxyService.addControlProduct(id, body));
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(502).body("CENTRAL_WORK_SESSION_UNAVAILABLE");
        }
    }

    @PostMapping("/{id}/faulty-products")
    public ResponseEntity<?> faultyProducts(@PathVariable Long id, @RequestBody FaultyProductCreateRequestTO body) {
        try {
            return ResponseEntity.ok(centralWorkSessionsProxyService.addFaultyProduct(id, body));
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(502).body("CENTRAL_WORK_SESSION_UNAVAILABLE");
        }
    }

    @PostMapping("/{id}/setup-products")
    public ResponseEntity<?> setupProducts(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(centralWorkSessionsProxyService.addSetupProduct(id));
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(502).body("CENTRAL_WORK_SESSION_UNAVAILABLE");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(centralWorkSessionsProxyService.getById(id));
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(502).body("CENTRAL_WORK_SESSION_UNAVAILABLE");
        }
    }
}
