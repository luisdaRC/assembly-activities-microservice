package co.edu.unicartagena.actividades.domain.entities;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class ResultadoSimple {

    public ResultadoSimple(){}

    private String descripcion;
    private Integer numeroVotos;
    private Float coeficientes;
}
