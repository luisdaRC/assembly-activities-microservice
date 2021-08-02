package co.edu.unicartagena.actividades.application.commands.asamblea;

import co.edu.unicartagena.actividades.application.commands.Command;
import co.edu.unicartagena.actividades.domain.services.AsambleaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AsambleaActivaCommand implements Command<Integer, String> {

    private final AsambleaService asambleaService;
    @Autowired
    public AsambleaActivaCommand(AsambleaService asambleaService){ this.asambleaService=asambleaService; }

    public Integer ejecutar(String idPropiedad){
        System.out.println("Ejecutando el comando: AsambleaActiva con id de PH: "+idPropiedad);
        return asambleaService.asambleaActiva(Integer.parseInt(idPropiedad));
    }
}
