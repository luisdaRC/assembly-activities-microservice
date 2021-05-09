package co.edu.unicartagena.actividades.application.commands.persona;

import co.edu.unicartagena.actividades.application.commands.Command;
import co.edu.unicartagena.actividades.application.dtos.AsistenteDTO;
import co.edu.unicartagena.actividades.domain.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrarAbandonoCommand implements Command<String, AsistenteDTO> {

    private final PersonaService personaService;

    @Autowired
    public RegistrarAbandonoCommand(PersonaService personaService){ this.personaService=personaService; }

    public String ejecutar(AsistenteDTO asistenteDTO){
        System.out.println("Ejecutando el comando: RegistrarAbandono con data: " + asistenteDTO);
        return personaService.registrarAbandono(asistenteDTO.getIdPersona(), asistenteDTO.getIdPropiedadHorizontal());
    }
}
