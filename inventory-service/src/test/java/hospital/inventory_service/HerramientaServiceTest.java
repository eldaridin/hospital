package hospital.inventory_service;

import hospital.inventory_service.dto.HerramientaDTO;
import hospital.inventory_service.model.Herramienta;
import hospital.inventory_service.repository.HerramientaRepository;
import hospital.inventory_service.service.HerramientaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HerramientaServiceTest {
    @Mock
    private HerramientaRepository herramientaRepository;
    @InjectMocks
    private HerramientaService herramientaService;
    private Herramienta mockHerramienta;

    @BeforeEach
    public void setup() {
        mockHerramienta = new Herramienta(1, "Bisturi", "Quirurgico", 50, "Almacen A");
    }

    @Test
    public void findAll_DebeRetornarListaDeHerramientas() {
       when(herramientaRepository.findAll()).thenReturn(List.of(mockHerramienta));

       List<Herramienta> resultado = herramientaService.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Bisturi", resultado.get(0).getNombre());
        verify(herramientaRepository, times(1)).findAll();
    }

    @Test
    public void findById_DebeRetornarHerramienta_SiExiste() {
        when(herramientaRepository.findById(1)).thenReturn(Optional.of(mockHerramienta));

        Optional<Herramienta> resultado = herramientaService.findById(1);

        assertTrue(resultado.isPresent());
        assertEquals(1, resultado.get().getId());
        assertEquals("Bisturi", resultado.get().getNombre());
    }

    @Test
    public void save_DeberiaGuardarHerramientaYRetornar() {
        when(herramientaRepository.save(any(Herramienta.class))).thenReturn(mockHerramienta);

        HerramientaDTO dto = new HerramientaDTO();
        dto.setNombre("Bisturi");
        dto.setTipo("Quirurgico");
        dto.setExistencias(50);
        dto.setUbicacion("Almacen A");

        Herramienta resultado = herramientaService.saveFromDTO(dto);

        assertNotNull(resultado);
        assertEquals("Almacen A", resultado.getUbicacion());
        verify(herramientaRepository, times(1)).save(any(Herramienta.class));
    }

    @Test
    public void deleteById_DeberiaRetornarTrue_SiExiste() {
        when(herramientaRepository.existsById(1)).thenReturn(true);

        boolean resultado = herramientaService.deleteById(1);

        assertTrue(resultado);
        verify(herramientaRepository, times(1)).deleteById(1);
    }

    @Test
    public void deleteById_DeberiaRetornarFalse_SiNoExiste() {
        when(herramientaRepository.existsById(99)).thenReturn(false);

        boolean resultado = herramientaService.deleteById(99);

        assertFalse(resultado);
        verify(herramientaRepository, never()).deleteById(any());
    }
}
