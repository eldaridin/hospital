package com.hospital.billing.controller;

import com.hospital.billing.dto.InvoiceDTO;
import com.hospital.billing.model.Invoice;
import com.hospital.billing.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/billing")
@Tag(name = "Billing Management", description = "Endpoints para cobros y facturacion hospitalaria")
public class BillingController {

    private static final Logger logger = LoggerFactory.getLogger(BillingController.class);

    private final BillingService service;

    public BillingController(BillingService service) {
        this.service = service;
    }

    @Operation(summary = "Generar una nueva factura", description = "Registra un cobro pendiente asociado a un paciente")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody InvoiceDTO dto) {
        logger.info("POST /api/v1/billing - Creando factura");
        try {
            Invoice creado = service.createFromDTO(dto);
            return new ResponseEntity<>(creado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Error al crear factura: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Historial de facturas por paciente", description = "Retorna todas las facturas de un paciente especifico")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Invoice>> getByPatient(@PathVariable Long patientId) {
        logger.info("GET /api/v1/billing/patient/{}", patientId);
        return ResponseEntity.ok(service.getInvoicesByPatient(patientId));
    }

    @Operation(summary = "Registrar el pago de una factura", description = "Cambia el estado de una factura de PENDIENTE a PAGADO")
    @PutMapping("/{id}/pay")
    public ResponseEntity<?> pay(@PathVariable Long id) {
        logger.info("PUT /api/v1/billing/{}/pay", id);
        try {
            return ResponseEntity.ok(service.payInvoice(id));
        } catch (RuntimeException e) {
            logger.error("Error al pagar factura ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
