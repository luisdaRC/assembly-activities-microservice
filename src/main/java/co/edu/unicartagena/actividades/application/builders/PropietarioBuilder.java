package co.edu.unicartagena.actividades.application.builders;

import co.edu.unicartagena.actividades.application.dtos.PropietarioDTO;
import co.edu.unicartagena.actividades.domain.entities.Persona;

import java.util.ArrayList;
import java.util.List;

public class PropietarioBuilder {

    public PropietarioBuilder(){
    }

    public static List<PropietarioDTO> crearListPropietarioDTODesdeEntidad(List<Persona> personaList){
        List<PropietarioDTO> listPropietarioDTO = new ArrayList<>();

        for (Persona persona:personaList) {
            listPropietarioDTO.add(
                new PropietarioDTO(
                    persona.getIdPersona(),
                    persona.getNombres(),
                    persona.getApellidos(),
                    persona.getNumeroDocumento(),
                    persona.getTipoDocumento())
            );
        }
        return listPropietarioDTO;
    }
}
