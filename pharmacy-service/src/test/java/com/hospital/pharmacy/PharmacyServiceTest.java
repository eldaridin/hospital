package com.hospital.pharmacy;

import com.hospital.pharmacy.dto.MedicationDTO;
import com.hospital.pharmacy.model.Medication;
import com.hospital.pharmacy.repository.MedicationRepository;
import com.hospital.pharmacy.service.PharmacyService;
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
public class PharmacyServiceTest {

    @Mock
    private MedicationRepository repository;

    @InjectMocks
    private PharmacyService service;

    private Medication medicationReal;

    @BeforeEach
    void setUp() {
        medicationReal = new Medication();
        medicationReal.setId(1L);
        medicationReal.setCode("PAR-01");
        medicationReal.setName("Paracetamol 500mg");
        medicationReal.setStock(100);
        medicationReal.setPrice(5.50);
    }

    @Test
    void getAllMedications_DeberiaRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(medicationReal));

        List<Medication> resultado = service.getAllMedications();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void saveFromDTO_DeberiaGuardar_CuandoCodigoNoExiste() {
        when(repository.findByCode("PAR-01")).thenReturn(Optional.empty());
        when(repository.save(any(Medication.class))).thenAnswer(i -> i.getArguments()[0]);

        MedicationDTO dto = new MedicationDTO();
        dto.setCode("PAR-01");
        dto.setName("Paracetamol 500mg");
        dto.setStock(100);
        dto.setPrice(5.50);

        Medication resultado = service.saveFromDTO(dto);

        assertNotNull(resultado);
        assertEquals("PAR-01", resultado.getCode());
        verify(repository, times(1)).findByCode("PAR-01");
        verify(repository, times(1)).save(any(Medication.class));
    }

    @Test
    void saveFromDTO_DeberiaLanzarIllegalArgumentException_CuandoCodigoExiste() {
        when(repository.findByCode("PAR-01")).thenReturn(Optional.of(medicationReal));

        MedicationDTO dto = new MedicationDTO();
        dto.setCode("PAR-01");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.saveFromDTO(dto));

        assertEquals("El codigo del medicamento ya esta registrado.", exception.getMessage());
        verify(repository, times(1)).findByCode("PAR-01");
        verify(repository, never()).save(any(Medication.class));
    }

    @Test
    void dispenseMedication_DeberiaReducirStockYGuardar_CuandoHaySuficiente() {
        when(repository.findByCode("PAR-01")).thenReturn(Optional.of(medicationReal));
        when(repository.save(any(Medication.class))).thenAnswer(i -> i.getArguments()[0]);

        Medication resultado = service.dispenseMedication("PAR-01", 20);

        assertNotNull(resultado);
        assertEquals(80, resultado.getStock());
        verify(repository, times(1)).findByCode("PAR-01");
        verify(repository, times(1)).save(medicationReal);
    }

    @Test
    void dispenseMedication_DeberiaLanzarIllegalStateException_CuandoStockEsInsuficiente() {
        when(repository.findByCode("PAR-01")).thenReturn(Optional.of(medicationReal));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.dispenseMedication("PAR-01", 150));

        assertEquals("Stock insuficiente. Disponible: 100", exception.getMessage());
        verify(repository, times(1)).findByCode("PAR-01");
        verify(repository, never()).save(any(Medication.class));
    }

    @Test
    void dispenseMedication_DeberiaLanzarRuntimeException_CuandoNoExiste() {
        when(repository.findByCode("DESC-99")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> service.dispenseMedication("DESC-99", 10));

        assertEquals("Medicamento no encontrado con codigo: DESC-99", exception.getMessage());
        verify(repository, times(1)).findByCode("DESC-99");
        verify(repository, never()).save(any(Medication.class));
    }
}
