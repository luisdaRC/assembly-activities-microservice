package co.edu.unicartagena.actividades.application.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class PropositionDTO {

    private final Integer idPropiedadHorizontal;
    private final String titulo;
    private final List<String> proposiciones;
}
