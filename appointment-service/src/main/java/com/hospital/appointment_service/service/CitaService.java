package com.hospital.appointment_service.service;

import com.hospital.appointment_service.client.DoctorClient;
import com.hospital.appointment_service.client.PacienteClient;
import com.hospital.appointment_service.dto.CitaDTO;
import com.hospital.appointment_service.dto.DoctorResponseDTO;
import com.hospital.appointment_service.dto.PacienteResponseDTO;
import com.hospital.appointment_service.model.Cita;
import com.hospital.appointment_service.repository.CitaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    private static final Logger logger = LoggerFactory.getLogger(CitaService.class);

    private final CitaRepository citaRepository;
    private final PacienteClient pacienteClient;
    private final DoctorClient doctorClient;

    public CitaService(CitaRepository citaRepository, PacienteClient pacienteClient, DoctorClient doctorClient) {
        this.citaRepository = citaRepository;
        this.pacienteClient = pacienteClient;
        this.doctorClient = doctorClient;
    }

    public List<Cita> obtenerTodas() {
        logger.info("Obteniendo lista de todas las citas");
        return citaRepository.findAll();
    }

    public Optional<Cita> obtenerPorId(Long id) {
        logger.info("Buscando cita con ID: {}", id);
        return citaRepository.findById(id);
    }

    public List<Cita> obtenerPorPaciente(Long pacienteId) {
        logger.info("Buscando citas del paciente ID: {}", pacienteId);
        return citaRepository.findByPacienteId(pacienteId);
    }

    public List<Cita> obtenerPorDoctor(Long doctorId) {
        logger.info("Buscando citas del doctor ID: {}", doctorId);
        return citaRepository.findByDoctorId(doctorId);
    }

    public Cita crear(CitaDTO dto) {
        logger.info("Creando nueva cita para paciente ID: {} con doctor ID: {}", dto.getPacienteId(), dto.getDoctorId());

        // Verificar que el paciente existe en patient-service
        try {
            PacienteResponseDTO paciente = pacienteClient.obtenerPacientePorId(dto.getPacienteId());
            if (paciente == null) {
                throw new RuntimeException("Paciente con ID " + dto.getPacienteId() + " no encontrado");
            }
            logger.info("Paciente ID: {} verificado correctamente", dto.getPacienteId());
        } catch (Exception e) {
            logger.error("Error al verificar paciente ID: {}", dto.getPacienteId());
            throw new RuntimeException("No se pudo verificar el paciente: " + e.getMessage());
        }

        // Verificar que el doctor existe en doctor-service
        try {
            DoctorResponseDTO doctor = doctorClient.obtenerDoctorPorId(dto.getDoctorId());
            if (doctor == null) {
                throw new RuntimeException("Doctor con ID " + dto.getDoctorId() + " no encontrado");
            }
            logger.info("Doctor ID: {} verificado correctamente", dto.getDoctorId());
        } catch (Exception e) {
            logger.error("Error al verificar doctor ID: {}", dto.getDoctorId());
            throw new RuntimeException("No se pudo verificar el doctor: " + e.getMessage());
        }

        // Verificar que no exista otra cita con el mismo doctor, fecha y hora
        LocalDate fecha = LocalDate.parse(dto.getFecha());
        LocalTime hora = LocalTime.parse(dto.getHora());

        if (citaRepository.existsByDoctorIdAndFechaAndHora(dto.getDoctorId(), fecha, hora)) {
            logger.warn("Ya existe una cita para el doctor ID: {} en fecha: {} hora: {}", dto.getDoctorId(), fecha, hora);
            throw new RuntimeException("El doctor ya tiene una cita en esa fecha y hora");
        }

        Cita cita = new Cita();
        cita.setPacienteId(dto.getPacienteId());
        cita.setDoctorId(dto.getDoctorId());
        cita.setFecha(fecha);
        cita.setHora(hora);
        cita.setMotivo(dto.getMotivo());
        cita.setEstado(dto.getEstado() != null ? dto.getEstado() : "PENDIENTE");

        Cita guardada = citaRepository.save(cita);
        logger.info("Cita creada exitosamente con ID: {}", guardada.getId());
        return guardada;
    }

    public Optional<Cita> actualizarEstado(Long id, String estado) {
        logger.info("Actualizando estado de cita ID: {} a {}", id, estado);
        return citaRepository.findById(id).map(cita -> {
            cita.setEstado(estado);
            Cita actualizada = citaRepository.save(cita);
            logger.info("Estado de cita ID: {} actualizado exitosamente", id);
            return actualizada;
        });
    }

    public boolean eliminar(Long id) {
        logger.info("Eliminando cita con ID: {}", id);
        if (citaRepository.existsById(id)) {
            citaRepository.deleteById(id);
            logger.info("Cita ID: {} eliminada exitosamente", id);
            return true;
        }
        logger.warn("Cita con ID: {} no encontrada para eliminar", id);
        return false;
    }
}