package co.edu.unicartagena.actividades.application.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class PersonaDTO {

    private final Integer idPersona;
    private final String nombres;
    private final String apellidos;
    private final String numeroDocumento;
    private final String tipoDocumento;
    private final String rol;


}
