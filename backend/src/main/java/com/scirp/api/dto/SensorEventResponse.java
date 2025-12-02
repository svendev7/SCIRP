package com.scirp.api.dto;

import com.scirp.domain.SensorEvent;

import java.time.OffsetDateTime;

public record SensorEventResponse(
        String id,
        String sensorId,
        String payload,
        String eventType,
        OffsetDateTime receivedAt
) {
    public static SensorEventResponse from(SensorEvent event) {
        return new SensorEventResponse(
                event.getId(),
                event.getSensorId(),
                event.getPayload(),
                event.getEventType(),
                event.getReceivedAt()
        );
    }
}

