package co.edu.unicartagena.actividades.application.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class DesignadoDTO {
    private final String idPropiedadHorizontal;
    private final String rol;
    private final DataDesignadoDTO dataPoder;
}
