package com.hospital.pharmacy.controller;

import com.hospital.pharmacy.dto.MedicationDTO;
import com.hospital.pharmacy.model.Medication;
import com.hospital.pharmacy.service.PharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pharmacy")
@Tag(name = "Pharmacy Management", description = "Endpoints para el control de inventario de farmacia")
public class PharmacyController {

    private static final Logger logger = LoggerFactory.getLogger(PharmacyController.class);

    private final PharmacyService service;

    public PharmacyController(PharmacyService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos los medicamentos", description = "Retorna el listado completo disponible en bodega")
    @GetMapping
    public ResponseEntity<List<Medication>> listAll() {
        logger.info("GET /api/v1/pharmacy");
        return ResponseEntity.ok(service.getAllMedications());
    }

    @Operation(summary = "Registrar un medicamento", description = "Agrega un nuevo farmaco al catalogo general")
    @ApiResponse(responseCode = "201", description = "Medicamento guardado con exito")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody MedicationDTO dto) {
        logger.info("POST /api/v1/pharmacy - Creando medicamento: {}", dto.getName());
        try {
            Medication creado = service.saveFromDTO(dto);
            return new ResponseEntity<>(creado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Error al crear medicamento: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Dispensar stock por receta", description = "Disminuye las unidades disponibles tras una atencion medica")
    @ApiResponse(responseCode = "200", description = "Stock actualizado correctamente")
    @PostMapping("/dispense")
    public ResponseEntity<?> dispense(@RequestParam String code, @RequestParam Integer quantity) {
        logger.info("POST /api/v1/pharmacy/dispense - Code: {}, Quantity: {}", code, quantity);
        try {
            return ResponseEntity.ok(service.dispenseMedication(code, quantity));
        } catch (IllegalStateException e) {
            logger.error("Error al dispensar: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
