package co.edu.unicartagena.actividades.application.commands.asamblea;

import co.edu.unicartagena.actividades.application.builders.QuorumBuilder;
import co.edu.unicartagena.actividades.application.commands.Command;
import co.edu.unicartagena.actividades.application.dtos.QuorumDTO;
import co.edu.unicartagena.actividades.domain.services.AsambleaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetQuorumCommand implements Command<QuorumDTO, String> {

    private final AsambleaService asambleaService;
    @Autowired
    public GetQuorumCommand(AsambleaService asambleaService){ this.asambleaService=asambleaService; }

    public QuorumDTO ejecutar(String idPropiedad){
        System.out.println("Ejecutando el comando: GetQuorum con id de PH: "+idPropiedad);
        return QuorumBuilder.crearQuorumDTODesdeList(asambleaService.getQuorum(idPropiedad));
    }
}
