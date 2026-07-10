package com.hospital.presciption_service;

import com.hospital.presciption_service.controller.PrescriptionController;
import com.hospital.presciption_service.dto.PrescriptionRequest;
import com.hospital.presciption_service.model.Prescription;
import com.hospital.presciption_service.service.PrescriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PrescriptionController.class)
public class PrescriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrescriptionService service;

    private Prescription prescriptionReal;
    private String requestJson;

    @BeforeEach
    void setUp() {
        prescriptionReal = new Prescription();
        prescriptionReal.setId(1L);
        prescriptionReal.setAppointmentId(10L);
        prescriptionReal.setMedication("Amoxicilina 500mg");
        prescriptionReal.setDosage("1 tableta cada 8 horas");
        prescriptionReal.setDuration("7 días");
        prescriptionReal.setMedicalNotes("Tomar después de las comidas");

        requestJson = """
            {
                "appointmentId": 10,
                "medication": "Amoxicilina 500mg",
                "dosage": "1 tableta cada 8 horas",
                "duration": "7 días",
                "medicalNotes": "Tomar después de las comidas"
            }
        """;
    }

    @Test
    @WithMockUser
    void create_DeberiaRetornar201_CuandoDatosSonValidos() throws Exception {
        when(service.createPrescription(any(PrescriptionRequest.class))).thenReturn(prescriptionReal);

        mockMvc.perform(post("/api/prescriptions")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.medication").value("Amoxicilina 500mg"));
    }

    @Test
    @WithMockUser
    void testEndpoint_DeberiaRetornarString() throws Exception {
        mockMvc.perform(get("/api/prescriptions/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Servicio Prescription funcionando"));
    }

    @Test
    @WithMockUser
    void getAll_DeberiaRetornar200YLista() throws Exception {
        when(service.findAll()).thenReturn(List.of(prescriptionReal));

        mockMvc.perform(get("/api/prescriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].medication").value("Amoxicilina 500mg"));
    }

    @Test
    @WithMockUser
    void getById_DeberiaRetornar200YPrescription() throws Exception {
        when(service.getById(1L)).thenReturn(prescriptionReal);

        mockMvc.perform(get("/api/prescriptions/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser
    void getByAppointment_DeberiaRetornar200YLista() throws Exception {
        when(service.getByAppointmentId(10L)).thenReturn(List.of(prescriptionReal));

        mockMvc.perform(get("/api/prescriptions/appointment/{appointmentId}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].appointmentId").value(10L));
    }

    @Test
    @WithMockUser
    void delete_DeberiaRetornar204() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/prescriptions/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
