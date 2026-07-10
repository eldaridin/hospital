//Test logica del microservicio
package com.hospital.doctor_service;

import com.hospital.doctor_service.dto.DoctorDTO;
import com.hospital.doctor_service.model.Doctor;
import com.hospital.doctor_service.repository.DoctorRepository;
import com.hospital.doctor_service.service.DoctorService;
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
public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService doctorService;

    private Doctor mockDoctor;
    private DoctorDTO dtoReal;

    @BeforeEach
    void setUp() {
        mockDoctor = new Doctor();
        mockDoctor.setId(1L);
        mockDoctor.setNombre("Gregory");
        mockDoctor.setApellido("House");
        mockDoctor.setEmail("ghouse@hospital.com");
        mockDoctor.setTelefono("555-1234");
        mockDoctor.setEspecialidad("Diagnóstico");
        mockDoctor.setRut("98765432-1");
        mockDoctor.setDisponible(true);


        dtoReal = new DoctorDTO();
        dtoReal.setNombre("Gregory");
        dtoReal.setApellido("House");
        dtoReal.setEmail("ghouse@hospital.com");
        dtoReal.setTelefono("555-1234");
        dtoReal.setEspecialidad("Diagnóstico");
        dtoReal.setRut("98765432-1");
        dtoReal.setDisponible(true);
    }
    //Obtener Todos
    @Test
    void obtenerTodosDeberiaRetornarListaDeDoctores() {
        when(doctorRepository.findAll()).thenReturn(List.of(mockDoctor));
        List<Doctor> resultado = doctorService.obtenerTodos();
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(doctorRepository, times(1)).findAll();
    }
    //Obtener por ID
    @Test
    void obtenerPorIdDeberiaRetornarDoctor_CuandoExiste() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(mockDoctor));
        Optional<Doctor> resultado = doctorService.obtenerPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals("Gregory", resultado.get().getNombre());
    }
    //Obtener por especialidad
    @Test
    void obtenerPorEspecialidadDeberiaRetornarListaDeDoctores() {
        when(doctorRepository.findByEspecialidad("Diagnóstico")).thenReturn(List.of(mockDoctor));
        List<Doctor> resultado = doctorService.obtenerPorEspecialidad("Diagnóstico");
        assertFalse(resultado.isEmpty());
        assertEquals("Diagnóstico", resultado.get(0).getEspecialidad());
    }
    //Obtener disponibles
    @Test
    void obtenerDisponiblesDeberiaRetornarListaDeDoctores() {
        when(doctorRepository.findByDisponibleTrue()).thenReturn(List.of(mockDoctor));
        List<Doctor> resultado = doctorService.obtenerDisponibles();
        assertFalse(resultado.isEmpty());
        assertTrue(resultado.get(0).getDisponible());
    }
    //Crear
    @Test
    void crear_DeberiaGuardarDoctorCuandoDatosSonValidos() {
        when(doctorRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(doctorRepository.existsByRut(any(String.class))).thenReturn(false);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(mockDoctor);

        Doctor resultado = doctorService.crear(dtoReal);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }
    //Excepcion si ya existe el Email
    @Test
    void crear_DeberiaLanzarExcepcionCuandoEmailYaExiste() {
        when(doctorRepository.existsByEmail(dtoReal.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> doctorService.crear(dtoReal));

        assertEquals("Ya existe un doctor con ese email", exception.getMessage());
        verify(doctorRepository, never()).save(any(Doctor.class));
    }
    //Exepcion si ya existe el Rut
    @Test
    void crear_DeberiaLanzarExcepcionCuandoRutYaExiste() {
        when(doctorRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(doctorRepository.existsByRut(dtoReal.getRut())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> doctorService.crear(dtoReal));

        assertEquals("Ya existe un doctor con ese RUT", exception.getMessage());
        verify(doctorRepository, never()).save(any(Doctor.class));
    }
    //Actualizar
    @Test
    void actualizar_DeberiaActualizarYRetornarDoctorCuandoExiste() {
        dtoReal.setNombre("Gregory Modificado");
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(mockDoctor));
        when(doctorRepository.save(any(Doctor.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<Doctor> resultado = doctorService.actualizar(1L, dtoReal);

        assertTrue(resultado.isPresent());
        assertEquals("Gregory Modificado", resultado.get().getNombre());
    }
    //Eliminar
    @Test
    void eliminar_DeberiaRetornarTrue_CuandoDoctorExiste() {
        when(doctorRepository.existsById(1L)).thenReturn(true);
        boolean resultado = doctorService.eliminar(1L);
        assertTrue(resultado);
        verify(doctorRepository, times(1)).deleteById(1L);
    }
    //Exepcion si no existe
    @Test
    void eliminar_DeberiaRetornarFalse_CuandoDoctorNoExiste() {
        when(doctorRepository.existsById(99L)).thenReturn(false);
        boolean resultado = doctorService.eliminar(99L);
        assertFalse(resultado);
        verify(doctorRepository, never()).deleteById(anyLong());
    }
}