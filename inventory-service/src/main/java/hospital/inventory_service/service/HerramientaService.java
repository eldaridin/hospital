package hospital.inventory_service.service;

import hospital.inventory_service.dto.HerramientaDTO;
import hospital.inventory_service.model.Herramienta;
import hospital.inventory_service.repository.HerramientaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HerramientaService {

    private static final Logger logger = LoggerFactory.getLogger(HerramientaService.class);

    private final HerramientaRepository herramientaRepository;

    public HerramientaService(HerramientaRepository herramientaRepository) {
        this.herramientaRepository = herramientaRepository;
    }

    public List<Herramienta> findAll() {
        logger.info("Obteniendo lista de todas las herramientas");
        return herramientaRepository.findAll();
    }

    public Optional<Herramienta> findById(Integer id) {
        logger.info("Buscando herramienta con ID: {}", id);
        return herramientaRepository.findById(id);
    }

    public Herramienta saveFromDTO(HerramientaDTO dto) {
        logger.info("Creando nueva herramienta: {}", dto.getNombre());
        Herramienta herramienta = new Herramienta();
        herramienta.setNombre(dto.getNombre());
        herramienta.setTipo(dto.getTipo());
        herramienta.setExistencias(dto.getExistencias());
        herramienta.setUbicacion(dto.getUbicacion());
        Herramienta guardada = herramientaRepository.save(herramienta);
        logger.info("Herramienta creada con ID: {}", guardada.getId());
        return guardada;
    }

    public Herramienta updateFromDTO(Integer id, HerramientaDTO dto) {
        logger.info("Actualizando herramienta con ID: {}", id);
        return herramientaRepository.findById(id).map(herramienta -> {
            herramienta.setNombre(dto.getNombre());
            herramienta.setTipo(dto.getTipo());
            herramienta.setExistencias(dto.getExistencias());
            herramienta.setUbicacion(dto.getUbicacion());
            Herramienta actualizada = herramientaRepository.save(herramienta);
            logger.info("Herramienta ID: {} actualizada exitosamente", id);
            return actualizada;
        }).orElseThrow(() -> {
            logger.warn("Herramienta con ID: {} no encontrada", id);
            return new RuntimeException("Herramienta no encontrada con ID: " + id);
        });
    }

    public boolean deleteById(Integer id) {
        logger.info("Eliminando herramienta con ID: {}", id);
        if (herramientaRepository.existsById(id)) {
            herramientaRepository.deleteById(id);
            logger.info("Herramienta ID: {} eliminada exitosamente", id);
            return true;
        }
        logger.warn("Herramienta con ID: {} no encontrada para eliminar", id);
        return false;
    }
}
