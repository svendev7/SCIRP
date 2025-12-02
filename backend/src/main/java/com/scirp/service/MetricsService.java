package com.scirp.service;

import com.scirp.api.dto.DashboardSnapshotResponse;
import com.scirp.domain.Incident;
import com.scirp.domain.IncidentStatus;
import com.scirp.domain.IncidentType;
import com.scirp.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MetricsService {

    private final IncidentRepository incidentRepository;

    public MetricsService(IncidentRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public DashboardSnapshotResponse snapshot() {
        var incidents = incidentRepository.findAll();
        Map<IncidentType, Integer> openByType = new EnumMap<>(IncidentType.class);
        for (IncidentType type : IncidentType.values()) {
            openByType.put(type, 0);
        }

        int openCount = 0;
        long totalResolution = 0;
        long resolvedCount = 0;

        for (Incident incident : incidents) {
            if (incident.getStatus() == IncidentStatus.RESOLVED || incident.getStatus() == IncidentStatus.CLOSED) {
                resolvedCount++;
                Duration duration = Duration.between(incident.getCreatedAt(), incident.getUpdatedAt());
                totalResolution += duration.toMinutes();
            } else {
                openCount++;
                openByType.computeIfPresent(incident.getType(), (k, v) -> v + 1);
            }
        }

        double avgResolution = resolvedCount == 0 ? 0 : (double) totalResolution / resolvedCount;

        return new DashboardSnapshotResponse(
                incidents.size(),
                openCount,
                avgResolution,
                openByType.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey().name(), Map.Entry::getValue)),
                OffsetDateTime.now()
        );
    }
}

