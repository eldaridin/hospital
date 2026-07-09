package hospital.inventory_service;


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


//Test de la lógica del microservicio
@ExtendWith(MockitoExtension.class)
public class HerramientaServiceTest {
    @Mock
    private HerramientaRepository herramientaRepository;
    @InjectMocks
    private HerramientaService herramientaService;
    private Herramienta mockHerramienta;

    @BeforeEach
    public void setup() {
        mockHerramienta = new Herramienta(1, "Bisturí", "Quirúrgico", 50, "Almacén A");
    }
    //Listar Todas
    @Test
    public void findAll_DebeRetornarListaDeHerramientas() {
       when(herramientaRepository.findAll()).thenReturn(List.of(mockHerramienta));

       List<Herramienta> resultado = herramientaService.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Bisturí", resultado.get(0).getNombre());
        verify(herramientaRepository, times(1)).findAll();
    }
    //Obtener por ID
    @Test
    public void findById_DebeRetornarHerramienta_SiExiste() {
        when(herramientaRepository.findById(1)).thenReturn(Optional.of(mockHerramienta));

        Herramienta resultado = herramientaService.findById(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Bisturí", resultado.getNombre());
    }
    //Guardar
    @Test
    public void save_DeberiaGuardarHerramientaYRetornar(){
        when(herramientaRepository.save(any(Herramienta.class))).thenReturn(mockHerramienta);

        Herramienta resultado = herramientaService.save(mockHerramienta);

        assertNotNull(resultado);
        assertEquals("Almacén A", resultado.getUbicacion());
        verify(herramientaRepository, times(1)).save(mockHerramienta);
    }

    //Borrar por ID
    @Test
    public void deleteById_DeberiaLlamarAlRepositorio(){
        doNothing().when(herramientaRepository).deleteById(1);

        herramientaService.deleteById(1);
        verify(herramientaRepository, times(1)).deleteById(1);

    }




}
