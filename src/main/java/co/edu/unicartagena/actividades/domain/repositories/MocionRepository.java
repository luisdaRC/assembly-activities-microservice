package co.edu.unicartagena.actividades.domain.repositories;

import co.edu.unicartagena.actividades.domain.entities.Mocion;

import java.util.List;
import java.util.Optional;

public interface MocionRepository {

    Optional<List<Mocion>> findByIdAsamblea(Integer idAsamblea);
}
