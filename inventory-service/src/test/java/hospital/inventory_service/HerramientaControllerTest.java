package hospital.inventory_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hospital.inventory_service.controller.HerramientaController;
import hospital.inventory_service.dto.HerramientaDTO;
import hospital.inventory_service.model.Herramienta;
import hospital.inventory_service.service.HerramientaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HerramientaController.class)
public class HerramientaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HerramientaService herramientaService;
    private Herramienta mockHerramienta;

    @BeforeEach
    public void setUp() {
        mockHerramienta = new Herramienta(1, "Bisturi", "Quirurgico", 50, "Almacen A");
    }

    @Test
    void findAll_DeberiaRetornar200YListaDeHerramientas() throws Exception {
        when(herramientaService.findAll()).thenReturn(List.of(mockHerramienta));

        mockMvc.perform(get("/api/inventario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Bisturi"))
                .andExpect(jsonPath("$[0].existencias").value(50));
    }

    @Test
    void findById_DeberiaRetornar200_CuandoExiste() throws Exception {
        when(herramientaService.findById(1)).thenReturn(Optional.of(mockHerramienta));

        mockMvc.perform(get("/api/inventario/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("Quirurgico"));
    }

    @Test
    void findById_DeberiaRetornar404_CuandoNoExiste() throws Exception {
        when(herramientaService.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/inventario/{id}", 99))
                .andExpect(status().isNotFound());
    }

    @Test
    void save_DeberiaRetornar201YHerramientaCreada() throws Exception {
        when(herramientaService.saveFromDTO(any(HerramientaDTO.class))).thenReturn(mockHerramienta);

        HerramientaDTO dto = new HerramientaDTO();
        dto.setNombre("Bisturi");
        dto.setTipo("Quirurgico");
        dto.setExistencias(50);
        dto.setUbicacion("Almacen A");

        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Bisturi"));
    }

    @Test
    void update_DeberiaRetornar200YHerramientaActualizada() throws Exception {
        Herramienta herramientaActualizada = new Herramienta(1, "Bisturi Laser", "Quirurgico", 45, "Almacen B");
        when(herramientaService.updateFromDTO(eq(1), any(HerramientaDTO.class))).thenReturn(herramientaActualizada);

        HerramientaDTO dto = new HerramientaDTO();
        dto.setNombre("Bisturi Laser");
        dto.setTipo("Quirurgico");
        dto.setExistencias(45);
        dto.setUbicacion("Almacen B");

        mockMvc.perform(put("/api/inventario/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Bisturi Laser"))
                .andExpect(jsonPath("$.ubicacion").value("Almacen B"));
    }

    @Test
    void delete_DeberiaRetornar204() throws Exception {
        when(herramientaService.deleteById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/inventario/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
