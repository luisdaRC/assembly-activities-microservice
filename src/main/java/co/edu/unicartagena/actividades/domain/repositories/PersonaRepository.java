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
    Integer findIdPropiedadByIdPersona(Integer idPersona);
    Integer doVote(Integer idMocion, Integer idOpcion, Integer idPersona);
    Optional<Persona> findByTipoDocumentoAndNumeroDocumento(String tipoDoc, String numDoc);
    Integer saveDataDelegado(Integer id, Integer idBienPrivado, String tipo, String  numero, String  nombres, String  apellidos, String rol, Boolean moroso);
    Integer saveDelegadoAsistente(Integer idAsamblea, Integer idDelegado, Integer idRepresentado, String rol, LocalDateTime llegada, LocalDateTime salida);
    Persona save(Persona persona);

}
