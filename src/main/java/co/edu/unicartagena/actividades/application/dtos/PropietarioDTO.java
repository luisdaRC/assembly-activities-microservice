package co.edu.unicartagena.actividades.application.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PropietarioDTO {

    public PropietarioDTO(Integer idPersona, String nombres, String apellidos, String numeroDocumento, String tipoDocumento) {
        this.idPersona = idPersona;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.numeroDocumento = numeroDocumento;
        this.tipoDocumento = tipoDocumento;
    }

    private final Integer idPersona;
    private final String nombres;
    private final String apellidos;
    private final String numeroDocumento;
    private final String tipoDocumento;
}
