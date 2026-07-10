package com.hospital.billing;

import com.hospital.billing.controller.BillingController;
import com.hospital.billing.dto.InvoiceDTO;
import com.hospital.billing.model.Invoice;
import com.hospital.billing.service.BillingService;
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
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BillingController.class)
public class BillingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillingService billingService;

    private Invoice invoiceReal;
    private String invoiceJson;

    @BeforeEach
    void setUp() {
        invoiceReal = new Invoice();
        invoiceReal.setId(1L);
        invoiceReal.setPatientId(100L);
        invoiceReal.setConcept("Consulta General");
        invoiceReal.setAmount(50.0);
        invoiceReal.setStatus("PENDIENTE");

        invoiceJson = """
            {
                "patientId": 100,
                "concept": "Consulta General",
                "amount": 50.0
            }
        """;
    }

    @Test
    @WithMockUser
    void create_DeberiaRetornar201_CuandoEsExitoso() throws Exception {
        when(billingService.createFromDTO(any(InvoiceDTO.class))).thenReturn(invoiceReal);

        mockMvc.perform(post("/api/v1/billing")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invoiceJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDIENTE"))
                .andExpect(jsonPath("$.concept").value("Consulta General"));
    }

    @Test
    @WithMockUser
    void getByPatient_DeberiaRetornar200YListaDeFacturas() throws Exception {
        when(billingService.getInvoicesByPatient(100L)).thenReturn(List.of(invoiceReal));

        mockMvc.perform(get("/api/v1/billing/patient/{patientId}", 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientId").value(100))
                .andExpect(jsonPath("$[0].amount").value(50.0));
    }

    @Test
    @WithMockUser
    void pay_DeberiaRetornar200_CuandoEsExitoso() throws Exception {
        Invoice invoicePagada = new Invoice();
        invoicePagada.setId(1L);
        invoicePagada.setStatus("PAGADO");
        invoicePagada.setConcept("Consulta General");

        when(billingService.payInvoice(1L)).thenReturn(invoicePagada);

        mockMvc.perform(put("/api/v1/billing/{id}/pay", 1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAGADO"));
    }

    @Test
    @WithMockUser
    void pay_DeberiaRetornar404_CuandoFacturaNoExiste() throws Exception {
        when(billingService.payInvoice(99L))
                .thenThrow(new RuntimeException("Factura no encontrada con ID: 99"));

        mockMvc.perform(put("/api/v1/billing/{id}/pay", 99L)
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Factura no encontrada con ID: 99"));
    }
}
