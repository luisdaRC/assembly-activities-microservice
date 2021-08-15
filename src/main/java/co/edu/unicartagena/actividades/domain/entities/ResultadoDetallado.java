package co.edu.unicartagena.actividades.domain.entities;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class ResultadoDetallado {

    public ResultadoDetallado(){}

    private String nombres;
    private String documento;
    private String eleccion;
    private String coeficiente;
    private String direccionIp;
}
