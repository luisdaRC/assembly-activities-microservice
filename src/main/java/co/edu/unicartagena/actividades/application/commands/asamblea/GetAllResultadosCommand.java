package co.edu.unicartagena.actividades.application.commands.asamblea;

import co.edu.unicartagena.actividades.application.commands.Command;
import co.edu.unicartagena.actividades.domain.services.AsambleaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GetAllResultadosCommand implements Command<List<Map<Object, Object>>, String > {

    private final AsambleaService asambleaService;
    @Autowired
    public GetAllResultadosCommand(AsambleaService asambleaService){
        this.asambleaService=asambleaService;}

    public List<Map<Object, Object>> ejecutar(String idPropiedad){
        System.out.println("Ejecutando el comando: GetAllResultados con idPropiedad: "+idPropiedad);
        return asambleaService.resultadosRevisor(Integer.parseInt(idPropiedad));
    }
}
