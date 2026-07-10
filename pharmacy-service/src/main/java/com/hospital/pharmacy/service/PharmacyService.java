package com.hospital.pharmacy.service;

import com.hospital.pharmacy.dto.MedicationDTO;
import com.hospital.pharmacy.model.Medication;
import com.hospital.pharmacy.repository.MedicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PharmacyService {

    private static final Logger logger = LoggerFactory.getLogger(PharmacyService.class);

    private final MedicationRepository repository;

    public PharmacyService(MedicationRepository repository) {
        this.repository = repository;
    }

    public List<Medication> getAllMedications() {
        logger.info("Obteniendo lista de todos los medicamentos");
        return repository.findAll();
    }

    public Medication saveFromDTO(MedicationDTO dto) {
        logger.info("Creando nuevo medicamento: {}", dto.getName());
        if (repository.findByCode(dto.getCode()).isPresent()) {
            logger.warn("El codigo {} ya esta registrado", dto.getCode());
            throw new IllegalArgumentException("El codigo del medicamento ya esta registrado.");
        }
        Medication medication = new Medication();
        medication.setCode(dto.getCode());
        medication.setName(dto.getName());
        medication.setStock(dto.getStock());
        medication.setPrice(dto.getPrice());
        Medication guardado = repository.save(medication);
        logger.info("Medicamento creado con ID: {}", guardado.getId());
        return guardado;
    }

    @Transactional
    public Medication dispenseMedication(String code, Integer quantity) {
        logger.info("Dispensando {} unidades de codigo: {}", quantity, code);
        Medication medication = repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Medicamento no encontrado con codigo: " + code));

        if (medication.getStock() < quantity) {
            logger.warn("Stock insuficiente. Disponible: {}, Solicitado: {}", medication.getStock(), quantity);
            throw new IllegalStateException("Stock insuficiente. Disponible: " + medication.getStock());
        }

        medication.setStock(medication.getStock() - quantity);
        Medication actualizado = repository.save(medication);
        logger.info("Stock actualizado. Nuevo stock para {}: {}", code, actualizado.getStock());
        return actualizado;
    }
}
