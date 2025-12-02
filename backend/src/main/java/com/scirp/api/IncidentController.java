package com.scirp.api;

import com.scirp.api.dto.CreateIncidentRequest;
import com.scirp.api.dto.IncidentResponse;
import com.scirp.api.dto.UpdateIncidentStatusRequest;
import com.scirp.domain.IncidentStatus;
import com.scirp.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/incidents")
@CrossOrigin(origins = "*")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping
    public List<IncidentResponse> list(@RequestParam(required = false) IncidentStatus status) {
        return incidentService.list(Optional.ofNullable(status)).stream().map(IncidentResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponse> getById(@PathVariable String id) {
        return incidentService.get(id).map(IncidentResponse::from).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<IncidentResponse> create(@Valid @RequestBody CreateIncidentRequest request) {
        return ResponseEntity.ok(IncidentResponse.from(incidentService.create(request)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<IncidentResponse> updateStatus(@PathVariable String id, @Valid @RequestBody UpdateIncidentStatusRequest request) {
        IncidentStatus newStatus = request.getStatus();
        return ResponseEntity.ok(IncidentResponse.from(incidentService.updateStatus(id, newStatus, request.getAssignedTo())));
    }
}

