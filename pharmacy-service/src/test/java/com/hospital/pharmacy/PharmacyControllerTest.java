package com.hospital.pharmacy;

import com.hospital.pharmacy.controller.PharmacyController;
import com.hospital.pharmacy.model.Medication;
import com.hospital.pharmacy.service.PharmacyService;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PharmacyController.class)
public class PharmacyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PharmacyService service;

    private Medication medicationReal;
    private String medicationJson;

    @BeforeEach
    void setUp() {
        medicationReal = new Medication();
        medicationReal.setId(1L);
        medicationReal.setCode("PAR-01");
        medicationReal.setName("Paracetamol 500mg");
        medicationReal.setStock(100);
        medicationReal.setPrice(5.50);

        medicationJson = """
            {
                "code": "PAR-01",
                "name": "Paracetamol 500mg",
                "stock": 100,
                "price": 5.50
            }
        """;
    }

    @Test
    @WithMockUser
    void listAll_DeberiaRetornar200YListaDeMedicamentos() throws Exception {
        when(service.getAllMedications()).thenReturn(List.of(medicationReal));

        mockMvc.perform(get("/api/v1/pharmacy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("PAR-01"))
                .andExpect(jsonPath("$[0].name").value("Paracetamol 500mg"));
    }

    @Test
    @WithMockUser
    void create_DeberiaRetornar201_CuandoEsExitoso() throws Exception {
        when(service.saveMedication(any(Medication.class))).thenReturn(medicationReal);

        mockMvc.perform(post("/api/v1/pharmacy")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicationJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("PAR-01"));
    }

    @Test
    @WithMockUser
    void create_DeberiaRetornar400_CuandoCodigoYaExiste() throws Exception {
        when(service.saveMedication(any(Medication.class)))
                .thenThrow(new IllegalArgumentException("El código del medicamento ya está registrado."));

        mockMvc.perform(post("/api/v1/pharmacy")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicationJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El código del medicamento ya está registrado."));
    }

    @Test
    @WithMockUser
    void dispense_DeberiaRetornar200_CuandoStockEsSuficiente() throws Exception {
        Medication medicationDispensado = new Medication();
        medicationDispensado.setCode("PAR-01");
        medicationDispensado.setStock(80);

        when(service.dispenseMedication("PAR-01", 20)).thenReturn(medicationDispensado);

        mockMvc.perform(post("/api/v1/pharmacy/dispense")
                        .with(csrf())
                        .param("code", "PAR-01")
                        .param("quantity", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(80));
    }

    @Test
    @WithMockUser
    void dispense_DeberiaRetornar400_CuandoStockEsInsuficiente() throws Exception {
        when(service.dispenseMedication(anyString(), anyInt()))
                .thenThrow(new IllegalStateException("Stock insuficiente. Disponible: 100"));

        mockMvc.perform(post("/api/v1/pharmacy/dispense")
                        .with(csrf())
                        .param("code", "PAR-01")
                        .param("quantity", "150"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Stock insuficiente. Disponible: 100"));
    }

    @Test
    @WithMockUser
    void dispense_DeberiaRetornar404_CuandoMedicamentoNoExiste() throws Exception {
        when(service.dispenseMedication(anyString(), anyInt()))
                .thenThrow(new RuntimeException("Medicamento no encontrado con código: DESC-99"));

        mockMvc.perform(post("/api/v1/pharmacy/dispense")
                        .with(csrf())
                        .param("code", "DESC-99")
                        .param("quantity", "10"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Medicamento no encontrado con código: DESC-99"));
    }
}
