package co.edu.unicartagena.actividades.infrastructure.controllers;

import co.edu.unicartagena.actividades.application.commands.persona.ObtenerAsistentesCommand;
import co.edu.unicartagena.actividades.application.commands.persona.ObtenerPropietariosCommand;
import co.edu.unicartagena.actividades.application.commands.persona.RegistrarAsistenteCommand;
import co.edu.unicartagena.actividades.application.commands.persona.RegistrarAbandonoCommand;
import co.edu.unicartagena.actividades.application.dtos.AsistenteDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persona")
public class PersonaController {

    ObtenerPropietariosCommand obtenerPropietariosCommand;
    ObtenerAsistentesCommand obtenerAsistentesCommand;
    RegistrarAsistenteCommand registrarAsistenteCommand;
    RegistrarAbandonoCommand registrarAbandonoCommand;

    @Autowired
    PersonaController(ObtenerPropietariosCommand obtenerPropietariosCommand,
                      RegistrarAsistenteCommand registrarAsistenteCommand,
                      ObtenerAsistentesCommand obtenerAsistentesCommand,
                      RegistrarAbandonoCommand registrarAbandonoCommand){
        this.obtenerPropietariosCommand = obtenerPropietariosCommand;
        this.registrarAsistenteCommand = registrarAsistenteCommand;
        this.obtenerAsistentesCommand = obtenerAsistentesCommand;
        this.registrarAbandonoCommand = registrarAbandonoCommand;
    }

    @Operation(summary = "Obtiene la lista de todos los propietarios de la propiedad horizontal")
    @GetMapping(value = "listar", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> listaPropietarios(
            @RequestParam(name = "idPropiedadHorizontal") String idPropiedad){

        System.out.println("Entra a listaPropietarios");
        return ResponseEntity.ok().body(obtenerPropietariosCommand.ejecutar(idPropiedad));
    }

    @Operation(summary = "Registra un nuevo propietario en la asamblea")
    @PostMapping(value="asistente", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrarAsistente(
            @RequestBody AsistenteDTO data){

        //try {
            return ResponseEntity.ok().body(registrarAsistenteCommand.ejecutar(data));
        /*}catch(Exception e){
            return ResponseEntity.ok().body("Ha ocurrido un error al registrar el asistente. "+e.getMessage());
        }*/
    }

    @Operation(summary = "Obtiene la lista de los asistentes de la asamblea")
    @GetMapping(value = "listarAsistentes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> listaAsistentes(
            @RequestParam(name = "idPropiedadHorizontal") String idPropiedad){

        System.out.println("Entra a listarAsistentes");
        try {
            return ResponseEntity.ok().body(obtenerAsistentesCommand.ejecutar(idPropiedad));
        }catch(Exception e){
            return ResponseEntity.ok().body("Error: "+e.getMessage());
        }
    }

    @Operation(summary = "Registra abandono de un asistente de la asamblea")
    @PostMapping(value="abandona", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registrarAbandono(
            @RequestBody AsistenteDTO data){

        try {
            return ResponseEntity.ok().body(registrarAbandonoCommand.ejecutar(data));
        }catch(Exception e){
            return ResponseEntity.ok().body("Ha ocurrido un error al registrar el asistente. "+e.getMessage());
        }
    }
}
