package com.hospital.hospitalization.service;

import com.hospital.hospitalization.model.Hospitalization;
import com.hospital.hospitalization.repository.HospitalizationRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HospitalizationService {

    private final HospitalizationRepository repository;

    public HospitalizationService(HospitalizationRepository repository) {
        this.repository = repository;
    }

    public Hospitalization registerAdmission(Hospitalization hospitalization) {
        hospitalization.setAdmissionDate(LocalDateTime.now());
        hospitalization.setStatus("ADMITTED");
        return repository.save(hospitalization);
    }

    public Hospitalization processDischarge(Long id) {
        Hospitalization hospitalization = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospitalización no encontrada con ID: " + id));
        hospitalization.setDischargeDate(LocalDateTime.now());
        hospitalization.setStatus("DISCHARGED");
        return repository.save(hospitalization);
    }

    public List<Hospitalization> getPatientHistory(Long patientId) {
        return repository.findByPatientId(patientId);
    }

    public List<Hospitalization> getActiveHospitalizations() {
        return repository.findByStatus("ADMITTED");
    }
}