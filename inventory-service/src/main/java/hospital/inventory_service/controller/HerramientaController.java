package hospital.inventory_service.controller;

import hospital.inventory_service.model.Herramienta;
import hospital.inventory_service.service.HerramientaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class HerramientaController {
    @Autowired
    private HerramientaService herramientaService;

    @GetMapping
    public List<Herramienta> findAll() {
        return herramientaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Herramienta> findById(@PathVariable Integer id) {
        Herramienta herramienta = herramientaService.findById(id);
        if (herramienta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(herramienta);
    }


    @PostMapping
    public Herramienta save(@RequestBody Herramienta herramienta) {
        return herramientaService.save(herramienta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Herramienta> update(@PathVariable Integer id, @RequestBody Herramienta herramienta) {
        herramienta.setId(id);
        return ResponseEntity.ok(herramientaService.save(herramienta));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        herramientaService.deleteById(id);
    }
}
