package com.scirp.repository;

import com.scirp.domain.Incident;
import com.scirp.domain.IncidentStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class IncidentRepository {

    private final List<Incident> storage = new CopyOnWriteArrayList<>();

    public List<Incident> findAll() {
        return Collections.unmodifiableList(storage);
    }

    public List<Incident> findByStatus(IncidentStatus status) {
        return storage.stream().filter(incident -> incident.getStatus() == status).toList();
    }

    public Optional<Incident> findById(String id) {
        return storage.stream().filter(incident -> incident.getId().equals(id)).findFirst();
    }

    public Incident save(Incident incident) {
        storage.removeIf(existing -> existing.getId().equals(incident.getId()));
        storage.add(incident);
        return incident;
    }

    public void seed(List<Incident> incidents) {
        storage.clear();
        storage.addAll(new ArrayList<>(incidents));
    }
}

