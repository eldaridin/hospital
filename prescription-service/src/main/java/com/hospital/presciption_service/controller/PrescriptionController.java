package com.hospital.presciption_service.controller;



import com.hospital.presciption_service.dto.PrescriptionRequest;
import com.hospital.presciption_service.model.Prescription;
import com.hospital.presciption_service.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService service;

    // Crear una nueva receta
    @PostMapping
    public ResponseEntity<Prescription> create(@Valid @RequestBody PrescriptionRequest dto) {
        Prescription newPrescription = service.createPrescription(dto);
        return new ResponseEntity<>(newPrescription, HttpStatus.CREATED);
    }

    //test
    @GetMapping("/test")
    public String test() {
        return "Servicio Prescription funcionando";
    }
    
    // Obtener todas las recetas (útil para auditoría)
    @GetMapping
    public ResponseEntity<List<Prescription>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // Obtener una receta por su ID (maneja el 404 con el GlobalExceptionHandler)
    @GetMapping("/{id}")
    public ResponseEntity<Prescription> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // Obtener todas las recetas de un paciente específico
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<Prescription>> getByAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(service.getByAppointmentId(appointmentId));
    }

    // Eliminar una receta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}