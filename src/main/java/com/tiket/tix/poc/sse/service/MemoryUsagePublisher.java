package com.tiket.tix.poc.sse.service;

import com.tiket.tix.poc.sse.model.MemoryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * @author zakyalvan
 */
@Component
public class MemoryUsagePublisher implements ApplicationEventPublisherAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryUsagePublisher.class);

    private ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedDelay = 1000)
    void memoryUsage() {
        LOGGER.info("Publish memory usage event");
        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = memBean.getHeapMemoryUsage();
        MemoryUsage nonHeap = memBean.getNonHeapMemoryUsage();

        MemoryInfo info = MemoryInfo.builder()
                .usedHeap(heap.getUsed())
                .usedNonHeap(nonHeap.getUsed())
                .build();
        eventPublisher.publishEvent(info);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
