package com.scirp.domain;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class Incident {
    private final String id;
    private IncidentType type;
    private IncidentStatus status;
    private int priority;
    private String description;
    private String location;
    private String source;
    private String assignedTo;
    private final OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public Incident(
            String id,
            IncidentType type,
            IncidentStatus status,
            int priority,
            String description,
            String location,
            String source,
            String assignedTo,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {
        this.id = Objects.requireNonNullElseGet(id, () -> "INC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        this.type = type;
        this.status = status;
        this.priority = priority;
        this.description = description;
        this.location = location;
        this.source = source;
        this.assignedTo = assignedTo;
        this.createdAt = createdAt != null ? createdAt : OffsetDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : this.createdAt;
    }

    public String getId() {
        return id;
    }

    public IncidentType getType() {
        return type;
    }

    public void setType(IncidentType type) {
        this.type = type;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public void setStatus(IncidentStatus status) {
        this.status = status;
        touch();
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void touch() {
        this.updatedAt = OffsetDateTime.now();
    }
}

