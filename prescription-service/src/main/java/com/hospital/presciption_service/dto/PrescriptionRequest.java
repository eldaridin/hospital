package com.hospital.presciption_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrescriptionRequest {
    @NotNull(message = "El ID de la cita es obligatorio")
    private Long appointmentId;

    @NotBlank(message = "El medicamento no puede estar vacío")
    private String medication;

    private String dosage;
    private String duration;
    private String medicalNotes;
}
