package com.skeeterSoftworks.WorkOrderLocal.facade;

import com.skeeterSoftworks.WorkOrderLocal.service.CentralWorkSessionsProxyService;
import com.skeeterSoftworks.WorkOrderLocal.service.MonitoringEventDispatchService;
import com.skeeterSoftworks.WorkOrderLocal.service.WorkSessionGoodCountBufferService;
import com.skeeterSoftworks.WorkOrderLocal.to.objects.ControlProductCreateRequestTO;
import com.skeeterSoftworks.WorkOrderLocal.to.objects.FaultyProductCreateRequestTO;
import com.skeeterSoftworks.WorkOrderLocal.to.objects.HelpSignalRequestTO;
import com.skeeterSoftworks.WorkOrderLocal.to.objects.ProductCountDeltaRequestTO;
import com.skeeterSoftworks.WorkOrderLocal.to.objects.WorkSessionOpenRequestTO;
import com.skeeterSoftworks.WorkOrderLocal.to.objects.WorkSessionResponseTO;
import com.skeeterSoftworks.WorkOrderLocal.to.objects.WorkSessionSetupProductCreateTO;
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
    private final MonitoringEventDispatchService monitoringEventDispatchService;

    @Autowired
    public ProductionWorkSessionFacade(
            CentralWorkSessionsProxyService centralWorkSessionsProxyService,
            WorkSessionGoodCountBufferService workSessionGoodCountBufferService,
            MonitoringEventDispatchService monitoringEventDispatchService) {
        this.centralWorkSessionsProxyService = centralWorkSessionsProxyService;
        this.workSessionGoodCountBufferService = workSessionGoodCountBufferService;
        this.monitoringEventDispatchService = monitoringEventDispatchService;
    }

    @PostMapping("/open")
    public ResponseEntity<?> open(@RequestBody WorkSessionOpenRequestTO body) {
        try {
            WorkSessionResponseTO opened = centralWorkSessionsProxyService.openSession(body);
            monitoringEventDispatchService.publish(
                    "WORK_SESSION_OPEN",
                    opened.getId(),
                    opened.getWorkOrderId(),
                    null,
                    null);
            return ResponseEntity.ok(opened);
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
            monitoringEventDispatchService.publish(
                    "WORK_SESSION_CLOSED",
                    ended.getId(),
                    ended.getWorkOrderId(),
                    null,
                    null);
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
            monitoringEventDispatchService.publish(
                    "RECORDED_GOOD_PRODUCTS",
                    updated.getId(),
                    updated.getWorkOrderId(),
                    body.getDelta(),
                    null);
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
            WorkSessionResponseTO updated = centralWorkSessionsProxyService.addControlProduct(id, body);
            monitoringEventDispatchService.publish(
                    "RECORDED_CONTROL_PRODUCT",
                    updated.getId(),
                    updated.getWorkOrderId(),
                    null,
                    null);
            return ResponseEntity.ok(updated);
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
            WorkSessionResponseTO updated = centralWorkSessionsProxyService.addFaultyProduct(id, body);
            monitoringEventDispatchService.publish(
                    "RECORDED_FAULTY_PRODUCT",
                    updated.getId(),
                    updated.getWorkOrderId(),
                    null,
                    body != null ? body.getRejectReason() : null);
            return ResponseEntity.ok(updated);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(502).body("CENTRAL_WORK_SESSION_UNAVAILABLE");
        }
    }

    @PostMapping("/{id}/setup-products")
    public ResponseEntity<?> setupProducts(
            @PathVariable Long id,
            @RequestBody(required = false) WorkSessionSetupProductCreateTO body) {
        try {
            WorkSessionResponseTO updated = centralWorkSessionsProxyService.addSetupProduct(id, body);
            monitoringEventDispatchService.publish(
                    "RECORDED_SETUP_PRODUCT",
                    updated.getId(),
                    updated.getWorkOrderId(),
                    null,
                    null);
            return ResponseEntity.ok(updated);
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

    @PostMapping("/{id}/help-required")
    public ResponseEntity<?> helpRequired(@PathVariable Long id, @RequestBody(required = false) HelpSignalRequestTO body) {
        try {
            WorkSessionResponseTO session = centralWorkSessionsProxyService.getById(id);
            monitoringEventDispatchService.publish(
                    "HELP_REQUIRED",
                    session.getId(),
                    session.getWorkOrderId(),
                    null,
                    body != null ? body.getDetails() : null);
            return ResponseEntity.ok().build();
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(502).body("CENTRAL_WORK_SESSION_UNAVAILABLE");
        }
    }

    @PostMapping("/{id}/help-resolved")
    public ResponseEntity<?> helpResolved(@PathVariable Long id, @RequestBody(required = false) HelpSignalRequestTO body) {
        try {
            WorkSessionResponseTO session = centralWorkSessionsProxyService.getById(id);
            monitoringEventDispatchService.publish(
                    "HELP_RESOLVED",
                    session.getId(),
                    session.getWorkOrderId(),
                    null,
                    body != null ? body.getDetails() : null);
            return ResponseEntity.ok().build();
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(502).body("CENTRAL_WORK_SESSION_UNAVAILABLE");
        }
    }
}
