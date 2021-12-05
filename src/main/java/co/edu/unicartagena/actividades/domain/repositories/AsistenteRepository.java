package co.edu.unicartagena.actividades.domain.repositories;

import co.edu.unicartagena.actividades.domain.entities.Asistente;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AsistenteRepository {

    Optional<List<Asistente>> findByIdAsamblea(Integer idAsamblea);
    Optional<Asistente> findByIdAsambleaAndIdPersona(Integer idAsamblea, Integer idPersona);
    Integer updateLlegadaAndSalida(Integer idAsistente, LocalDateTime llegada);
}
