package hospital.inventory_service.repository;

import hospital.inventory_service.model.Herramienta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HerramientaRepository extends JpaRepository<Herramienta, Integer> {
}
