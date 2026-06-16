package com.hospital.pharmacy.repository;

import com.hospital.pharmacy.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    // Permite buscar un medicamento por su código único de barra o catálogo
    Optional<Medication> findByCode(String code);
}