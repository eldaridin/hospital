package com.hospital.doctor_service.repository;

import com.hospital.doctor_service.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByEmail(String email);
    Optional<Doctor> findByRut(String rut);
    boolean existsByEmail(String email);
    boolean existsByRut(String rut);
    List<Doctor> findByEspecialidad(String especialidad);
    List<Doctor> findByDisponibleTrue();
}