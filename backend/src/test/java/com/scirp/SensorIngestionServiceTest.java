package com.scirp;

import com.scirp.api.dto.SensorEventRequest;
import com.scirp.domain.SensorEvent;
import com.scirp.repository.SensorEventRepository;
import com.scirp.service.IncidentService;
import com.scirp.service.SensorIngestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class SensorIngestionServiceTest {

    private SensorEventRepository eventRepository;
    private IncidentService incidentService;
    private SensorIngestionService ingestionService;

    @BeforeEach
    void setUp() {
        eventRepository = Mockito.mock(SensorEventRepository.class);
        incidentService = Mockito.mock(IncidentService.class);
        ingestionService = new SensorIngestionService(eventRepository, incidentService);

        Mockito.when(eventRepository.save(any(SensorEvent.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void ingestEvent_savesEventAndCreatesIncident() {
        SensorEventRequest req = new SensorEventRequest();
        req.setSensorId("CAM-1");
        req.setPayload("payload");
        req.setEventType("TRAFFIC");

        SensorEvent result = ingestionService.ingestEvent(req);

        assertThat(result.getId()).isNotNull();
        Mockito.verify(eventRepository).save(any(SensorEvent.class));
        Mockito.verify(incidentService)
                .fromSensorEvent(any(), any(), any(), any(), any(Integer.class));
    }
}
