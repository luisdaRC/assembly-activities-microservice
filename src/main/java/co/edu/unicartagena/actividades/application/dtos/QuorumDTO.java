package co.edu.unicartagena.actividades.application.dtos;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class QuorumDTO {

    private final Integer asistentes;
    private final Integer ausentes;
    private final Float quorum;
}
