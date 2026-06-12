package com.hospital.appointment_service.controller;

import com.hospital.appointment_service.dto.CitaDTO;
import com.hospital.appointment_service.model.Cita;
import com.hospital.appointment_service.service.CitaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private static final Logger logger = LoggerFactory.getLogger(CitaController.class);

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    public ResponseEntity<List<Cita>> obtenerTodas() {
        logger.info("GET /api/citas");
        return ResponseEntity.ok(citaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerPorId(@PathVariable Long id) {
        logger.info("GET /api/citas/{}", id);
        return citaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Cita>> obtenerPorPaciente(@PathVariable Long pacienteId) {
        logger.info("GET /api/citas/paciente/{}", pacienteId);
        return ResponseEntity.ok(citaService.obtenerPorPaciente(pacienteId));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Cita>> obtenerPorDoctor(@PathVariable Long doctorId) {
        logger.info("GET /api/citas/doctor/{}", doctorId);
        return ResponseEntity.ok(citaService.obtenerPorDoctor(doctorId));
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody CitaDTO dto) {
        logger.info("POST /api/citas");
        try {
            Cita creada = citaService.crear(dto);
            return ResponseEntity.status(201).body(creada);
        } catch (RuntimeException e) {
            logger.error("Error al crear cita: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        logger.info("PATCH /api/citas/{}/estado", id);
        return citaService.actualizarEstado(id, estado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("DELETE /api/citas/{}", id);
        if (citaService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}