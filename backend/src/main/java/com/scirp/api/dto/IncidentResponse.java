package com.scirp.api.dto;

import com.scirp.domain.Incident;
import com.scirp.domain.IncidentStatus;
import com.scirp.domain.IncidentType;

import java.time.OffsetDateTime;

public record IncidentResponse(
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
    public static IncidentResponse from(Incident incident) {
        return new IncidentResponse(
                incident.getId(),
                incident.getType(),
                incident.getStatus(),
                incident.getPriority(),
                incident.getDescription(),
                incident.getLocation(),
                incident.getSource(),
                incident.getAssignedTo(),
                incident.getCreatedAt(),
                incident.getUpdatedAt()
        );
    }
}

