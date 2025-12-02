package com.scirp.service;

import com.scirp.api.dto.SensorEventRequest;
import com.scirp.domain.IncidentType;
import com.scirp.domain.SensorEvent;
import com.scirp.repository.SensorEventRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Locale;

@Service
public class SensorIngestionService {

    private final SensorEventRepository sensorEventRepository;
    private final IncidentService incidentService;

    public SensorIngestionService(SensorEventRepository sensorEventRepository, IncidentService incidentService) {
        this.sensorEventRepository = sensorEventRepository;
        this.incidentService = incidentService;
    }

    public SensorEvent ingestEvent(SensorEventRequest dto) {
        SensorEvent event = new SensorEvent(
                null,
                dto.getSensorId(),
                dto.getPayload(),
                dto.getEventType(),
                OffsetDateTime.now()
        );
        sensorEventRepository.save(event);

        // simplistic business logic to illustrate ingestion -> incident
        IncidentType type = mapEventType(dto.getEventType());
        String description = "Auto-generated via sensor " + dto.getSensorId();
        String location = "Virtual Zone";
        incidentService.fromSensorEvent(type, description, location, dto.getSensorId(), type == IncidentType.CYBER_ATTACK ? 5 : 3);
        return event;
    }

    public java.util.List<SensorEvent> listEvents() {
        return sensorEventRepository.findAll();
    }

    private IncidentType mapEventType(String incoming) {
        String normalized = incoming.toUpperCase(Locale.ROOT);
        return switch (normalized) {
            case "TRAFFIC" -> IncidentType.TRAFFIC;
            case "IOT_FAILURE" -> IncidentType.IOT_FAILURE;
            case "CYBER", "CYBER_ATTACK" -> IncidentType.CYBER_ATTACK;
            default -> IncidentType.OTHER;
        };
    }
}

