package com.hospital.hospitalization.service;

import com.hospital.hospitalization.dto.HospitalizationDTO;
import com.hospital.hospitalization.model.Hospitalization;
import com.hospital.hospitalization.repository.HospitalizationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HospitalizationService {

    private static final Logger logger = LoggerFactory.getLogger(HospitalizationService.class);

    private final HospitalizationRepository repository;

    public HospitalizationService(HospitalizationRepository repository) {
        this.repository = repository;
    }

    public Hospitalization registerAdmissionFromDTO(HospitalizationDTO dto) {
        logger.info("Registrando ingreso del paciente ID: {}", dto.getPatientId());
        Hospitalization hospitalization = new Hospitalization();
        hospitalization.setPatientId(dto.getPatientId());
        hospitalization.setDoctorId(dto.getDoctorId());
        hospitalization.setRoomNumber(dto.getRoomNumber());
        hospitalization.setDiagnosis(dto.getDiagnosis());
        hospitalization.setAdmissionDate(LocalDateTime.now());
        hospitalization.setStatus("ADMITTED");
        Hospitalization guardado = repository.save(hospitalization);
        logger.info("Ingreso registrado con ID: {}", guardado.getId());
        return guardado;
    }

    public Hospitalization processDischarge(Long id) {
        logger.info("Procesando alta de hospitalizacion ID: {}", id);
        Hospitalization hospitalization = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospitalizacion no encontrada con ID: " + id));
        hospitalization.setDischargeDate(LocalDateTime.now());
        hospitalization.setStatus("DISCHARGED");
        Hospitalization actualizado = repository.save(hospitalization);
        logger.info("Alta procesada para hospitalizacion ID: {}", id);
        return actualizado;
    }

    public List<Hospitalization> getPatientHistory(Long patientId) {
        logger.info("Obteniendo historial del paciente ID: {}", patientId);
        return repository.findByPatientId(patientId);
    }

    public List<Hospitalization> getActiveHospitalizations() {
        logger.info("Obteniendo hospitalizaciones activas");
        return repository.findByStatus("ADMITTED");
    }
}
