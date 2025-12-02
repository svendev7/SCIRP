package com.scirp.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public class SensorEvent {
    private final String id;
    private final String sensorId;
    private final String payload;
    private final String eventType;
    private final OffsetDateTime receivedAt;

    public SensorEvent(String id, String sensorId, String payload, String eventType, OffsetDateTime receivedAt) {
        this.id = id != null ? id : "EVT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.sensorId = sensorId;
        this.payload = payload;
        this.eventType = eventType;
        this.receivedAt = receivedAt != null ? receivedAt : OffsetDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getPayload() {
        return payload;
    }

    public String getEventType() {
        return eventType;
    }

    public OffsetDateTime getReceivedAt() {
        return receivedAt;
    }
}

