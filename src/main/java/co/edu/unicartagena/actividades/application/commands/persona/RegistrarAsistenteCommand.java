package co.edu.unicartagena.actividades.application.commands.persona;

import co.edu.unicartagena.actividades.application.commands.Command;
import co.edu.unicartagena.actividades.application.dtos.AsistenteDTO;
import co.edu.unicartagena.actividades.domain.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrarAsistenteCommand implements Command<Integer, AsistenteDTO> {

    private final PersonaService personaService;

    @Autowired
    public RegistrarAsistenteCommand(PersonaService personaService){ this.personaService=personaService; }

    public Integer ejecutar(AsistenteDTO asistenteDTO){
        System.out.println("Ejecutando el comando: RegistrarAsistente con data: " + asistenteDTO);
        return personaService.registrarAsistente(asistenteDTO.getIdPersona(), asistenteDTO.getIdPropiedadHorizontal());
    }
}
