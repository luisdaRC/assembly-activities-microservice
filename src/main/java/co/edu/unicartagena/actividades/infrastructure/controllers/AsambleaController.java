package co.edu.unicartagena.actividades.infrastructure.controllers;

import co.edu.unicartagena.actividades.application.commands.asamblea.*;
import co.edu.unicartagena.actividades.application.commands.persona.RegistrarPoderCedidoCommand;
import co.edu.unicartagena.actividades.application.commands.persona.VerificarCandidatoCommand;
import co.edu.unicartagena.actividades.application.dtos.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
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
    GetLastVotationCommand getLastVotationCommand;
    VerificarCandidatoCommand verificarCandidatoCommand;
    GetVotationPropietarioCommand getVotationPropietarioCommand;
    GetAllResultadosCommand getAllResultadosCommand;
    RegistrarPoderCedidoCommand registrarPoderCedidoCommand;
    AsambleaActivaCommand asambleaActivaCommand;
    GetDetailedResultsRevisorCommand getDetailedResultsRevisorCommand;

    @Autowired
    AsambleaController(TerminarAsambleaCommand terminarAsambleaCommand,
                       GetQuorumCommand getQuorumCommand,
                       RegistrarProposicionCommand registrarProposicionCommand,
                       ExisteMocionCommand existeMocionCommand,
                       DetenerVotacionCommand detenerVotacionCommand,
                       GetMocionPropietarioCommand getMocionPropietarioCommand,
                       RegistrarVotoCommand registrarVotoCommand,
                       GetLastVotationCommand getLastVotationCommand,
                       VerificarCandidatoCommand verificarCandidatoCommand,
                       GetVotationPropietarioCommand getVotationPropietarioCommand,
                       GetAllResultadosCommand getAllResultadosCommand,
                       RegistrarPoderCedidoCommand registrarPoderCedidoCommand,
                       AsambleaActivaCommand asambleaActivaCommand,
                       GetDetailedResultsRevisorCommand getDetailedResultsRevisorCommand){
        this.terminarAsambleaCommand = terminarAsambleaCommand;
        this.getQuorumCommand = getQuorumCommand;
        this.registrarProposicionCommand = registrarProposicionCommand;
        this.existeMocionCommand = existeMocionCommand;
        this.detenerVotacionCommand = detenerVotacionCommand;
        this.getMocionPropietarioCommand = getMocionPropietarioCommand;
        this.registrarVotoCommand = registrarVotoCommand;
        this.getLastVotationCommand = getLastVotationCommand;
        this.verificarCandidatoCommand = verificarCandidatoCommand;
        this.getVotationPropietarioCommand = getVotationPropietarioCommand;
        this.getAllResultadosCommand = getAllResultadosCommand;
        this.registrarPoderCedidoCommand = registrarPoderCedidoCommand;
        this.asambleaActivaCommand = asambleaActivaCommand;
        this.getDetailedResultsRevisorCommand = getDetailedResultsRevisorCommand;
    }

    @Operation(summary = "Termina asamblea con un id de propiedad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asamblea finalizada correctamente",
                    content = { @Content(mediaType = "Integer",
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "1")) }),
            @ApiResponse(responseCode = "404", description = "Asamblea no encontrada con el id proporcionado",
                    content = { @Content(mediaType = "Integer",
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "0")) }),
            @ApiResponse(responseCode = "500", description = "No se pudo procesar la petición",
                    content = @Content) })
    @GetMapping(value="terminar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> terminarAsamblea(
            @RequestParam(name = "idPropiedadHorizontal") String idPropiedad){

        try {
            Integer response = terminarAsambleaCommand.ejecutar(idPropiedad);
            if (response == 1) return ResponseEntity.ok().body(response);
            else return ResponseEntity.status(404).body(response);
        }catch(Exception e){
            return ResponseEntity.status(500).body("No se pudo procesar la petición. "+e.getMessage());
        }
    }

    @Operation(summary = "Consulta si hay una asamblea en curso en la propiedad horizontal proporcionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hay una asamblea activa",
                    content = { @Content(mediaType = "Integer",
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "1")) }),
            @ApiResponse(responseCode = "200", description = "No hay una asamblea activa actualmente para la propiedad suministrada",
                    content = { @Content(mediaType = "Integer",
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "0")) }),
            @ApiResponse(responseCode = "500", description = "No se pudo procesar la petición",
                    content = @Content)})
    @GetMapping(value="activa", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> asambleaActiva(
            @RequestParam(name = "idPropiedadHorizontal") String idPropiedad){
        try {
            return ResponseEntity.ok().body(asambleaActivaCommand.ejecutar(idPropiedad));
        } catch (Exception e){
            return ResponseEntity.status(500).body("No se pudo procesar la petición. "+e.getMessage());
        }
    }

    @Operation(summary = "Consulta el quorum para la asamblea actualmente transcurriendo en la propiedad proporcionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hay una asamblea activa",
                    content = { @Content(mediaType = "List<number>",
                            schema = @Schema(implementation = List.class),
                            examples = @ExampleObject(value = "[ [0] asistentes:int, [1] ausentes:int, [2] quorum:float ]")) }),
            @ApiResponse(responseCode = "500", description = "No se pudo procesar la petición",
                    content = @Content)})
    @GetMapping(value="quorum", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getQuorum(
            @RequestParam(name = "idPropiedadHorizontal") String idPropiedad){

        try {
            // [0]asistentes:int, [1] ausentes:int, [2] quorum:float.
            return ResponseEntity.ok().body(getQuorumCommand.ejecutar(idPropiedad));
        }catch(Exception e){
            return ResponseEntity.status(500).body("Ha ocurrido un error al obtener el quorum. "+e.getMessage());
        }
    }

    @Operation(summary = "Recibe y guarda en la base de datos una proposición para una asamblea de la asamblea proporcionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proposición correctamente guardada",
                    content = { @Content(mediaType = "String",
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "1")) }),
            @ApiResponse(responseCode = "200", description = "Hay una moción activa actualmente",
                    content = { @Content(mediaType = "String",
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "2")) }),
            @ApiResponse(responseCode = "200", description = "Los coeficientes de copropiedad no están debidamente registrados",
                    content = { @Content(mediaType = "String",
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "3")) }),
            @ApiResponse(responseCode = "500", description = "No se pudo procesar la petición",
                    content = @Content)})
    @PostMapping(value="proposition", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrarProposicion(
            @RequestBody PropositionDTO propositions){

        try {
            return ResponseEntity.ok().body(registrarProposicionCommand.ejecutar(propositions));
        }catch(Exception e){
            return ResponseEntity.status(500).body("Ha ocurrido un error al registrar la proposición. "+e.getMessage());
        }
    }

    @Operation(summary = "Consulta si en la asamblea actualmente transcurriendo existe una moción activa para la propiedad proporcionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hay una moción activa",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{ existeMocion: true }")) }),
            @ApiResponse(responseCode = "200", description = "No hay una moción activa",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{ existeMocion: false }")) }),
            @ApiResponse(responseCode = "500", description = "No se pudo procesar la petición",
                    content = @Content)})
    @GetMapping(value="mocion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getExisteMocion(
            @RequestParam(name = "idPropiedadHorizontal") String idPropiedad){

        try {
            boolean exist = existeMocionCommand.ejecutar(idPropiedad);
            Map<Object, Object> model = new HashMap<>();
            model.put("existeMocion", exist);
            return ResponseEntity.ok().body(model);
        }catch(Exception e){
            return ResponseEntity.status(500).body("Ha ocurrido un error al consultar si existe una moción. "+e.getMessage());
        }
    }

    @Operation(summary = "Detiene votación para la moción actualmente transcurriendo en la asamblea")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "No hay votos aún para la moción ó hay uno solo",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{ result: 0 }")) }),
            @ApiResponse(responseCode = "200", description = "Votaciones detenidas y resultados guardados correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{ result: 1 }")) }),
            @ApiResponse(responseCode = "500", description = "No se pudo procesar la petición",
                    content = @Content)})
    @GetMapping(value="detener/votacion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> detenerVotacion(
            @RequestParam(name = "idPropiedadHorizontal") String idPropiedad){

        try {
            Integer value = detenerVotacionCommand.ejecutar(idPropiedad);
            Map<Object, Object> model = new HashMap<>();
            model.put("result", value);
            return ResponseEntity.ok().body(model);
        } catch(Exception e){
            return ResponseEntity.status(500).body("Ha ocurrido un error al detener la votación. "+e.getMessage());
        }
    }

    @Operation(summary = "Consulta la última moción registrada por el secretario de la asamblea")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "No hay moción activa actualmente en la asamblea",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{ mocionActiva: false, estado: string }")) }),
            @ApiResponse(responseCode = "200", description = "Última moción registrada en la asamblea",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{ mocionActiva: true, estado: string, titulo: string, esPlancha boolean, opciones: List<String> }")) }),
            @ApiResponse(responseCode = "500", description = "No se pudo procesar la petición",
                    content = @Content)})
    @GetMapping(value="mocionPropietario", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getMocionPropietario(
            @RequestParam(name = "idPersona") String idPersona){
        try {
             Map<Object, Object> model = new HashMap<>();
             model = getMocionPropietarioCommand.ejecutar(idPersona);
             System.out.println(model);
             return ResponseEntity.ok().body(model);
        }catch(Exception e){
            return ResponseEntity.status(500).body("Ha ocurrido un error al obtener mociones. "+e.getMessage());
        }
    }

    @Operation(summary = "Registra voto para la moción actual registrada por el secretario de la asamblea")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hay una restricción que le impide votar",
                    content = { @Content(mediaType = "Integer",
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "0")) }),
            @ApiResponse(responseCode = "200", description = "Voto registrado correctamente",
                    content = { @Content(mediaType = "Integer",
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "1")) }),
            @ApiResponse(responseCode = "200", description = "El propietario no está presente en la asamblea",
                    content = { @Content(mediaType = "Integer",
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "2")) }),
            @ApiResponse(responseCode = "200", description = "El propietario ya ha votado",
                    content = { @Content(mediaType = "Integer",
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "3")) }),
            @ApiResponse(responseCode = "500", description = "No se pudo procesar la petición",
                    content = @Content)})
    @PostMapping(value="voto", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrarVoto(
            @RequestBody VotoDTO voto, HttpServletRequest request){

            List options = new LinkedList<String>();
            options.add(voto.getIdPersona().toString());
            options.add(voto.getEleccion());
            options.add(request.getRemoteAddr());
            try {
                return ResponseEntity.ok().body(registrarVotoCommand.ejecutar(options));
            }catch(Exception e){
                return ResponseEntity.status(500).body("No se pudo registrar el voto. "+e.getMessage());
            }

    }

    @Operation(summary = "Obtiene los resultados de la última votación para mostrarlos al secretario de la asamblea")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "No se han registrado mociones en la asamblea",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{ hayMocion: false }")) }),
            @ApiResponse(responseCode = "200", description = "Resultados de la última votación de la asamblea",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{ hayMocion: true, esPlancha: boolean, titulo: string, resultados?: List<ResultadoSimple>, descripciones: List<String>, votosPorOpcion: List<Integer>, coeficientesPorOpcion: List<Float> }")) }),
            @ApiResponse(responseCode = "500", description = "No se pudo procesar la petición",
                    content = @Content)})
    @GetMapping(value="obtener/votacion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getLastVotation(
            @RequestParam(name = "idPropiedadHorizontal") String idPropiedadHorizontal){
        
        try{
            Map<Object, Object> model = new HashMap<>();
            model = getLastVotationCommand.ejecutar(idPropiedadHorizontal);
            System.out.println(model);
            return ResponseEntity.ok().body(model);
        }catch (Exception e){
            return ResponseEntity.status(500).body("No se pudo procesar la petición. "+e.getMessage());
        }
    }

    @Operation(summary = "Obtiene los resultados de todas las votaciones para mostrarlos al personal de apoyo de la propiedad horizontal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultados de la última votación de la asamblea",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{ titulo: string, idMocion: Integer, esPlancha: boolean, resultados?: List<Resultado>, descripciones: List<String>, votosPorOpcion: List<Integer>, coeficientesPorOpcion: List<Float> }")) }),
            @ApiResponse(responseCode = "500", description = "No se pudo procesar la petición",
                    content = @Content)})
    @GetMapping(value="results/revisor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllResultados(
            @RequestParam(name = "idPropiedadHorizontal") String idPropiedadHorizontal){

            List<Map<Object, Object>> model = new LinkedList<>();
            model = getAllResultadosCommand.ejecutar(idPropiedadHorizontal);
            System.out.println(model);
        try {
            return ResponseEntity.ok().body(model);
        }catch (Exception e){
            return ResponseEntity.status(500).body("No se pudo procesar la petición. "+e.getMessage());
        }
    }

    @Operation(summary = "Obtiene los resultados de todas las votaciones detalladamente para mostrarlos al revisor fiscal de la propiedad horizontal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultados detallados de todas las votaciones de la asamblea",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{ tituloMocion: string, resultados: List<ResultadoDetallado> }")) }),
            @ApiResponse(responseCode = "500", description = "No se pudo procesar la petición",
                    content = @Content)})
    @GetMapping(value="results/detailed/revisor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getDetailedResultsRevisor(
            @RequestParam(name = "idMocion") String idMocion){

        try{
            return ResponseEntity.ok().body(getDetailedResultsRevisorCommand.ejecutar(idMocion));
        }catch (Exception e){
            return ResponseEntity.status(500).body("No se pudo procesar la petición. "+e.getMessage());
        }

    }

    @Operation(summary = "Obtiene los resultados de la última votación para mostrarlos a propietarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "El propietario no se encuentra participando en la asamblea",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{ ausente: true,  esPlancha: false,  message: string }")) }),
            @ApiResponse(responseCode = "200", description = "Resultados de la última votación para propietario",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{ hayMocion: true, esPlancha: boolean, titulo: string, resultados?: List<ResultadoSimple>, descripciones: List<String>, votosPorOpcion: List<Integer>, coeficientesPorOpcion: List<Float> }")) }),
            @ApiResponse(responseCode = "500", description = "No se pudo procesar la petición",
                    content = @Content)})
    @GetMapping(value="results/votacion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getVotationPropietario(
            @RequestParam(name = "idPersona") String idPersona){

            try{
                Map<Object, Object> model = new HashMap<>();
                model = getVotationPropietarioCommand.ejecutar(idPersona);
                System.out.println(model);
                return ResponseEntity.ok().body(model);
            }catch(Exception e){
                return ResponseEntity.status(500).body("No se pudo procesar la petición. "+e.getMessage());
            }
    }

    @Operation(summary = "Verifica si un candidato puede postularse a la asamblea basado en su estado de moroso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Los datos proporcionados no corresponden a un propietario registrado en el sistema",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{ id: 0 }")) }),
            @ApiResponse(responseCode = "200", description = "El propietario proporcionado es moroso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{ id: 1, nombre: string }")) }),
            @ApiResponse(responseCode = "200", description = "Propietario apto para postulación",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{ id: 2, nombre: string }")) }),
            @ApiResponse(responseCode = "500", description = "No se pudo procesar la petición",
                    content = @Content)})
    @PostMapping(value="verificarCandidato", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> verificarCandidato(
            @RequestBody DocumentoDTO documentoDTO){

        try{
            return ResponseEntity.ok().body(verificarCandidatoCommand.ejecutar(documentoDTO));
        }catch(Exception e){
            return ResponseEntity.status(500).body("No se pudo procesar la petición. "+e.getMessage());
        }

    }

    @Operation(summary = "Registra poder cedido por un propietario a un apoderado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Los datos proporcionados no corresponden a un propietario registrado en el sistema",
                    content = { @Content(mediaType = "Integer",
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "0")) }),
            @ApiResponse(responseCode = "200", description = "Poder registrado satisfactoriamente",
                    content = { @Content(mediaType = "Integer",
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "1")) }),
            @ApiResponse(responseCode = "200", description = "Ocurrió un error al intentar el poder",
                    content = { @Content(mediaType = "Integer",
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "2")) }),
            @ApiResponse(responseCode = "200", description = "El propietario ya se encuentra registrado como asistente",
                    content = { @Content(mediaType = "Integer",
                            schema = @Schema(implementation = Integer.class),
                            examples = @ExampleObject(value = "3")) }) })
    @PostMapping(value="poder", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrarPoderCedido(
            @RequestBody DesignadoDTO designadoDTO){
            return ResponseEntity.ok().body(registrarPoderCedidoCommand.ejecutar(designadoDTO));
    }

}
