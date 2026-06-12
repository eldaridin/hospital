package com.hospital.doctor_service.controller;

import com.hospital.doctor_service.dto.DoctorDTO;
import com.hospital.doctor_service.model.Doctor;
import com.hospital.doctor_service.service.DoctorService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctores")
public class DoctorController {

    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> obtenerTodos() {
        logger.info("GET /api/doctores");
        return ResponseEntity.ok(doctorService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> obtenerPorId(@PathVariable Long id) {
        logger.info("GET /api/doctores/{}", id);
        return doctorService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<Doctor>> obtenerPorEspecialidad(@PathVariable String especialidad) {
        logger.info("GET /api/doctores/especialidad/{}", especialidad);
        return ResponseEntity.ok(doctorService.obtenerPorEspecialidad(especialidad));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Doctor>> obtenerDisponibles() {
        logger.info("GET /api/doctores/disponibles");
        return ResponseEntity.ok(doctorService.obtenerDisponibles());
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody DoctorDTO dto) {
        logger.info("POST /api/doctores");
        try {
            Doctor creado = doctorService.crear(dto);
            return ResponseEntity.status(201).body(creado);
        } catch (RuntimeException e) {
            logger.error("Error al crear doctor: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody DoctorDTO dto) {
        logger.info("PUT /api/doctores/{}", id);
        return doctorService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("DELETE /api/doctores/{}", id);
        if (doctorService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}