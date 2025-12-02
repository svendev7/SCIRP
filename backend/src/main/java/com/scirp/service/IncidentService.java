package com.scirp.service;

import com.scirp.api.dto.CreateIncidentRequest;
import com.scirp.domain.Incident;
import com.scirp.domain.IncidentStatus;
import com.scirp.domain.IncidentType;
import com.scirp.repository.IncidentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class IncidentService {

    private final IncidentRepository repository;

    public IncidentService(IncidentRepository repository) {
        this.repository = repository;
    }

    public List<Incident> list(Optional<IncidentStatus> status) {
        return status.map(repository::findByStatus).orElseGet(repository::findAll);
    }

    public Optional<Incident> get(String id) {
        return repository.findById(id);
    }

    public Incident create(CreateIncidentRequest request) {
        Incident incident = new Incident(
                null,
                request.getType(),
                IncidentStatus.OPEN,
                request.getPriority(),
                request.getDescription(),
                request.getLocation(),
                request.getSource(),
                null,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
        return repository.save(incident);
    }

    public Incident updateStatus(String id, IncidentStatus status, String assignedTo) {
        Incident incident = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Incident not found"));
        incident.setStatus(status);
        if (StringUtils.hasText(assignedTo)) {
            incident.setAssignedTo(assignedTo);
        }
        incident.touch();
        return repository.save(incident);
    }

    public Incident assignTo(String id, String userId) {
        Incident incident = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Incident not found"));
        incident.setAssignedTo(userId);
        incident.touch();
        return repository.save(incident);
    }

    public Incident fromSensorEvent(IncidentType type, String description, String location, String source, int priority) {
        Incident incident = new Incident(
                null,
                type,
                IncidentStatus.OPEN,
                priority,
                description,
                location,
                source,
                null,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
        return repository.save(incident);
    }
}

