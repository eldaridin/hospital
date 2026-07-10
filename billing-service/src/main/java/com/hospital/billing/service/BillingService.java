package com.hospital.billing.service;

import com.hospital.billing.dto.InvoiceDTO;
import com.hospital.billing.model.Invoice;
import com.hospital.billing.repository.InvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BillingService {

    private static final Logger logger = LoggerFactory.getLogger(BillingService.class);

    private final InvoiceRepository repository;

    public BillingService(InvoiceRepository repository) {
        this.repository = repository;
    }

    public Invoice createFromDTO(InvoiceDTO dto) {
        logger.info("Creando nueva factura para paciente ID: {}", dto.getPatientId());
        Invoice invoice = new Invoice();
        invoice.setPatientId(dto.getPatientId());
        invoice.setConcept(dto.getConcept());
        invoice.setAmount(dto.getAmount());
        invoice.setStatus("PENDIENTE");
        Invoice guardada = repository.save(invoice);
        logger.info("Factura creada con ID: {}", guardada.getId());
        return guardada;
    }

    public List<Invoice> getInvoicesByPatient(Long patientId) {
        logger.info("Obteniendo facturas del paciente ID: {}", patientId);
        return repository.findByPatientId(patientId);
    }

    public Invoice payInvoice(Long invoiceId) {
        logger.info("Procesando pago de factura ID: {}", invoiceId);
        Invoice invoice = repository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + invoiceId));
        invoice.setStatus("PAGADO");
        Invoice pagada = repository.save(invoice);
        logger.info("Factura ID: {} pagada exitosamente", invoiceId);
        return pagada;
    }
}
