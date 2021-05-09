package co.edu.unicartagena.actividades.domain.repositories;

import co.edu.unicartagena.actividades.domain.entities.Persona;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PersonaRepository {

    List<Persona> findByIdPropiedadAndRol(Integer idPropiedad, String rol);
    Optional<List<Integer>> findAsistenteByIdAsamblea(Integer idAsamblea);
    Optional<List<Integer>> findAllAsistentesByIdAsamblea(Integer idAsamblea);
    Persona findPersonaById(Integer idPersona);
    Integer registrarAbandono(Integer idPersona, Integer idAsamblea, LocalDateTime horaSalida);
    Float findCoeficienteByIdBienPrivado(Integer idPersona);
}
