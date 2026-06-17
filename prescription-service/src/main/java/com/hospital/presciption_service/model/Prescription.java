package com.hospital.presciption_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "prescriptions")
@Data // Genera Getters, Setters, ToString, Equals y HashCode (Lombok)
@NoArgsConstructor // Constructor vacío requerido por JPA
@AllArgsConstructor // Constructor con todos los campos
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación lógica con el microservicio appointment-service
    @Column(name = "appointment_id", nullable = false)
    private Long appointmentId;

    @Column(nullable = false, length = 100)
    private String medication;

    @Column(nullable = false)
    private String dosage;

    @Column(nullable = false)
    private String duration;

    // Campo adicional para notas del doctor (opcional)
    @Column(columnDefinition = "TEXT")
    private String medicalNotes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Se ejecuta automáticamente antes de guardar en la DB
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
