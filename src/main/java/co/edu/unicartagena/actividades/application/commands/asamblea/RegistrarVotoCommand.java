package co.edu.unicartagena.actividades.application.commands.asamblea;

import co.edu.unicartagena.actividades.application.commands.Command;
import co.edu.unicartagena.actividades.domain.services.AsambleaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegistrarVotoCommand implements Command<Integer, List<String>> {

    private final AsambleaService asambleaService;
    @Autowired
    public RegistrarVotoCommand(AsambleaService asambleaService){ this.asambleaService=asambleaService; }

    public Integer ejecutar(List<String> options){
        System.out.println("Ejecutando el comando: RegistrarVoto con id de Persona: "+options);
        return asambleaService.registerVote(Integer.parseInt(options.get(0)), options.get(1), options.get(2));
    }
}
