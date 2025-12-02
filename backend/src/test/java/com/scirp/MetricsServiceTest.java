package com.scirp;

import com.scirp.api.dto.DashboardSnapshotResponse;
import com.scirp.domain.*;
import com.scirp.repository.IncidentRepository;
import com.scirp.service.MetricsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MetricsServiceTest {

    @Test
    void snapshot_calculatesOpenAndAverageResolution() {
        IncidentRepository repo = Mockito.mock(IncidentRepository.class);

        Incident open = new Incident(
                "INC-1", IncidentType.TRAFFIC, IncidentStatus.OPEN, 3,
                "a", "loc", "src", null,
                OffsetDateTime.now().minusMinutes(30),
                OffsetDateTime.now().minusMinutes(10)
        );
        Incident resolved = new Incident(
                "INC-2", IncidentType.CYBER_ATTACK, IncidentStatus.RESOLVED, 5,
                "b", "loc", "src", null,
                OffsetDateTime.now().minusHours(2),
                OffsetDateTime.now().minusHours(1)
        );

        Mockito.when(repo.findAll()).thenReturn(List.of(open, resolved));

        MetricsService metricsService = new MetricsService(repo);
        DashboardSnapshotResponse snap = metricsService.snapshot();

        assertThat(snap.getTotalIncidents()).isEqualTo(2);
        assertThat(snap.getOpenIncidents()).isEqualTo(1);
        assertThat(snap.getAverageResolutionMinutes()).isGreaterThan(0);
        assertThat(snap.getOpenByType().get("TRAFFIC")).isEqualTo(1);
    }
}
