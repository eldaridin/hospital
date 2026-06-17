package com.hospital.hospitalization.repository;

import com.hospital.hospitalization.model.Hospitalization;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HospitalizationRepository extends JpaRepository<Hospitalization, Long> {
    List<Hospitalization> findByPatientId(Long patientId);
    List<Hospitalization> findByStatus(String status);
}