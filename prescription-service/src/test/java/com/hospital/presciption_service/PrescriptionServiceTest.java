package com.hospital.presciption_service;

import com.hospital.presciption_service.dto.PrescriptionRequest;
import com.hospital.presciption_service.exception.ResourceNotFoundException;
import com.hospital.presciption_service.model.Prescription;
import com.hospital.presciption_service.repository.PrescriptionRepository;
import com.hospital.presciption_service.service.PrescriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrescriptionServiceTest {

    @Mock
    private PrescriptionRepository repository;

    @InjectMocks
    private PrescriptionService service;

    private Prescription prescriptionReal;
    private PrescriptionRequest requestDto;

    @BeforeEach
    void setUp() {
        prescriptionReal = new Prescription();
        prescriptionReal.setId(1L);
        prescriptionReal.setAppointmentId(10L);
        prescriptionReal.setMedication("Amoxicilina 500mg");
        prescriptionReal.setDosage("1 tableta cada 8 horas");
        prescriptionReal.setDuration("7 días");
        prescriptionReal.setMedicalNotes("Tomar después de las comidas");
        prescriptionReal.setCreatedAt(LocalDateTime.now());

        requestDto = new PrescriptionRequest();
        requestDto.setAppointmentId(10L);
        requestDto.setMedication("Amoxicilina 500mg");
        requestDto.setDosage("1 tableta cada 8 horas");
        requestDto.setDuration("7 días");
        requestDto.setMedicalNotes("Tomar después de las comidas");
    }

    @Test
    void createPrescription_DeberiaMapearYGuardar() {
        when(repository.save(any(Prescription.class))).thenAnswer(i -> i.getArguments()[0]);

        Prescription resultado = service.createPrescription(requestDto);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getAppointmentId());
        assertEquals("Amoxicilina 500mg", resultado.getMedication());
        verify(repository, times(1)).save(any(Prescription.class));
    }

    @Test
    void findAll_DeberiaRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(prescriptionReal));

        List<Prescription> resultado = service.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getById_DeberiaRetornarPrescription_CuandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(prescriptionReal));

        Prescription resultado = service.getById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void getById_DeberiaLanzarResourceNotFoundException_CuandoNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.getById(99L));

        assertEquals("Receta médica no encontrada con el ID: 99", exception.getMessage());
        verify(repository, times(1)).findById(99L);
    }

    @Test
    void getByAppointmentId_DeberiaRetornarLista_CuandoExisten() {
        when(repository.findByAppointmentId(10L)).thenReturn(List.of(prescriptionReal));

        List<Prescription> resultado = service.getByAppointmentId(10L);

        assertFalse(resultado.isEmpty());
        assertEquals(10L, resultado.get(0).getAppointmentId());
        verify(repository, times(1)).findByAppointmentId(10L);
    }

    @Test
    void getByAppointmentId_DeberiaLanzarException_CuandoNoExisten() {
        when(repository.findByAppointmentId(99L)).thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.getByAppointmentId(99L));

        assertEquals("No existen recetas para la cita ID: 99", exception.getMessage());
        verify(repository, times(1)).findByAppointmentId(99L);
    }

    @Test
    void delete_DeberiaEliminar_CuandoExiste() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void delete_DeberiaLanzarException_CuandoNoExiste() {
        when(repository.existsById(99L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.delete(99L));

        assertEquals("Imposible eliminar. No existe la receta con ID: 99", exception.getMessage());
        verify(repository, times(1)).existsById(99L);
        verify(repository, never()).deleteById(anyLong());
    }
}