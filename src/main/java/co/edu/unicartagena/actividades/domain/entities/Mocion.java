package co.edu.unicartagena.actividades.domain.entities;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class Mocion {

    public Mocion(){
    }

    private Integer idMocion;
    private Integer idAsamblea;
    private String descripcionMocion;
    private Boolean estado;
    private String tipo;

}
