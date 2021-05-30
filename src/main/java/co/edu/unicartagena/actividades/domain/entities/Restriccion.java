package co.edu.unicartagena.actividades.domain.entities;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class Restriccion {

    public Restriccion(){}

    private Integer idRestriccion;
    private Integer idPropiedad;
    private String restriccion;
}
