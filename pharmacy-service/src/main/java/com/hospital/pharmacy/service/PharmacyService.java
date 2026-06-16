package com.hospital.pharmacy.service;

import com.hospital.pharmacy.model.Medication;
import com.hospital.pharmacy.repository.MedicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PharmacyService {

    private final MedicationRepository repository;

    // Inyección de dependencias por constructor en la dirección correcta (IE 1.2.1)
    public PharmacyService(MedicationRepository repository) {
        this.repository = repository;
    }

    public List<Medication> getAllMedications() {
        return repository.findAll();
    }

    public Medication saveMedication(Medication medication) {
        if (repository.findByCode(medication.getCode()).isPresent()) {
            throw new IllegalArgumentException("El código del medicamento ya está registrado.");
        }
        return repository.save(medication);
    }

    @Transactional
    public Medication dispenseMedication(String code, Integer quantity) {
        Medication medication = repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Medicamento no encontrado con código: " + code));

        if (medication.getStock() < quantity) {
            throw new IllegalStateException("Stock insuficiente. Disponible: " + medication.getStock());
        }

        medication.setStock(medication.getStock() - quantity);
        return repository.save(medication);
    }
}