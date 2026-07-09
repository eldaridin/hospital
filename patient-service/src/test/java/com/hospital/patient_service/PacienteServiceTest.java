package com.hospital.patient_service;


import com.hospital.patient_service.dto.PacienteDTO;
import com.hospital.patient_service.model.Paciente;
import com.hospital.patient_service.repository.PacienteRepository;
import com.hospital.patient_service.service.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
//Testear lógica del microservicio
@ExtendWith(MockitoExtension.class)
public class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;
    private Paciente mockPaciente;
    private PacienteDTO mockDto;

    @BeforeEach
    public void setUp() {
        mockPaciente = new Paciente();
        mockPaciente = new Paciente();
        mockPaciente.setId(1L);
        mockPaciente.setNombre("Juan");
        mockPaciente.setApellido("Perez");
        mockPaciente.setEmail("juan.perez@email.com");
        mockPaciente.setRut("12345678-9");
        mockPaciente.setTelefono("987654321");
        mockPaciente.setFechaNacimiento(LocalDate.of(1990, 5, 20));

        mockDto = mock(PacienteDTO.class);
    }
    //Obtener todos los Pacientes
    @Test
    public void obtenerTodos_DeberiaRetornarTodosLosPacientes(){
        when(pacienteRepository.findAll()).thenReturn(List.of(mockPaciente));

        List<Paciente> resultado = pacienteService.obtenerTodos();
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(pacienteRepository, times(1)).findAll();

    }
    //Obtener por ID
    @Test
    public void obtenerPorId_DeberiaRetornarPacienteSiExiste(){
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(mockPaciente));
        Optional<Paciente> resultado = pacienteService.obtenerPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals("Juan", resultado.get().getNombre());
    }
    //Crear
    @Test
    public void crear_DeberiaGuardarPaciente(){
        when(mockDto.getNombre()).thenReturn("Juan");
        when(mockDto.getApellido()).thenReturn("Perez");
        when(mockDto.getEmail()).thenReturn("juan.perez@email.com");
        when(mockDto.getRut()).thenReturn("12345678-9");
        when(mockDto.getTelefono()).thenReturn("987654321");
        when(mockDto.getFechaNacimiento()).thenReturn("1990-05-20");

        when(pacienteRepository.existsByEmail(anyString())).thenReturn(false);
        when(pacienteRepository.existsByRut(anyString())).thenReturn(false);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(mockPaciente);

        Paciente resultado = pacienteService.crear(mockDto);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(pacienteRepository, times(1)).save(any(Paciente.class));

    }
    //Excepción si el RUT ya está registrado
    @Test
    public void crear_DeberiaLanzarExepcionCuandoRutYaExiste(){
        when(mockDto.getEmail()).thenReturn("juan.perez@email.com");
        when(mockDto.getRut()).thenReturn("12345678-9");
        when(pacienteRepository.existsByEmail(anyString())).thenReturn(false);
        when(pacienteRepository.existsByRut("12345678-9")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> pacienteService.crear(mockDto));

        assertEquals("Ya existe un paciente con ese RUT", exception.getMessage());
        verify(pacienteRepository,never()).save(any(Paciente.class));
    }
    //Actualizar un cliente
    @Test
    public void actualizar_DeberiaActualizarYRetornarPaciente(){
        when(mockDto.getNombre()).thenReturn("Juan Actualizado");
        when(mockDto.getApellido()).thenReturn("Perez Actualizado");
        when(mockDto.getEmail()).thenReturn("Juan.actualizado@email.com");
        when(mockDto.getRut()).thenReturn("12345678-9");
        when(mockDto.getTelefono()).thenReturn("987654321");
        when(mockDto.getFechaNacimiento()).thenReturn("1990-05-20");

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(mockPaciente));
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(i -> i.getArguments()[0]);

        Optional<Paciente> resultado = pacienteService.actualizar(1L,mockDto);

        assertTrue(resultado.isPresent());
        assertEquals("Juan Actualizado", resultado.get().getNombre());

    }
    //Eliminar un paciente
    @Test
    public void eliminar_DeberiaRetornarTrue_SiExiste(){
        when(pacienteRepository.existsById(1L)).thenReturn(true);

        boolean resultado = pacienteService.eliminar(1L);
        assertTrue(resultado);
        verify(pacienteRepository, times(1)).existsById(1L);
    }
    //Excepción si al eliminar no existe el paciente
    @Test
    public void eliminar_DeberiaLanzarExepcionNoExiste(){
        when(pacienteRepository.existsById(99L)).thenReturn(false);

        boolean resultado = pacienteService.eliminar(99L);
        assertFalse(resultado);
        verify(pacienteRepository, never()).deleteById(anyLong());
    }

}
