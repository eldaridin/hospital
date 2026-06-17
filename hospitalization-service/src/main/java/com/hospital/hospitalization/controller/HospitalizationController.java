package com.hospital.hospitalization.controller;

import com.hospital.hospitalization.model.Hospitalization;
import com.hospital.hospitalization.service.HospitalizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hospitalizations")
@Tag(name = "Hospitalization Management", description = "Endpoints para la gestión de ingresos y altas médicas")
public class HospitalizationController {

    private final HospitalizationService service;

    public HospitalizationController(HospitalizationService service) {
        this.service = service;
    }

    @Operation(summary = "Registrar ingreso médico", description = "Asigna una habitación e inicia el proceso de hospitalización")
    @PostMapping("/admission")
    public ResponseEntity<Hospitalization> admitPatient(@RequestBody Hospitalization hospitalization) {
        return new ResponseEntity<>(service.registerAdmission(hospitalization), HttpStatus.CREATED);
    }

    @Operation(summary = "Registrar alta médica", description = "Finaliza la estadía del paciente y libera la habitación")
    @PutMapping("/{id}/discharge")
    public ResponseEntity<Hospitalization> dischargePatient(@PathVariable Long id) {
        return ResponseEntity.ok(service.processDischarge(id));
    }

    @Operation(summary = "Ver hospitalizaciones activas", description = "Lista todos los pacientes actualmente ingresados")
    @GetMapping("/active")
    public ResponseEntity<List<Hospitalization>> getActive() {
        return ResponseEntity.ok(service.getActiveHospitalizations());
    }

    @Operation(summary = "Historial por paciente", description = "Obtiene los registros de ingresos de un usuario específico")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Hospitalization>> getHistory(@PathVariable Long patientId) {
        return ResponseEntity.ok(service.getPatientHistory(patientId));
    }
}