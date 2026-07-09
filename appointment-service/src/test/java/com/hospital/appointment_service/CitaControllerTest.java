package com.hospital.appointment_service;


//Este test simula la respuesta del servidor

import com.hospital.appointment_service.controller.CitaController;
import com.hospital.appointment_service.dto.CitaDTO;
import com.hospital.appointment_service.model.Cita;
import com.hospital.appointment_service.service.CitaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.when;

@WebMvcTest(CitaController.class)
public class CitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CitaService citaService;

    private Cita mockCita;
    private String citaDtoJson;
    @BeforeEach
    public void setUp() {
        mockCita = new Cita();
        mockCita.setId(1L);
        mockCita.setPacienteId(10L);
        mockCita.setDoctorId(20L);
        mockCita.setFecha(LocalDate.of(2026,12,1));
        mockCita.setHora(LocalTime.of(12,15));
        mockCita.setMotivo("Dolor de Cabeza");
        mockCita.setEstado("Pendiente");

        citaDtoJson = """
            {
                "pacienteId": 10,
                "doctorId": 20,
                "fecha": "2026-12-01",
                "hora": "12:15",
                "motivo": "Dolor de cabeza",
                "estado": "PENDIENTE"
            }
       """;
    }
    //Obtener todas las citas
    @Test
    @WithMockUser
    public void obtenerTodas_DeberiaRetornar200() throws Exception {
        when(citaService.obtenerTodas()).thenReturn(List.of(mockCita));
        mockMvc.perform(get("/api/citas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));


    }
    //Obtener por ID de Cita
    @Test
    @WithMockUser
    void obtenerPorId_DeberiaRetornar200_CuandoExiste() throws Exception {
        when(citaService.obtenerPorId(1L)).thenReturn(Optional.of(mockCita));

        mockMvc.perform(get("/api/citas/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.motivo").value("Dolor de Cabeza"));
    }
    //Obtener por paciente (PacienteId)
    @Test
    @WithMockUser
    void obtenerPorPaciente_DeberiaRetornar200() throws Exception {
        when(citaService.obtenerPorPaciente(10L)).thenReturn(List.of(mockCita));

        mockMvc.perform(get("/api/citas/paciente/{pacienteId}", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pacienteId").value(10L));
    }
    //Crear Cita
    @Test
    @WithMockUser
    void crear_DeberiaRetornar201() throws Exception {
        when(citaService.crear(any(CitaDTO.class))).thenReturn(mockCita);

        mockMvc.perform(post("/api/citas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(citaDtoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }
    //Crear, pero el Doctor ya tiene una cita asignada
    @Test
    @WithMockUser
    void crear_DeberiaRetornar400_CuandoServicioLanzaExcepcion() throws Exception {
        when(citaService.crear(any(CitaDTO.class))).thenThrow(new RuntimeException("El doctor ya tiene una cita en esa fecha y hora"));

        mockMvc.perform(post("/api/citas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(citaDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El doctor ya tiene una cita en esa fecha y hora"));
    }

    //Actualizar Cita
    @Test
    @WithMockUser
    void actualizarEstado_DeberiaRetornar200() throws Exception {
        mockCita.setEstado("CANCELADA");
        when(citaService.actualizarEstado(eq(1L), eq("CANCELADA"))).thenReturn(Optional.of(mockCita));

        mockMvc.perform(patch("/api/citas/{id}/estado", 1L)
                        .param("estado", "CANCELADA")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CANCELADA"));
    }

    //Eliminar Cita
    @Test
    @WithMockUser
    void eliminar_DeberiaRetornar204_CuandoEsExitoso() throws Exception {
        when(citaService.eliminar(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/citas/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
