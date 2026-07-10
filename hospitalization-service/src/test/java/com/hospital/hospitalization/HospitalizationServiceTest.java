package com.hospital.hospitalization;

import com.hospital.hospitalization.dto.HospitalizationDTO;
import com.hospital.hospitalization.model.Hospitalization;
import com.hospital.hospitalization.repository.HospitalizationRepository;
import com.hospital.hospitalization.service.HospitalizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HospitalizationServiceTest {

    @Mock
    private HospitalizationRepository repository;

    @InjectMocks
    private HospitalizationService service;

    private Hospitalization hospitalizationReal;

    @BeforeEach
    void setUp() {
        hospitalizationReal = new Hospitalization();
        hospitalizationReal.setId(1L);
        hospitalizationReal.setPatientId(100L);
        hospitalizationReal.setDoctorId(200L);
        hospitalizationReal.setRoomNumber("101A");
        hospitalizationReal.setDiagnosis("Neumonía");
    }

    @Test
    void registerAdmissionFromDTO_DeberiaEstablecerFechaYEstadoAdmitted() {
        when(repository.save(any(Hospitalization.class))).thenAnswer(i -> i.getArguments()[0]);

        HospitalizationDTO dto = new HospitalizationDTO();
        dto.setPatientId(100L);
        dto.setDoctorId(200L);
        dto.setRoomNumber("101A");
        dto.setDiagnosis("Neumonía");

        Hospitalization resultado = service.registerAdmissionFromDTO(dto);

        assertNotNull(resultado);
        assertEquals("ADMITTED", resultado.getStatus());
        assertNotNull(resultado.getAdmissionDate());
        assertNull(resultado.getDischargeDate());
        verify(repository, times(1)).save(any(Hospitalization.class));
    }

    @Test
    void processDischarge_DeberiaEstablecerFechaYEstadoDischarged_CuandoExiste() {
        hospitalizationReal.setStatus("ADMITTED");
        when(repository.findById(1L)).thenReturn(Optional.of(hospitalizationReal));
        when(repository.save(any(Hospitalization.class))).thenAnswer(i -> i.getArguments()[0]);

        Hospitalization resultado = service.processDischarge(1L);

        assertNotNull(resultado);
        assertEquals("DISCHARGED", resultado.getStatus());
        assertNotNull(resultado.getDischargeDate());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Hospitalization.class));
    }

    @Test
    void processDischarge_DeberiaLanzarExcepcion_CuandoNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.processDischarge(99L));

        assertEquals("Hospitalización no encontrada con ID: 99", exception.getMessage());
        verify(repository, times(1)).findById(99L);
        verify(repository, never()).save(any(Hospitalization.class));
    }

    @Test
    void getPatientHistory_DeberiaRetornarListaDeHospitalizaciones() {
        when(repository.findByPatientId(100L)).thenReturn(List.of(hospitalizationReal));

        List<Hospitalization> resultado = service.getPatientHistory(100L);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(100L, resultado.get(0).getPatientId());
        verify(repository, times(1)).findByPatientId(100L);
    }

    @Test
    void getActiveHospitalizations_DeberiaRetornarListaDeAdmitidos() {
        hospitalizationReal.setStatus("ADMITTED");
        when(repository.findByStatus("ADMITTED")).thenReturn(List.of(hospitalizationReal));

        List<Hospitalization> resultado = service.getActiveHospitalizations();

        assertFalse(resultado.isEmpty());
        assertEquals("ADMITTED", resultado.get(0).getStatus());
        verify(repository, times(1)).findByStatus("ADMITTED");
    }
}
