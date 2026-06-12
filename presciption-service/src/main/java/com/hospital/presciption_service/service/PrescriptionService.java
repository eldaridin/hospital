package com.hospital.presciption_service.service;


import com.hospital.presciption_service.dto.PrescriptionRequest;
import com.hospital.presciption_service.exception.ResourceNotFoundException;
import com.hospital.presciption_service.model.Prescription;
import com.hospital.presciption_service.repository.PrescriptionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository repository;

    /**
     * Crea una receta validando primero que la cita exista en appointment-service.
     */
    @Transactional
    public Prescription createPrescription(@Valid PrescriptionRequest dto) {
        // 2. Mapeo de DTO a Entidad
        Prescription prescription = new Prescription();
        prescription.setAppointmentId(dto.getAppointmentId());
        prescription.setMedication(dto.getMedication());
        prescription.setDosage(dto.getDosage());
        prescription.setDuration(dto.getDuration());
        prescription.setMedicalNotes(dto.getMedicalNotes());

        // 3. Persistencia en la DB de Laragon
        return repository.save(prescription);
    }

    /**
     * Recupera todas las recetas registradas.
     */
    @Transactional(readOnly = true)
    public List<Prescription> findAll() {
        return repository.findAll();
    }

    /**
     * Busca una receta por su ID.
     */
    @Transactional(readOnly = true)
    public Prescription getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Receta médica no encontrada con el ID: " + id));
    }

    /**
     * Busca todas las recetas vinculadas a una cita específica.
     */
    @Transactional(readOnly = true)
    public List<Prescription> getByAppointmentId(Long appointmentId) {
        List<Prescription> prescriptions = repository.findByAppointmentId(appointmentId);
        if (prescriptions.isEmpty()) {
            throw new ResourceNotFoundException("No existen recetas para la cita ID: " + appointmentId);
        }
        return prescriptions;
    }

    /**
     * Elimina una receta de la base de datos.
     */
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Imposible eliminar. No existe la receta con ID: " + id);
        }
        repository.deleteById(id);
    }
}
