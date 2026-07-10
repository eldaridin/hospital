package com.hospital.appointment_service;


import com.hospital.appointment_service.client.DoctorClient;
import com.hospital.appointment_service.client.PacienteClient;
import com.hospital.appointment_service.dto.CitaDTO;
import com.hospital.appointment_service.dto.DoctorResponseDTO;
import com.hospital.appointment_service.dto.PacienteResponseDTO;
import com.hospital.appointment_service.model.Cita;
import com.hospital.appointment_service.repository.CitaRepository;
import com.hospital.appointment_service.service.CitaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//Este test es para la logica del microservicio
@ExtendWith(MockitoExtension.class)
public class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    PacienteClient pacienteClient;

    @Mock
    DoctorClient doctorClient;

    @InjectMocks
    private CitaService citaService;

    private Cita mockCita;
    private CitaDTO mockCitaDTO;

    @BeforeEach
    public void setUp() {
        mockCita = new Cita();
        mockCita.setId(1L);
        mockCita.setPacienteId(10L);
        mockCita.setDoctorId(20L);
        mockCita.setFecha(LocalDate.now());
        mockCita.setHora(LocalTime.now());
        mockCita.setMotivo("Dolor de cabeza");
        mockCita.setEstado("Pendiente");


        mockCitaDTO = mock(CitaDTO.class);
    }
    //Obtiene todas las Citas
    @Test
    public void obtenerTodas_DeberiaRetornarLista(){
        when(citaRepository.findAll()).thenReturn(List.of(mockCita));

        List<Cita> resultado = citaService.obtenerTodas();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(citaRepository, times(1)).findAll();

    }
    //Busca por ID si es que existe
    @Test
    public void obtenerPorId_DeberiaRetornarCita_CuandoExiste(){
        when(citaRepository.findById(1L)).thenReturn(Optional.of(mockCita));

        Optional<Cita> resultado = citaService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L,resultado.get().getId());
    }
    //Crea y guarda una cita si los datos son VALIDOS
    @Test
    public void crear_DeberiaGuardarCita_CuandoDatosSonValidos(){
        when(mockCitaDTO.getPacienteId()).thenReturn(10L);
        when(mockCitaDTO.getDoctorId()).thenReturn(20L);
        when(mockCitaDTO.getFecha()).thenReturn(String.valueOf(LocalDate.now()));
        when(mockCitaDTO.getHora()).thenReturn(String.valueOf(LocalTime.now()));
        when(mockCitaDTO.getMotivo()).thenReturn("Dolor de cabeza");
        when(mockCitaDTO.getEstado()).thenReturn("Pendiente");

        PacienteResponseDTO pacienteMock = new PacienteResponseDTO();
        pacienteMock.setId(10L);
        DoctorResponseDTO doctorMock = new DoctorResponseDTO();
        doctorMock.setId(20L);

        when(pacienteClient.obtenerPacientePorId(10L)).thenReturn(pacienteMock);
        when(doctorClient.obtenerDoctorPorId(20L)).thenReturn(doctorMock);

        when(citaRepository.existsByDoctorIdAndFechaAndHora(eq(20L), any(LocalDate.class), any(LocalTime.class))).thenReturn(false);
        when(citaRepository.save(any(Cita.class))).thenReturn(mockCita);

        Cita resultado = citaService.crear(mockCitaDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(citaRepository, times(1)).save(any(Cita.class));
    }
    //Si el paciente no existe
    @Test
    public void crear_DeberiaLanzarExcepcion_CuandoPacienteNoExiste() {
        when(mockCitaDTO.getPacienteId()).thenReturn(99L);

        when(pacienteClient.obtenerPacientePorId(99L)).thenThrow(new RuntimeException("Not Found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            citaService.crear(mockCitaDTO);
        });

        assertTrue(exception.getMessage().contains("No se pudo verificar el paciente"));
        verify(citaRepository, never()).save(any());
    }
    //Si el horario esta ocupado
    @Test
    public void crear_DeberiaLanzarExcepcion_CuandoHorarioOcupado() {
        when(mockCitaDTO.getPacienteId()).thenReturn(10L);
        when(mockCitaDTO.getDoctorId()).thenReturn(20L);
        when(mockCitaDTO.getFecha()).thenReturn("2026-12-01");
        when(mockCitaDTO.getHora()).thenReturn("10:30");

        PacienteResponseDTO pacienteMock = new PacienteResponseDTO();
        pacienteMock.setId(10L);
        DoctorResponseDTO doctorMock = new DoctorResponseDTO();
        doctorMock.setId(20L);

        when(pacienteClient.obtenerPacientePorId(10L)).thenReturn(pacienteMock);
        when(doctorClient.obtenerDoctorPorId(20L)).thenReturn(doctorMock);

        when(citaRepository.existsByDoctorIdAndFechaAndHora(eq(20L), any(LocalDate.class), any(LocalTime.class))).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            citaService.crear(mockCitaDTO);
        });

        assertEquals("El doctor ya tiene una cita en esa fecha y hora", exception.getMessage());
        verify(citaRepository, never()).save(any());
    }

    @Test
    //Actualizar si existe
    public void actualizarEstado_DeberiaCambiarEstado_CuandoCitaExiste() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(mockCita));
        when(citaRepository.save(any(Cita.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Cita> resultado = citaService.actualizarEstado(1L, "COMPLETADA");

        assertTrue(resultado.isPresent());
        assertEquals("COMPLETADA", resultado.get().getEstado());
    }
    //Eliminar si existe
    @Test
    public void eliminar_DeberiaRetornarTrue_CuandoCitaExiste() {
        when(citaRepository.existsById(1L)).thenReturn(true);

        boolean resultado = citaService.eliminar(1L);

        assertTrue(resultado);
        verify(citaRepository, times(1)).deleteById(1L);
    }

}
