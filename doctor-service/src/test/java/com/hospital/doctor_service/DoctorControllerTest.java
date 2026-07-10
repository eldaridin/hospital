package com.hospital.doctor_service;


import com.hospital.doctor_service.controller.DoctorController;
import com.hospital.doctor_service.dto.DoctorDTO;
import com.hospital.doctor_service.model.Doctor;
import com.hospital.doctor_service.service.DoctorService;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;
    private Doctor mockDoctor;
    private String doctorJson;

    @BeforeEach
    public void setup() {
        mockDoctor = new Doctor();
        mockDoctor.setId(1L);
        mockDoctor.setNombre("Gregory");
        mockDoctor.setApellido("House");
        mockDoctor.setEmail("ghouse@hospital.com");
        mockDoctor.setTelefono("555-1234");
        mockDoctor.setEspecialidad("Diagnostico");
        mockDoctor.setRut("98765432-1");
        mockDoctor.setDisponible(true);

        doctorJson = """
                        {
                            "nombre": "Gregory",
                            "apellido": "House",
                            "email": "ghouse@hospital.com",
                            "telefono": "555-1234",
                            "especialidad": "Diagnostico",
                            "rut": "98765432-1",
                            "disponible": "true"
                        }
                     """;

    }
    // GET Obtiene todos los doctores
    @Test
    @WithMockUser
    public void obtenerTodosDeberiaRetornar200() throws Exception {
        when(doctorService.obtenerTodos()).thenReturn(List.of(mockDoctor));

        mockMvc.perform(get("/api/doctores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Gregory"));
    }
    //GET Obtiene doctores por su ID
    @Test
    @WithMockUser
    public void obtenerPorIdDeberiaRetornar200CuandoExiste() throws Exception {
        when(doctorService.obtenerPorId(1L)).thenReturn(Optional.of(mockDoctor));

        mockMvc.perform(get("/api/doctores/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ghouse@hospital.com"));
    }
    //GET Obtiene los doctores listados por especialidad
    @Test
    @WithMockUser
    void obtenerPorEspecialidadDeberiaRetornar200() throws Exception {
        when(doctorService.obtenerPorEspecialidad("Diagnostico")).thenReturn(List.of(mockDoctor));

        mockMvc.perform(get("/api/doctores/especialidad/{especialidad}", "Diagnostico"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].especialidad").value("Diagnostico"));
    }
    //GET Obtiene los doctores disponibles
    @Test
    @WithMockUser
    void obtenerDisponiblesDeberiaRetornar200() throws Exception {
        when(doctorService.obtenerDisponibles()).thenReturn(List.of(mockDoctor));

        mockMvc.perform(get("/api/doctores/disponibles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].disponible").value(true));
    }
    //POST Guarda un doctor si los datos son validos
    @Test
    @WithMockUser
    void crear_DeberiaRetornar201() throws Exception {
        when(doctorService.crear(any(DoctorDTO.class))).thenReturn(mockDoctor);

        mockMvc.perform(post("/api/doctores")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(doctorJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rut").value("98765432-1"));
    }
    //POST Excepcion cuando el rut ya existe
    @Test
    @WithMockUser
    void crear_DeberiaRetornar400() throws Exception {
        when(doctorService.crear(any(DoctorDTO.class)))
                .thenThrow(new RuntimeException("Ya existe un doctor con ese RUT"));

        mockMvc.perform(post("/api/doctores")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(doctorJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Ya existe un doctor con ese RUT"));
    }
    //PUT Actualiza un doctor
    @Test
    @WithMockUser
    void actualizar_DeberiaRetornar200() throws Exception {
        when(doctorService.actualizar(eq(1L), any(DoctorDTO.class))).thenReturn(Optional.of(mockDoctor));

        mockMvc.perform(put("/api/doctores/{id}", 1L)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(doctorJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Gregory"));
    }
    //DELETE Elimina un doctor
    @Test
    @WithMockUser
    void eliminar_DeberiaRetornar204() throws Exception {
        when(doctorService.eliminar(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/doctores/{id}", 1L)
                        .with(csrf()))
                        .andExpect(status().isNoContent());
    }
    //DELETE Lanza una excepcion cuando no existe
    @Test
    @WithMockUser
    void eliminar_DeberiaRetornar404_CuandoNoExiste() throws Exception {
        when(doctorService.eliminar(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/doctores/{id}", 99L)
                        .with(csrf()))
                        .andExpect(status().isNotFound());
    }
}
