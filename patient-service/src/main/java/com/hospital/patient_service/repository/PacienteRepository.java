package com.hospital.patient_service.repository;

import com.hospital.patient_service.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByEmail(String email);
    Optional<Paciente> findByRut(String rut);
    boolean existsByEmail(String email);
    boolean existsByRut(String rut);
}