package co.edu.unicartagena.actividades.infrastructure.controllers;

import co.edu.unicartagena.actividades.application.commands.asamblea.RegistrarProposicionCommand;
import co.edu.unicartagena.actividades.application.commands.asamblea.TerminarAsambleaCommand;
import co.edu.unicartagena.actividades.application.commands.asamblea.GetQuorumCommand;
import co.edu.unicartagena.actividades.application.dtos.AsistenteDTO;
import co.edu.unicartagena.actividades.application.dtos.PropositionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/asamblea")
public class AsambleaController {

    TerminarAsambleaCommand terminarAsambleaCommand;
    RegistrarProposicionCommand registrarProposicionCommand;
    GetQuorumCommand getQuorumCommand;

    @Autowired
    AsambleaController(TerminarAsambleaCommand terminarAsambleaCommand,
                       GetQuorumCommand getQuorumCommand,
                       RegistrarProposicionCommand registrarProposicionCommand){
        this.terminarAsambleaCommand = terminarAsambleaCommand;
        this.getQuorumCommand = getQuorumCommand;
        this.registrarProposicionCommand = registrarProposicionCommand;
    }

    @GetMapping(value="terminar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> terminarAsamblea(
            @RequestParam(name = "idPropiedadHorizontal") String idPropiedad){

        try {
            return ResponseEntity.ok().body(terminarAsambleaCommand.ejecutar(idPropiedad));
        }catch(Exception e){
            return ResponseEntity.ok().body("Ha ocurrido un error al terminar la asamblea. "+e.getMessage());
        }
    }

    @GetMapping(value="quorum", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getQuorum(
            @RequestParam(name = "idPropiedadHorizontal") String idPropiedad){

        try {
            // [0]asistentes:int, [1] ausentes:int, [2] quorum:float.
            return ResponseEntity.ok().body(getQuorumCommand.ejecutar(idPropiedad));
        }catch(Exception e){
            return ResponseEntity.ok().body("Ha ocurrido un error al obtener el quorum. "+e.getMessage());
        }
    }

    @PostMapping(value="proposition", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrarAsistente(
            @RequestBody PropositionDTO propositions){

        try {
            return ResponseEntity.ok().body(registrarProposicionCommand.ejecutar(propositions));
        }catch(Exception e){
            return ResponseEntity.ok().body("Ha ocurrido un error al registrar la proposición. "+e.getMessage());
        }
    }


}
