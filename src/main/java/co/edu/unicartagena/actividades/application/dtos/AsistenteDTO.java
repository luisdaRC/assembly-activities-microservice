package co.edu.unicartagena.actividades.application.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class AsistenteDTO {

    private final Integer idPersona;
    private final Integer idPropiedadHorizontal;
}
