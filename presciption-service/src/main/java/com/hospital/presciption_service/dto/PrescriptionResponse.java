package com.hospital.presciption_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PrescriptionResponse {
    private Long id;
    private Long appointmentId;
    private String medication;
    private String dosage;
    private String duration;
    private String medicalNotes;
    private LocalDateTime createdAt;
}
