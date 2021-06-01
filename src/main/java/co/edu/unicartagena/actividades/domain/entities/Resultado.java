package co.edu.unicartagena.actividades.domain.entities;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class Resultado {

    public Resultado(){}

    private Integer idResultado;
    private Integer idMocion;
    private String descripcionesMociones;
    private String coeficientesPorOpcion;
    private String personasPorOpcion;
}
