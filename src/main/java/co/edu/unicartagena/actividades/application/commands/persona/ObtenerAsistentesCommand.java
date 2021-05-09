package co.edu.unicartagena.actividades.application.commands.persona;
import co.edu.unicartagena.actividades.application.builders.PropietarioBuilder;
import co.edu.unicartagena.actividades.application.commands.Command;
import co.edu.unicartagena.actividades.application.dtos.PropietarioDTO;
import co.edu.unicartagena.actividades.domain.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ObtenerAsistentesCommand implements Command<List<PropietarioDTO>,String> {

    private final PersonaService personaService;
    @Autowired
    public ObtenerAsistentesCommand(PersonaService personaService){ this.personaService=personaService; }

    public List<PropietarioDTO> ejecutar(String idPropiedad){
        System.out.println("Ejecutando el comando: ObtenerAsistentes con id de PH: "+idPropiedad);
        return PropietarioBuilder.crearListPropietarioDTODesdeEntidad(personaService.obtenerAsistentes(idPropiedad));
    }
}
