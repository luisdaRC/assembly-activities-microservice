package co.edu.unicartagena.actividades.application.commands.persona;

import co.edu.unicartagena.actividades.application.commands.Command;
import co.edu.unicartagena.actividades.application.dtos.AsistenteDTO;
import co.edu.unicartagena.actividades.application.dtos.DocumentoDTO;
import co.edu.unicartagena.actividades.domain.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VerificarCandidatoCommand implements Command<Map<Object, Object>, DocumentoDTO> {

    private final PersonaService personaService;

    @Autowired
    public VerificarCandidatoCommand(PersonaService personaService){ this.personaService=personaService; }

    public Map<Object, Object> ejecutar(DocumentoDTO documentoDTO){
        System.out.println("Ejecutando el comando: VerificarCandidato con data: " + documentoDTO);
        return personaService.verificarCandidato(documentoDTO.getTipo(), documentoDTO.getNumero());
    }
}
