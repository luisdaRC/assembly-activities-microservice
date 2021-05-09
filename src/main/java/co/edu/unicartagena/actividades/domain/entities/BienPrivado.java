package co.edu.unicartagena.actividades.domain.entities;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class BienPrivado {

    public BienPrivado() {
    }

    private Integer id;
    private Integer idPropiedad;
    private Float coeficienteCopropiedad;
    private String matriculaInmobiliaria;
    private String referenciaCatastral;

}
