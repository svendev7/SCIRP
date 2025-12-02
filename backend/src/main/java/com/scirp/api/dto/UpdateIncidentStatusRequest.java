package com.scirp.api.dto;

import com.scirp.domain.IncidentStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateIncidentStatusRequest {
    @NotNull
    private IncidentStatus status;
    private String assignedTo;

    public IncidentStatus getStatus() {
        return status;
    }

    public void setStatus(IncidentStatus status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}

