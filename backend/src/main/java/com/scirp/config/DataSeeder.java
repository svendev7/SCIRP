package com.scirp.config;

import com.scirp.domain.Incident;
import com.scirp.domain.IncidentStatus;
import com.scirp.domain.IncidentType;
import com.scirp.domain.SensorEvent;
import com.scirp.domain.User;
import com.scirp.domain.UserRole;
import com.scirp.repository.IncidentRepository;
import com.scirp.repository.SensorEventRepository;
import com.scirp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.HexFormat;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final IncidentRepository incidentRepository;
    private final SensorEventRepository sensorEventRepository;
    private final UserRepository userRepository;

    public DataSeeder(IncidentRepository incidentRepository, SensorEventRepository sensorEventRepository, UserRepository userRepository) {
        this.incidentRepository = incidentRepository;
        this.sensorEventRepository = sensorEventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        incidentRepository.seed(List.of(
                new Incident("INC-1001", IncidentType.TRAFFIC, IncidentStatus.OPEN, 4, "Congestie op Erasmusbrug", "Rotterdam", "CAM-23", "traffic-ops",
                        OffsetDateTime.now().minusMinutes(20), OffsetDateTime.now().minusMinutes(5)),
                new Incident("INC-1002", IncidentType.IOT_FAILURE, IncidentStatus.IN_PROGRESS, 3, "Sensor AQ-44 offline", "Utrecht", "AQ-44", "iot-team",
                        OffsetDateTime.now().minusHours(1), OffsetDateTime.now().minusMinutes(10)),
                new Incident("INC-1003", IncidentType.CYBER_ATTACK, IncidentStatus.RESOLVED, 5, "IDS blokkeerde IP", "Cloud EU", "SIEM", "secops",
                        OffsetDateTime.now().minusHours(4), OffsetDateTime.now().minusHours(2))
        ));

        sensorEventRepository.seed(List.of(
                new SensorEvent("EVT-2001", "CAM-RDM-23", "vehicle_count=120", "TRAFFIC", OffsetDateTime.now().minusMinutes(2)),
                new SensorEvent("EVT-2002", "AQ-44", "signal_lost", "IOT_FAILURE", OffsetDateTime.now().minusMinutes(4)),
                new SensorEvent("EVT-2003", "IDS-GW-3", "blocked_ip=185.23.11.4", "CYBER_ATTACK", OffsetDateTime.now().minusMinutes(8))
        ));

        userRepository.seed(List.of(
                new User("usr-ops", "operator", UserRole.OPERATOR, hash("operator")),
                new User("usr-admin", "admin", UserRole.ADMIN, hash("admin")),
                new User("usr-analyst", "analyst", UserRole.ANALYST, hash("analyst"))
        ));
    }

    private String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Hash algorithm missing", e);
        }
    }
}

