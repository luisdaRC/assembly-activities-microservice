package co.edu.unicartagena.actividades.application.commands.persona;

import co.edu.unicartagena.actividades.application.commands.Command;
import co.edu.unicartagena.actividades.application.dtos.DesignadoDTO;
import co.edu.unicartagena.actividades.domain.services.AsambleaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RegistrarPoderCedidoCommand implements Command<Integer, DesignadoDTO> {

    private final AsambleaService asambleaService;
    @Autowired
    public RegistrarPoderCedidoCommand(AsambleaService asambleaService){
        this.asambleaService=asambleaService;}

    public Integer ejecutar(DesignadoDTO designadoDTO){
        System.out.println("Ejecutando el comando: RegistrarPoderCedido con idPropiedad: "+designadoDTO);
        return asambleaService.registrarPoder(Integer.parseInt(designadoDTO.getIdPropiedadHorizontal()),
                designadoDTO.getRol(),designadoDTO.getDataPoder().getTipoDocumento(),
                designadoDTO.getDataPoder().getNumeroDocumento(), designadoDTO.getDataPoder().getNombres(),
                designadoDTO.getDataPoder().getApellidos(), designadoDTO.getDataPoder().getTipoDocumentoPropietario(),
                designadoDTO.getDataPoder().getNumeroDocumentoPropietario());
    }
}
