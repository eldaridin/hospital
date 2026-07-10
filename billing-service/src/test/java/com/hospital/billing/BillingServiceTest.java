package com.hospital.billing;

import com.hospital.billing.model.Invoice;
import com.hospital.billing.repository.InvoiceRepository;
import com.hospital.billing.service.BillingService;
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
public class BillingServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private BillingService billingService;

    private Invoice invoiceReal;

    @BeforeEach
    void setUp() {
        invoiceReal = new Invoice();
        invoiceReal.setId(1L);
        invoiceReal.setPatientId(100L);
        invoiceReal.setConcept("Consulta General");
        invoiceReal.setAmount(50.0);
        invoiceReal.setStatus("PENDIENTE");
    }

    @Test
    void createInvoice_DeberiaEstablecerStatusPendienteYGuardar() {
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(i -> i.getArguments()[0]);

        Invoice nuevaInvoice = new Invoice();
        nuevaInvoice.setPatientId(100L);
        nuevaInvoice.setConcept("Consulta General");
        nuevaInvoice.setAmount(50.0);

        Invoice resultado = billingService.createInvoice(nuevaInvoice);

        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getStatus());
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void getInvoicesByPatient_DeberiaRetornarListaDeFacturas() {
        when(invoiceRepository.findByPatientId(100L)).thenReturn(List.of(invoiceReal));

        List<Invoice> resultado = billingService.getInvoicesByPatient(100L);

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(100L, resultado.get(0).getPatientId());
        verify(invoiceRepository, times(1)).findByPatientId(100L);
    }

    @Test
    void payInvoice_DeberiaCambiarStatusAPagado_CuandoExiste() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoiceReal));
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(i -> i.getArguments()[0]);

        Invoice resultado = billingService.payInvoice(1L);

        assertNotNull(resultado);
        assertEquals("PAGADO", resultado.getStatus());
        verify(invoiceRepository, times(1)).findById(1L);
        verify(invoiceRepository, times(1)).save(any(Invoice.class));
    }

    @Test
    void payInvoice_DeberiaLanzarExcepcion_CuandoNoExiste() {
        when(invoiceRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> billingService.payInvoice(99L));

        assertEquals("Factura no encontrada con ID: 99", exception.getMessage());
        verify(invoiceRepository, times(1)).findById(99L);
        verify(invoiceRepository, never()).save(any(Invoice.class));
    }
}
