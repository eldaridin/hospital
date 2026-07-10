package com.hospital.hospitalization;

import com.hospital.hospitalization.config.JwtTokenProvider;
import com.hospital.hospitalization.controller.HospitalizationController;
import com.hospital.hospitalization.dto.HospitalizationDTO;
import com.hospital.hospitalization.model.Hospitalization;
import com.hospital.hospitalization.service.HospitalizationService;
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
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HospitalizationController.class)
public class HospitalizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private HospitalizationService service;

    private Hospitalization hospitalizationReal;
    private String admissionJson;

    @BeforeEach
    void setUp() {
        hospitalizationReal = new Hospitalization();
        hospitalizationReal.setId(1L);
        hospitalizationReal.setPatientId(100L);
        hospitalizationReal.setDoctorId(200L);
        hospitalizationReal.setRoomNumber("101A");
        hospitalizationReal.setDiagnosis("Neumonía");
        hospitalizationReal.setStatus("ADMITTED");

        admissionJson = """
            {
                "patientId": 100,
                "doctorId": 200,
                "roomNumber": "101A",
                "diagnosis": "Neumonía"
            }
        """;
    }

    @Test
    @WithMockUser
    void admitPatient_DeberiaRetornar201_CuandoEsExitoso() throws Exception {
        when(service.registerAdmissionFromDTO(any(HospitalizationDTO.class))).thenReturn(hospitalizationReal);

        mockMvc.perform(post("/api/v1/hospitalizations/admission")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(admissionJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ADMITTED"))
                .andExpect(jsonPath("$.roomNumber").value("101A"));
    }

    @Test
    @WithMockUser
    void dischargePatient_DeberiaRetornar200_CuandoEsExitoso() throws Exception {
        hospitalizationReal.setStatus("DISCHARGED");
        when(service.processDischarge(1L)).thenReturn(hospitalizationReal);

        mockMvc.perform(put("/api/v1/hospitalizations/{id}/discharge", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DISCHARGED"));
    }

    @Test
    @WithMockUser
    void getActive_DeberiaRetornar200YListaDeHospitalizaciones() throws Exception {
        when(service.getActiveHospitalizations()).thenReturn(List.of(hospitalizationReal));

        mockMvc.perform(get("/api/v1/hospitalizations/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ADMITTED"))
                .andExpect(jsonPath("$[0].diagnosis").value("Neumonía"));
    }

    @Test
    @WithMockUser
    void getHistory_DeberiaRetornar200YListaPorPaciente() throws Exception {
        when(service.getPatientHistory(100L)).thenReturn(List.of(hospitalizationReal));

        mockMvc.perform(get("/api/v1/hospitalizations/patient/{patientId}", 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientId").value(100))
                .andExpect(jsonPath("$[0].roomNumber").value("101A"));
    }
}
