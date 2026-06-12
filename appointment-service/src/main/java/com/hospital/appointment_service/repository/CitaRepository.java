package com.hospital.appointment_service.repository;

import com.hospital.appointment_service.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByPacienteId(Long pacienteId);
    List<Cita> findByDoctorId(Long doctorId);
    List<Cita> findByFecha(LocalDate fecha);
    List<Cita> findByEstado(String estado);
    boolean existsByDoctorIdAndFechaAndHora(Long doctorId, LocalDate fecha, java.time.LocalTime hora);
}