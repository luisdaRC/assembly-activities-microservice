package co.edu.unicartagena.actividades.domain.entities;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class Asistente {

    public Asistente(){}

    private Integer idAsistente;
    private Integer idAsamblea;
    private Integer idPersona;
    private Integer idRepresentado;
    private String rol;
    private LocalDateTime horaLlegada;
    private LocalDateTime horaSalida;
}
