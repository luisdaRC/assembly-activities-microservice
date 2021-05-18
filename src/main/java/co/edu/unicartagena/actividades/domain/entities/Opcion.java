package co.edu.unicartagena.actividades.domain.entities;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class Opcion {

    public Opcion(){
    }

    private Integer idOpcion;
    private Integer idMocion;
    private String descripcion;
}
