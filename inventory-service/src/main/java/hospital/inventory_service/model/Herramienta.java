package hospital.inventory_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Herramienta {
    private int id;
    private String nombre;
    private String tipo;
    private int existencias;
    private String ubicacion;
}
