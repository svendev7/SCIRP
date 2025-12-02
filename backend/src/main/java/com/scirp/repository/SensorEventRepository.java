package com.scirp.repository;

import com.scirp.domain.SensorEvent;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class SensorEventRepository {
    private final List<SensorEvent> events = new CopyOnWriteArrayList<>();

    public List<SensorEvent> findAll() {
        return Collections.unmodifiableList(events);
    }

    public SensorEvent save(SensorEvent event) {
        events.add(event);
        return event;
    }

    public void seed(List<SensorEvent> seedData) {
        events.clear();
        events.addAll(seedData);
    }
}

