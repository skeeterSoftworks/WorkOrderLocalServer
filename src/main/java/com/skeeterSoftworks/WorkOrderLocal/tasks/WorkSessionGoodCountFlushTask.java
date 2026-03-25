package com.skeeterSoftworks.WorkOrderLocal.tasks;

import com.skeeterSoftworks.WorkOrderLocal.service.WorkSessionGoodCountBufferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WorkSessionGoodCountFlushTask {

    private final WorkSessionGoodCountBufferService bufferService;

    @Autowired
    public WorkSessionGoodCountFlushTask(WorkSessionGoodCountBufferService bufferService) {
        this.bufferService = bufferService;
    }

    @Scheduled(fixedDelayString = "${production.work-session.good-product-flush-interval-ms:30000}")
    public void flush() {
        bufferService.flushAllPending();
    }
}
