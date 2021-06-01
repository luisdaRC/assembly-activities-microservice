package co.edu.unicartagena.actividades.infrastructure.controllers;

import co.edu.unicartagena.actividades.application.commands.asamblea.*;
import co.edu.unicartagena.actividades.application.dtos.AsistenteDTO;
import co.edu.unicartagena.actividades.application.dtos.PropositionDTO;
import co.edu.unicartagena.actividades.application.dtos.VotoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/asamblea")
public class AsambleaController {

    TerminarAsambleaCommand terminarAsambleaCommand;
    RegistrarProposicionCommand registrarProposicionCommand;
    GetQuorumCommand getQuorumCommand;
    ExisteMocionCommand existeMocionCommand;
    DetenerVotacionCommand detenerVotacionCommand;
    GetMocionPropietarioCommand getMocionPropietarioCommand;
    RegistrarVotoCommand registrarVotoCommand;

    @Autowired
    AsambleaController(TerminarAsambleaCommand terminarAsambleaCommand,
                       GetQuorumCommand getQuorumCommand,
                       RegistrarProposicionCommand registrarProposicionCommand,
                       ExisteMocionCommand existeMocionCommand,
                       DetenerVotacionCommand detenerVotacionCommand,
                       GetMocionPropietarioCommand getMocionPropietarioCommand,
                       RegistrarVotoCommand registrarVotoCommand){
        this.terminarAsambleaCommand = terminarAsambleaCommand;
        this.getQuorumCommand = getQuorumCommand;
        this.registrarProposicionCommand = registrarProposicionCommand;
        this.existeMocionCommand = existeMocionCommand;
        this.detenerVotacionCommand = detenerVotacionCommand;
        this.getMocionPropietarioCommand = getMocionPropietarioCommand;
        this.registrarVotoCommand = registrarVotoCommand;
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

    @GetMapping(value="mocion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getExisteMocion(
            @RequestParam(name = "idPropiedadHorizontal") String idPropiedad){

        try {
            boolean exist = existeMocionCommand.ejecutar(idPropiedad);
            Map<Object, Object> model = new HashMap<>();
            model.put("existeMocion", exist);
            return ResponseEntity.ok().body(model);
        }catch(Exception e){
            return ResponseEntity.ok().body("Ha ocurrido un error al obtener el quorum. "+e.getMessage());
        }
    }

    @GetMapping(value="detener/votacion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> detenerVotacion(
            @RequestParam(name = "idPropiedadHorizontal") String idPropiedad){

        try {
            Integer value = detenerVotacionCommand.ejecutar(idPropiedad);
            Map<Object, Object> model = new HashMap<>();
            model.put("result", value);
            return ResponseEntity.ok().body(model);
        }catch(Exception e){
            return ResponseEntity.ok().body("Ha ocurrido un error al obtener el quorum. "+e.getMessage());
        }
    }

    @GetMapping(value="mocionPropietario", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getMocionPropietario(
            @RequestParam(name = "idPersona") String idPersona){
        try {
             Map<Object, Object> model = new HashMap<>();
             model = getMocionPropietarioCommand.ejecutar(idPersona);
             System.out.println(model);
             return ResponseEntity.ok().body(model);
        }catch(Exception e){
            return ResponseEntity.ok().body("Ha ocurrido un error al obtener mociones. "+e.getMessage());
        }
    }

    @PostMapping(value="voto", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrarVoto(
            @RequestBody VotoDTO voto){

        try {
            return ResponseEntity.ok().body(registrarVotoCommand.ejecutar(voto));
        }catch(Exception e){
            return ResponseEntity.ok().body("Ha ocurrido un error al registrar el voto. "+e.getMessage());
        }
    }

    @GetMapping(value="obtener/votacion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getLastVotation(
            @RequestParam(name = "idPropiedadHorizontal") String idPropiedadHorizontal){
        try {
            Map<Object, Object> model = new HashMap<>();
            model = getLastVotationCommand.ejecutar(idPropiedadHorizontal);
            System.out.println(model);
            return ResponseEntity.ok().body(model);
        }catch(Exception e){
            return ResponseEntity.ok().body("Ha ocurrido un error al obtener mociones. "+e.getMessage());
        }
    }

}
