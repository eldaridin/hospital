package com.hospital.billing.controller;

import com.hospital.billing.model.Invoice;
import com.hospital.billing.service.BillingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/billing")
@Tag(name = "Billing Management", description = "Endpoints para cobros y facturación hospitalaria")
public class BillingController {

    private final BillingService service;

    public BillingController(BillingService service) {
        this.service = service;
    }

    @Operation(summary = "Generar una nueva factura", description = "Registra un cobro pendiente asociado a un paciente")
    @PostMapping
    public ResponseEntity<Invoice> create(@RequestBody Invoice invoice) {
        return new ResponseEntity<>(service.createInvoice(invoice), HttpStatus.CREATED);
    }

    @Operation(summary = "Historial de facturas por paciente", description = "Retorna todas las facturas de un paciente específico")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Invoice>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(service.getInvoicesByPatient(patientId));
    }

    @Operation(summary = "Registrar el pago de una factura", description = "Cambia el estado de una factura de PENDIENTE a PAGADO")
    @PutMapping("/{id}/pay")
    public ResponseEntity<?> pay(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.payInvoice(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}