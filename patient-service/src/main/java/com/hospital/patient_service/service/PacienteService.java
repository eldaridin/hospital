package com.hospital.patient_service.service;

import com.hospital.patient_service.dto.PacienteDTO;
import com.hospital.patient_service.model.Paciente;
import com.hospital.patient_service.repository.PacienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PacienteService {

    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public List<Paciente> obtenerTodos() {
        logger.info("Obteniendo lista de todos los pacientes");
        return pacienteRepository.findAll();
    }

    public Optional<Paciente> obtenerPorId(Long id) {
        logger.info("Buscando paciente con ID: {}", id);
        return pacienteRepository.findById(id);
    }

    public Paciente crear(PacienteDTO dto) {
        logger.info("Creando nuevo paciente con RUT: {}", dto.getRut());

        if (pacienteRepository.existsByEmail(dto.getEmail())) {
            logger.warn("Ya existe un paciente con email: {}", dto.getEmail());
            throw new RuntimeException("Ya existe un paciente con ese email");
        }

        if (pacienteRepository.existsByRut(dto.getRut())) {
            logger.warn("Ya existe un paciente con RUT: {}", dto.getRut());
            throw new RuntimeException("Ya existe un paciente con ese RUT");
        }

        Paciente paciente = new Paciente();
        paciente.setNombre(dto.getNombre());
        paciente.setApellido(dto.getApellido());
        paciente.setEmail(dto.getEmail());
        paciente.setTelefono(dto.getTelefono());
        paciente.setRut(dto.getRut());
        paciente.setFechaNacimiento(LocalDate.parse(dto.getFechaNacimiento()));

        Paciente guardado = pacienteRepository.save(paciente);
        logger.info("Paciente creado exitosamente con ID: {}", guardado.getId());
        return guardado;
    }

    public Optional<Paciente> actualizar(Long id, PacienteDTO dto) {
        logger.info("Actualizando paciente con ID: {}", id);
        return pacienteRepository.findById(id).map(paciente -> {
            paciente.setNombre(dto.getNombre());
            paciente.setApellido(dto.getApellido());
            paciente.setEmail(dto.getEmail());
            paciente.setTelefono(dto.getTelefono());
            paciente.setRut(dto.getRut());
            paciente.setFechaNacimiento(LocalDate.parse(dto.getFechaNacimiento()));
            Paciente actualizado = pacienteRepository.save(paciente);
            logger.info("Paciente ID: {} actualizado exitosamente", id);
            return actualizado;
        });
    }

    public boolean eliminar(Long id) {
        logger.info("Eliminando paciente con ID: {}", id);
        if (pacienteRepository.existsById(id)) {
            pacienteRepository.deleteById(id);
            logger.info("Paciente ID: {} eliminado exitosamente", id);
            return true;
        }
        logger.warn("Paciente con ID: {} no encontrado para eliminar", id);
        return false;
    }
}