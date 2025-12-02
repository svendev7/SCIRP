package com.scirp;

import com.scirp.api.dto.CreateIncidentRequest;
import com.scirp.domain.Incident;
import com.scirp.domain.IncidentStatus;
import com.scirp.domain.IncidentType;
import com.scirp.repository.IncidentRepository;
import com.scirp.service.IncidentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class IncidentServiceTest {

    private IncidentRepository repository;
    private IncidentService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(IncidentRepository.class);
        service = new IncidentService(repository);
    }

    @Test
    void create_setsOpenStatusAndSaves() {
        CreateIncidentRequest req = new CreateIncidentRequest();
        req.setType(IncidentType.TRAFFIC);
        req.setPriority(3);
        req.setDescription("Test");
        req.setLocation("Utrecht");
        req.setSource("MANUAL");

        Mockito.when(repository.save(any(Incident.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Incident result = service.create(req);

        assertThat(result.getStatus()).isEqualTo(IncidentStatus.OPEN);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    void list_withoutStatus_returnsAll() {
        Mockito.when(repository.findAll()).thenReturn(List.of(
                new Incident(null, IncidentType.TRAFFIC, IncidentStatus.OPEN, 3,
                        "a", "loc", "src", null, null, null)
        ));

        var result = service.list(Optional.empty());
        assertThat(result).hasSize(1);
    }

    @Test
    void updateStatus_throwsWhenNotFound() {
        Mockito.when(repository.findById("INC-404")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
                () -> service.updateStatus("INC-404", IncidentStatus.IN_PROGRESS, null));
    }

    @Test
    void updateStatus_updatesStatusAndAssignedTo() {
        Incident existing = new Incident(
                "INC-1", IncidentType.TRAFFIC, IncidentStatus.OPEN, 3,
                "a", "loc", "src", null, null, null
        );
        Mockito.when(repository.findById("INC-1")).thenReturn(Optional.of(existing));
        Mockito.when(repository.save(any(Incident.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Incident updated = service.updateStatus("INC-1", IncidentStatus.IN_PROGRESS, "usr-ops");

        assertThat(updated.getStatus()).isEqualTo(IncidentStatus.IN_PROGRESS);
        assertThat(updated.getAssignedTo()).isEqualTo("usr-ops");
    }
}
