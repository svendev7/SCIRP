package com.scirp.api.dto;

import java.time.OffsetDateTime;
import java.util.Map;

public class DashboardSnapshotResponse {

    private int totalIncidents;
    private int openIncidents;
    private double averageResolutionMinutes;
    private Map<String, Integer> openByType;
    private OffsetDateTime generatedAt;

    public DashboardSnapshotResponse(int totalIncidents,
                                     int openIncidents,
                                     double averageResolutionMinutes,
                                     Map<String, Integer> openByType,
                                     OffsetDateTime generatedAt) {
        this.totalIncidents = totalIncidents;
        this.openIncidents = openIncidents;
        this.averageResolutionMinutes = averageResolutionMinutes;
        this.openByType = openByType;
        this.generatedAt = generatedAt;
    }

    public int getTotalIncidents() {
        return totalIncidents;
    }

    public int getOpenIncidents() {
        return openIncidents;
    }

    public double getAverageResolutionMinutes() {
        return averageResolutionMinutes;
    }

    public Map<String, Integer> getOpenByType() {
        return openByType;
    }

    public OffsetDateTime getGeneratedAt() {
        return generatedAt;
    }
}
