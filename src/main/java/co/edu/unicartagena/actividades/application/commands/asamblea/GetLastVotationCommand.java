package co.edu.unicartagena.actividades.application.commands.asamblea;

import co.edu.unicartagena.actividades.application.commands.Command;
import co.edu.unicartagena.actividades.domain.services.AsambleaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GetLastVotationCommand implements Command<Map<Object, Object>, String > {

    private final AsambleaService asambleaService;
    @Autowired
    public GetLastVotationCommand(AsambleaService asambleaService){
        this.asambleaService=asambleaService;}

    public Map<Object, Object> ejecutar(String idPropiedadHorizontal){
        System.out.println("Ejecutando el comando: GetLastVotation con id de Persona: "+idPropiedadHorizontal);
        return asambleaService.resultadosSecretario(Integer.parseInt(idPropiedadHorizontal));
    }


}
