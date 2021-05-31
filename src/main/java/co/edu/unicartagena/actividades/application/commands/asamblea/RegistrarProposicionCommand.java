package co.edu.unicartagena.actividades.application.commands.asamblea;

import co.edu.unicartagena.actividades.application.commands.Command;
import co.edu.unicartagena.actividades.application.dtos.PropositionDTO;
import co.edu.unicartagena.actividades.domain.services.AsambleaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrarProposicionCommand implements Command<String, PropositionDTO> {

    private final AsambleaService asambleaService;
    @Autowired
    public RegistrarProposicionCommand(AsambleaService asambleaService){ this.asambleaService=asambleaService; }

    public String ejecutar(PropositionDTO proposiciones){
        System.out.println("Ejecutando el comando: RegistrarProposicion con id de PH: "+proposiciones);
        return asambleaService.registerProposition(proposiciones.getIdPropiedadHorizontal(),
                proposiciones.getTipo(),
                proposiciones.getTitulo(),
                proposiciones.getProposiciones());
    }
}
