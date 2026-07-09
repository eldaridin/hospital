package com.hospital.patient_service;

import com.hospital.patient_service.controller.PacienteController;
import com.hospital.patient_service.dto.PacienteDTO;
import com.hospital.patient_service.model.Paciente;
import com.hospital.patient_service.service.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Respuesta del servidor
@WebMvcTest(PacienteController.class)
public class PacienteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PacienteService pacienteService;
    private Paciente mockPaciente;
    private String pacienteDtoJson;

    @BeforeEach
    public void setup() {
        mockPaciente = new Paciente();
        mockPaciente.setId(1L);
        mockPaciente.setNombre("Juan");
        mockPaciente.setApellido("Perez");
        mockPaciente.setRut("12345678-9");
        mockPaciente.setEmail("juan.perez@email.com");
        mockPaciente.setTelefono("987654321");
        mockPaciente.setFechaNacimiento(LocalDate.of(1990,5,20));

        pacienteDtoJson = """
                {
                    "nombre": "Juan",
                    "apellido": "Perez",
                    "email": "juan.perez@email.com",
                    "rut": "12345678-9",
                    "telefono": "987654321",
                    "fechaNacimiento": "1990-05-20"
                } """;
    }
    //GET Obtener Todos
    @Test
    @WithMockUser
    public void obtenerTodos_DeberiaRetornar200() throws Exception {
        when(pacienteService.obtenerTodos()).thenReturn(List.of(mockPaciente));

        mockMvc.perform(get("/api/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }
    //GET Obtener por ID
    @Test
    @WithMockUser
    void obtenerPorId_DeberiaRetornar200_CuandoExiste() throws Exception {
        when(pacienteService.obtenerPorId(1L)).thenReturn(Optional.of(mockPaciente));

        mockMvc.perform(get("/api/pacientes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan.perez@email.com"));
    }
    //POST Registra un paciente
    @Test
    @WithMockUser
    void crear_DeberiaRetornar201_CuandoEsExitoso() throws Exception {
        when(pacienteService.crear(any(PacienteDTO.class))).thenReturn(mockPaciente);

        mockMvc.perform(post("/api/pacientes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pacienteDtoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }
    //POST Lanza excepcion cuando ya existe
    @Test
    @WithMockUser
    void crear_DeberiaRetornar400_CuandoServicioLanzaExcepcion() throws Exception {
        when(pacienteService.crear(any(PacienteDTO.class)))
                .thenThrow(new RuntimeException("Ya existe un paciente con ese RUT"));

        mockMvc.perform(post("/api/pacientes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pacienteDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ya existe un paciente con ese RUT"));
    }
    //PUT Actualiza un paciente si existe
    @Test
    @WithMockUser
    void actualizar_DeberiaRetornar200_CuandoExiste() throws Exception {
        when(pacienteService.actualizar(eq(1L), any(PacienteDTO.class))).thenReturn(Optional.of(mockPaciente));

        mockMvc.perform(put("/api/pacientes/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(pacienteDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }
    //DELETE Elimina un paciente si existe
    @Test
    @WithMockUser
    void eliminar_DeberiaRetornar204_CuandoEsExitoso() throws Exception {
        when(pacienteService.eliminar(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/pacientes/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
    //DELETE Lanza una excepcion cuando no existe
    @Test
    @WithMockUser
    void eliminar_DeberiaRetornar404_CuandoNoExiste() throws Exception {
        when(pacienteService.eliminar(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/pacientes/{id}", 99L)
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
