package com.hospital.doctor_service.service;

import com.hospital.doctor_service.dto.DoctorDTO;
import com.hospital.doctor_service.model.Doctor;
import com.hospital.doctor_service.repository.DoctorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private static final Logger logger = LoggerFactory.getLogger(DoctorService.class);

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> obtenerTodos() {
        logger.info("Obteniendo lista de todos los doctores");
        return doctorRepository.findAll();
    }

    public Optional<Doctor> obtenerPorId(Long id) {
        logger.info("Buscando doctor con ID: {}", id);
        return doctorRepository.findById(id);
    }

    public List<Doctor> obtenerPorEspecialidad(String especialidad) {
        logger.info("Buscando doctores con especialidad: {}", especialidad);
        return doctorRepository.findByEspecialidad(especialidad);
    }

    public List<Doctor> obtenerDisponibles() {
        logger.info("Obteniendo doctores disponibles");
        return doctorRepository.findByDisponibleTrue();
    }

    public Doctor crear(DoctorDTO dto) {
        logger.info("Creando nuevo doctor con RUT: {}", dto.getRut());

        if (doctorRepository.existsByEmail(dto.getEmail())) {
            logger.warn("Ya existe un doctor con email: {}", dto.getEmail());
            throw new RuntimeException("Ya existe un doctor con ese email");
        }

        if (doctorRepository.existsByRut(dto.getRut())) {
            logger.warn("Ya existe un doctor con RUT: {}", dto.getRut());
            throw new RuntimeException("Ya existe un doctor con ese RUT");
        }

        Doctor doctor = new Doctor();
        doctor.setNombre(dto.getNombre());
        doctor.setApellido(dto.getApellido());
        doctor.setEmail(dto.getEmail());
        doctor.setTelefono(dto.getTelefono());
        doctor.setEspecialidad(dto.getEspecialidad());
        doctor.setRut(dto.getRut());
        doctor.setDisponible(dto.getDisponible() != null ? dto.getDisponible() : true);

        Doctor guardado = doctorRepository.save(doctor);
        logger.info("Doctor creado exitosamente con ID: {}", guardado.getId());
        return guardado;
    }

    public Optional<Doctor> actualizar(Long id, DoctorDTO dto) {
        logger.info("Actualizando doctor con ID: {}", id);
        return doctorRepository.findById(id).map(doctor -> {
            doctor.setNombre(dto.getNombre());
            doctor.setApellido(dto.getApellido());
            doctor.setEmail(dto.getEmail());
            doctor.setTelefono(dto.getTelefono());
            doctor.setEspecialidad(dto.getEspecialidad());
            doctor.setRut(dto.getRut());
            doctor.setDisponible(dto.getDisponible() != null ? dto.getDisponible() : doctor.getDisponible());
            Doctor actualizado = doctorRepository.save(doctor);
            logger.info("Doctor ID: {} actualizado exitosamente", id);
            return actualizado;
        });
    }

    public boolean eliminar(Long id) {
        logger.info("Eliminando doctor con ID: {}", id);
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            logger.info("Doctor ID: {} eliminado exitosamente", id);
            return true;
        }
        logger.warn("Doctor con ID: {} no encontrado para eliminar", id);
        return false;
    }
}