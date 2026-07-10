package com.hospital.hospitalization.controller;

import com.hospital.hospitalization.dto.HospitalizationDTO;
import com.hospital.hospitalization.model.Hospitalization;
import com.hospital.hospitalization.service.HospitalizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hospitalizations")
@Tag(name = "Hospitalization Management", description = "Endpoints para la gestion de ingresos y altas medicas")
public class HospitalizationController {

    private static final Logger logger = LoggerFactory.getLogger(HospitalizationController.class);

    private final HospitalizationService service;

    public HospitalizationController(HospitalizationService service) {
        this.service = service;
    }

    @Operation(summary = "Registrar ingreso medico", description = "Asigna una habitacion e inicia el proceso de hospitalizacion")
    @PostMapping("/admission")
    public ResponseEntity<?> admitPatient(@Valid @RequestBody HospitalizationDTO dto) {
        logger.info("POST /api/v1/hospitalizations/admission - Ingresando paciente ID: {}", dto.getPatientId());
        try {
            Hospitalization creado = service.registerAdmissionFromDTO(dto);
            return new ResponseEntity<>(creado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Error al ingresar paciente: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Registrar alta medica", description = "Finaliza la estadia del paciente y libera la habitacion")
    @PutMapping("/{id}/discharge")
    public ResponseEntity<?> dischargePatient(@PathVariable Long id) {
        logger.info("PUT /api/v1/hospitalizations/{}/discharge", id);
        try {
            return ResponseEntity.ok(service.processDischarge(id));
        } catch (RuntimeException e) {
            logger.error("Error al dar alta ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Ver hospitalizaciones activas", description = "Lista todos los pacientes actualmente ingresados")
    @GetMapping("/active")
    public ResponseEntity<List<Hospitalization>> getActive() {
        logger.info("GET /api/v1/hospitalizations/active");
        return ResponseEntity.ok(service.getActiveHospitalizations());
    }

    @Operation(summary = "Historial por paciente", description = "Obtiene los registros de ingresos de un usuario especifico")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Hospitalization>> getHistory(@PathVariable Long patientId) {
        logger.info("GET /api/v1/hospitalizations/patient/{}", patientId);
        return ResponseEntity.ok(service.getPatientHistory(patientId));
    }
}
