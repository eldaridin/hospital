package com.hospital.pharmacy.controller;

import com.hospital.pharmacy.model.Medication;
import com.hospital.pharmacy.service.PharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pharmacy")
@Tag(name = "Pharmacy Management", description = "Endpoints para el control de inventario de farmacia")
public class PharmacyController {

    private final PharmacyService service;

    public PharmacyController(PharmacyService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos los medicamentos", description = "Retorna el listado completo disponible en bodega")
    @GetMapping
    public ResponseEntity<List<Medication>> listAll() {
        return ResponseEntity.ok(service.getAllMedications());
    }

    @Operation(summary = "Registrar un medicamento", description = "Agrega un nuevo fármaco al catálogo general")
    @ApiResponse(responseCode = "201", description = "Medicamento guardado con éxito")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Medication medication) {
        try {
            return new ResponseEntity<>(service.saveMedication(medication), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Dispensar stock por receta", description = "Disminuye las unidades disponibles tras una atención médica")
    @ApiResponse(responseCode = "200", description = "Stock actualizado correctamente")
    @PostMapping("/dispense")
    public ResponseEntity<?> dispense(@RequestParam String code, @RequestParam Integer quantity) {
        try {
            return ResponseEntity.ok(service.dispenseMedication(code, quantity));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}