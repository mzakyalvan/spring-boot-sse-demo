package com.tiket.tix.poc.sse.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

/**
 * @author zakyalvan
 */
@Getter
public class MemoryInfo {
    private final long usedHeap;
    private final long usedNonHeap;
    private final Date timestamp;

    @Builder
    protected MemoryInfo(long usedHeap, long usedNonHeap) {
        this.usedHeap = usedHeap;
        this.usedNonHeap = usedNonHeap;
        this.timestamp = new Date();
    }
}
