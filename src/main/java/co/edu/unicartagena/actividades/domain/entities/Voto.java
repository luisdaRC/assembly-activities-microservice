package co.edu.unicartagena.actividades.domain.entities;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class Voto {

    public Voto(){}

    private Integer idVoto;
    private Integer idMocion;
    private Integer idOpcion;
    private Integer idPersona;
}
