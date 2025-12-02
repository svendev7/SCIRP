package com.scirp.api.dto;

import java.time.OffsetDateTime;
import java.util.Map;

public record DashboardSnapshotResponse(
        int totalIncidents,
        int openIncidents,
        double avgResolutionMins,
        Map<String, Integer> openByType,
        OffsetDateTime lastUpdated
) {
}

