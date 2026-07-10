package com.hospital.billing.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class InvoiceDTO {

    @NotNull(message = "El ID del paciente es obligatorio")
    private Long patientId;

    @NotBlank(message = "El concepto es obligatorio")
    private String concept;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private Double amount;

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getConcept() { return concept; }
    public void setConcept(String concept) { this.concept = concept; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
