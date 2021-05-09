package co.edu.unicartagena.actividades.domain.entities;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class Asamblea {

    public Asamblea(){}

    private Integer idAsamblea;
    private Integer idPersonalapoyo;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

}
