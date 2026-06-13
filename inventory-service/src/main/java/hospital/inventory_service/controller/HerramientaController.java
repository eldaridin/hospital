package hospital.inventory_service.controller;

import hospital.inventory_service.model.Herramienta;
import hospital.inventory_service.service.HerramientaService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/id")
    public Herramienta findById(String id){
        return herramientaService.findById(id);
    }
    @PostMapping
    public Herramienta save(@RequestBody Herramienta herramienta) {
        return herramientaService.save(herramienta);
    }
    @PutMapping("/{id}")
    public Herramienta update(@PathVariable String id, @RequestBody Herramienta herramienta) {
        return herramientaService.save(herramienta);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        herramientaService.deleteById(id);
    }
}
