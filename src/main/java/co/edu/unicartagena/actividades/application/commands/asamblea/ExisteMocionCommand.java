package co.edu.unicartagena.actividades.application.commands.asamblea;

import co.edu.unicartagena.actividades.application.commands.Command;
import co.edu.unicartagena.actividades.domain.services.AsambleaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExisteMocionCommand implements Command<Boolean, String> {

    private final AsambleaService asambleaService;
    @Autowired
    public ExisteMocionCommand(AsambleaService asambleaService){ this.asambleaService=asambleaService; }

    public Boolean ejecutar(String idPropiedad){
        System.out.println("Ejecutando el comando: ExisteMocion con id de PH: "+idPropiedad);
        return asambleaService.getExisteMocion(Integer.parseInt(idPropiedad));
    }
}
