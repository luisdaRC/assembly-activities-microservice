package co.edu.unicartagena.actividades.application.commands.asamblea;

import co.edu.unicartagena.actividades.application.commands.Command;
import co.edu.unicartagena.actividades.domain.entities.ResultadoDetallado;
import co.edu.unicartagena.actividades.domain.services.AsambleaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GetDetailedResultsRevisorCommand implements Command<Map<Object, Object>, String > {

    private final AsambleaService asambleaService;
    @Autowired
    public GetDetailedResultsRevisorCommand(AsambleaService asambleaService){
        this.asambleaService=asambleaService;}

    public Map<Object, Object> ejecutar(String idMocion){
        System.out.println("Ejecutando el comando: GetDetailedResultsRevisor con idMocion: "+idMocion);
        return asambleaService.resultadosDetalladosRevisor(idMocion);
    }
}
