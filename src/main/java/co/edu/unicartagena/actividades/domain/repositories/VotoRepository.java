package co.edu.unicartagena.actividades.domain.repositories;

import co.edu.unicartagena.actividades.domain.entities.Voto;

import java.util.List;
import java.util.Optional;

public interface VotoRepository {

    Optional<List<Voto>> findByIdMocion(Integer idMocion);
}
