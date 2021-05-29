package co.edu.unicartagena.actividades.application.commands.asamblea;

import co.edu.unicartagena.actividades.application.commands.Command;
import co.edu.unicartagena.actividades.application.dtos.PropositionDTO;
import co.edu.unicartagena.actividades.application.dtos.VotoDTO;
import co.edu.unicartagena.actividades.domain.services.AsambleaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrarVotoCommand implements Command<Integer, VotoDTO> {

    private final AsambleaService asambleaService;
    @Autowired
    public RegistrarVotoCommand(AsambleaService asambleaService){ this.asambleaService=asambleaService; }

    public Integer ejecutar(VotoDTO voto){
        System.out.println("Ejecutando el comando: RegistrarVoto con id de Persona: "+voto.getIdPersona());
        return asambleaService.registerVote(voto.getIdPersona(), voto.getEleccion());
    }
}
