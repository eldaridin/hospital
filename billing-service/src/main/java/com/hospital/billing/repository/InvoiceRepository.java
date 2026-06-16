package com.hospital.billing.repository;

import com.hospital.billing.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    // Permite obtener el historial de facturación por paciente (Exigencia del negocio)
    List<Invoice> findByPatientId(Long patientId);
}