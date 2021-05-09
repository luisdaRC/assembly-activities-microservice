package co.edu.unicartagena.actividades.application.builders;

import co.edu.unicartagena.actividades.application.dtos.PersonaDTO;
import co.edu.unicartagena.actividades.domain.entities.Persona;

public class PersonaBuilder {

    public PersonaBuilder() {
    }

    public static PersonaDTO crearPersonaDTODesdeEntidad(Persona persona) {
        return PersonaDTO.builder()
                .nombres(persona.getNombres())
                .apellidos(persona.getApellidos())
                .numeroDocumento(persona.getNumeroDocumento())
                .tipoDocumento(persona.getTipoDocumento())
                .build();
    }

}
