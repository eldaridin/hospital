package com.hospital.presciption_service.repository;


import com.hospital.presciption_service.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    // Método personalizado para buscar todas las recetas de una cita específica
    List<Prescription> findByAppointmentId(Long appointmentId);
}
