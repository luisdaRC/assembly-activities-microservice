package co.edu.unicartagena.actividades.application.commands.asamblea;

import co.edu.unicartagena.actividades.application.commands.Command;
import co.edu.unicartagena.actividades.domain.services.AsambleaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DetenerVotacionCommand implements Command<Integer, String> {

    private final AsambleaService asambleaService;
    @Autowired
    public DetenerVotacionCommand(AsambleaService asambleaService){ this.asambleaService=asambleaService; }

    public Integer ejecutar(String idPropiedad){
        System.out.println("Ejecutando el comando: ExisteMocion con id de PH: "+idPropiedad);
        return asambleaService.detenerVotacion(Integer.parseInt(idPropiedad));
    }
}
