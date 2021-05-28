package co.edu.unicartagena.actividades.application.commands.asamblea;

import co.edu.unicartagena.actividades.application.commands.Command;
import co.edu.unicartagena.actividades.domain.services.AsambleaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GetMocionPropietarioCommand implements Command< Map<Object, Object>, String > {

    private final AsambleaService asambleaService;
    @Autowired
    public GetMocionPropietarioCommand(AsambleaService asambleaService){
        this.asambleaService=asambleaService;}

    public Map<Object, Object> ejecutar(String idPersona){
        System.out.println("Ejecutando el comando: GetMocionPropietario con id de Persona: "+idPersona);
        return asambleaService.getMocionPropietario(Integer.parseInt(idPersona));
    }
}
