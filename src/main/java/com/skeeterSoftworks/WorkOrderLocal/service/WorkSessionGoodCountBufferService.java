package com.skeeterSoftworks.WorkOrderLocal.service;

import com.skeeterSoftworks.WorkOrderLocal.to.objects.ProductCountDeltaRequestTO;
import com.skeeterSoftworks.WorkOrderLocal.to.objects.WorkSessionResponseTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Buffers good-product count deltas before periodic flush to central.
 */
@Slf4j
@Service
public class WorkSessionGoodCountBufferService {

    private final CentralWorkSessionsProxyService centralWorkSessionsProxyService;

    private final ConcurrentHashMap<Long, Long> pendingDeltaBySession = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, String> lastProductReferenceBySession = new ConcurrentHashMap<>();

    @Autowired
    public WorkSessionGoodCountBufferService(CentralWorkSessionsProxyService centralWorkSessionsProxyService) {
        this.centralWorkSessionsProxyService = centralWorkSessionsProxyService;
    }

    public void recordDelta(long sessionId, long delta, String productReferenceId) {
        if (delta <= 0) {
            return;
        }
        pendingDeltaBySession.merge(sessionId, delta, Long::sum);
        if (productReferenceId != null && !productReferenceId.isBlank()) {
            lastProductReferenceBySession.put(sessionId, productReferenceId.trim());
        }
    }

    /**
     * Flushes all pending counts to central. Restores pending amounts on failure for retry.
     */
    public void flushAllPending() {
        for (Long sessionId : pendingDeltaBySession.keySet()) {
            flushSession(sessionId);
        }
    }

    /**
     * Sends all pending good count for the session to central. Returns central's updated session, or {@code null} if there was nothing pending.
     */
    public WorkSessionResponseTO flushSessionReturningUpdated(long sessionId) {
        Long delta = pendingDeltaBySession.remove(sessionId);
        if (delta == null || delta <= 0) {
            return null;
        }
        String ref = lastProductReferenceBySession.get(sessionId);
        ProductCountDeltaRequestTO req = new ProductCountDeltaRequestTO();
        req.setDelta(delta);
        req.setProductReferenceID(ref);
        try {
            return centralWorkSessionsProxyService.incrementProductCount(sessionId, req);
        } catch (Exception e) {
            log.warn("Failed to flush good count for session {}: {}", sessionId, e.getMessage());
            pendingDeltaBySession.merge(sessionId, delta, Long::sum);
            throw e;
        }
    }

    public void flushSession(long sessionId) {
        try {
            flushSessionReturningUpdated(sessionId);
        } catch (Exception e) {
            // Caller (e.g. end session) already logs; pending restored inside flushSessionReturningUpdated
        }
    }

    public void clearSession(long sessionId) {
        pendingDeltaBySession.remove(sessionId);
        lastProductReferenceBySession.remove(sessionId);
    }
}
