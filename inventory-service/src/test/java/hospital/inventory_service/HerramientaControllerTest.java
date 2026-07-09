package hospital.inventory_service;


import com.fasterxml.jackson.databind.ObjectMapper;
import hospital.inventory_service.controller.HerramientaController;
import hospital.inventory_service.model.Herramienta;
import hospital.inventory_service.service.HerramientaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
        mockHerramienta = new Herramienta(1, "Bisturí", "Quirúrgico", 50, "Almacén A");

    }

    //GET Listar todas
    @Test
    @WithMockUser
    void findAll_DeberiaRetornar200YListaDeHerramientas() throws Exception {
        when(herramientaService.findAll()).thenReturn(List.of(mockHerramienta));

        mockMvc.perform(get("/api/inventario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Bisturí"))
                .andExpect(jsonPath("$[0].existencias").value(50));
    }
    //GET Obtener por ID
    @Test
    @WithMockUser
    void findById_DeberiaRetornar200_CuandoExiste() throws Exception {
        when(herramientaService.findById(1)).thenReturn(mockHerramienta);

        mockMvc.perform(get("/api/inventario/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipo").value("Quirúrgico"));
    }
    //GET retorna 404 si no existe
    @Test
    @WithMockUser
    void findById_DeberiaRetornar404_CuandoNoExiste() throws Exception {
        when(herramientaService.findById(99)).thenReturn(null);

        mockMvc.perform(get("/api/inventario/{id}", 99))
                .andExpect(status().isNotFound());
    }
    // POST Guarda una herramienta
    @Test
    @WithMockUser
    void save_DeberiaRetornar200YHerramientaCreada() throws Exception {
        when(herramientaService.save(any(Herramienta.class))).thenReturn(mockHerramienta);

        mockMvc.perform(post("/api/inventario")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockHerramienta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Bisturí"));
    }

    //PUT Actualiza una Herramienta
    @Test
    @WithMockUser
    void update_DeberiaRetornar200YHerramientaActualizada() throws Exception {
        Herramienta herramientaActualizada = new Herramienta(1, "Bisturí Láser", "Quirúrgico", 45, "Almacén B");

        when(herramientaService.save(any(Herramienta.class))).thenReturn(herramientaActualizada);

        mockMvc.perform(put("/api/inventario/{id}", 1)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(herramientaActualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Bisturí Láser"))
                .andExpect(jsonPath("$.ubicacion").value("Almacén B"));
    }

    //DELETE Borra una herramienta
    @Test
    @WithMockUser
    void delete_DeberiaRetornar200() throws Exception {
        doNothing().when(herramientaService).deleteById(1);

        mockMvc.perform(delete("/api/inventario/{id}", 1)
                        .with(csrf()))
                .andExpect(status().isOk());
    }



}
