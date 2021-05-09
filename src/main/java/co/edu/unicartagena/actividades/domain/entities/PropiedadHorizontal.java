package co.edu.unicartagena.actividades.domain.entities;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class PropiedadHorizontal {

    public PropiedadHorizontal() {
    }

    private Integer id;
    private String nombre;
    private String tipo;
    private String direccion;

}
