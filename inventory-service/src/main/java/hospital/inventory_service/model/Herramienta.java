package hospital.inventory_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "herramientas")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Herramienta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String tipo;
    private int existencias;
    private String ubicacion;
}
