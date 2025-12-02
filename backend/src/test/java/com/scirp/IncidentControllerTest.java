package com.scirp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scirp.api.IncidentController;
import com.scirp.api.dto.CreateIncidentRequest;
import com.scirp.domain.Incident;
import com.scirp.domain.IncidentStatus;
import com.scirp.domain.IncidentType;
import com.scirp.service.IncidentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = IncidentController.class)
class IncidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IncidentService incidentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void list_returnsIncidents() throws Exception {
        Incident inc = new Incident(
                "INC-1", IncidentType.TRAFFIC, IncidentStatus.OPEN, 3,
                "a", "loc", "src", null, null, null
        );
        Mockito.when(incidentService.list(Optional.empty())).thenReturn(List.of(inc));

        mockMvc.perform(get("/api/incidents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void create_returnsCreatedIncident() throws Exception {
        CreateIncidentRequest req = new CreateIncidentRequest();
        req.setType(IncidentType.TRAFFIC);
        req.setPriority(3);
        req.setDescription("test");
        req.setLocation("loc");
        req.setSource("src");

        Incident saved = new Incident(
                "INC-1", IncidentType.TRAFFIC, IncidentStatus.OPEN, 3,
                "test", "loc", "src", null, null, null
        );
        Mockito.when(incidentService.create(Mockito.any())).thenReturn(saved);

        mockMvc.perform(post("/api/incidents")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("INC-1"))
                .andExpect(jsonPath("$.status").value("OPEN"));
    }
}
