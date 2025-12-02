package com.scirp.api;

import com.scirp.api.dto.SensorEventRequest;
import com.scirp.api.dto.SensorEventResponse;
import com.scirp.service.SensorIngestionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@CrossOrigin(origins = "*")
public class SensorController {

    private final SensorIngestionService ingestionService;

    public SensorController(SensorIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/events")
    public ResponseEntity<SensorEventResponse> ingest(@Valid @RequestBody SensorEventRequest request) {
        return ResponseEntity.ok(SensorEventResponse.from(ingestionService.ingestEvent(request)));
    }

    @GetMapping("/events")
    public List<SensorEventResponse> list() {
        return ingestionService.listEvents().stream().map(SensorEventResponse::from).toList();
    }
}

