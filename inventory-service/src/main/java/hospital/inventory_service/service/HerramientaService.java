package hospital.inventory_service.service;

import hospital.inventory_service.model.Herramienta;
import hospital.inventory_service.repository.HerramientaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HerramientaService {
    @Autowired
    private HerramientaRepository herramientaRepository;

    public List<Herramienta> findAll(){
        return herramientaRepository.findAll();

    }
    public Herramienta findById(String id){
        return herramientaRepository.findById(id).orElse(null);
    }

    public Herramienta save(Herramienta herramienta){
        return herramientaRepository.save(herramienta);
    }
    public void deleteById(String id){
        herramientaRepository.deleteById(id);
    }
}
