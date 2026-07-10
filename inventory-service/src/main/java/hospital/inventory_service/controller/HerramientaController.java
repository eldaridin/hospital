package hospital.inventory_service.controller;

import hospital.inventory_service.dto.HerramientaDTO;
import hospital.inventory_service.model.Herramienta;
import hospital.inventory_service.service.HerramientaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class HerramientaController {

    private static final Logger logger = LoggerFactory.getLogger(HerramientaController.class);

    private final HerramientaService herramientaService;

    public HerramientaController(HerramientaService herramientaService) {
        this.herramientaService = herramientaService;
    }

    @GetMapping
    public ResponseEntity<List<Herramienta>> findAll() {
        logger.info("GET /api/inventario");
        return ResponseEntity.ok(herramientaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Herramienta> findById(@PathVariable Integer id) {
        logger.info("GET /api/inventario/{}", id);
        return herramientaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody HerramientaDTO dto) {
        logger.info("POST /api/inventario");
        try {
            Herramienta creado = herramientaService.saveFromDTO(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            logger.error("Error al crear herramienta: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody HerramientaDTO dto) {
        logger.info("PUT /api/inventario/{}", id);
        try {
            Herramienta actualizado = herramientaService.updateFromDTO(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            logger.error("Error al actualizar herramienta: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        logger.info("DELETE /api/inventario/{}", id);
        if (herramientaService.deleteById(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
