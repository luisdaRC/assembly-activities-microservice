package co.edu.unicartagena.actividades.domain.entities;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class Persona {

    public Persona() {
    }

    private Integer idPersona;
    private Integer idBienPrivado;
    private String nombres;
    private String apellidos;
    private String numeroDocumento;
    private String tipoDocumento;
    private String rol;
}