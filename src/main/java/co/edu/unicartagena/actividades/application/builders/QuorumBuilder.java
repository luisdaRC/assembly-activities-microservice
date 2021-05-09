package co.edu.unicartagena.actividades.application.builders;

import co.edu.unicartagena.actividades.application.dtos.QuorumDTO;

import java.util.List;

public class QuorumBuilder {

    public QuorumBuilder() {}
    public static QuorumDTO crearQuorumDTODesdeList(List<String> data){
        return QuorumDTO.builder()
                .asistentes(Integer.parseInt(data.get(0)))
                .ausentes(Integer.parseInt(data.get(1)))
                .quorum(Float.parseFloat(data.get(2)))
                .build();
    }
}
