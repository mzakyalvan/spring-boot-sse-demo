package com.tiket.tix.poc.sse.controller;

import com.tiket.tix.poc.sse.model.MemoryInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author zakyalvan
 */
@RestController
@RequestMapping("/memory")
public class MemoryInfoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemoryInfoController.class);

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping(value = "/usages", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    SseEmitter memoryUsages() {
        SseEmitter emitter = new SseEmitter();
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitters.add(emitter);
        return emitter;
    }

    @EventListener
    public void handleEvents(MemoryInfo memoryInfo) {
        LOGGER.info("Handle memory info event");

        List<SseEmitter> dead = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                        .id(UUID.randomUUID().toString())
                        .data(memoryInfo, MediaType.APPLICATION_JSON_UTF8)
                        .name("memory-usage")
                        .build());
            }
            catch (IOException ioe) {
                LOGGER.error("Error on emitting data", ioe);
                dead.add(emitter);
            }
        });
        emitters.removeAll(dead);
    }
}
