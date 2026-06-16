package com.hospital.billing.service;

import com.hospital.billing.model.Invoice;
import com.hospital.billing.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BillingService {

    private final InvoiceRepository repository;

    public BillingService(InvoiceRepository repository) {
        this.repository = repository;
    }

    public Invoice createInvoice(Invoice invoice) {
        invoice.setStatus("PENDIENTE");
        return repository.save(invoice);
    }

    public List<Invoice> getInvoicesByPatient(Long patientId) {
        return repository.findByPatientId(patientId);
    }

    public Invoice payInvoice(Long invoiceId) {
        Invoice invoice = repository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + invoiceId));
        invoice.setStatus("PAGADO");
        return repository.save(invoice);
    }
}