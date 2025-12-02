package com.scirp.api.dto;

import jakarta.validation.constraints.NotBlank;

public class SensorEventRequest {
    @NotBlank
    private String sensorId;
    @NotBlank
    private String payload;
    @NotBlank
    private String eventType;

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}

