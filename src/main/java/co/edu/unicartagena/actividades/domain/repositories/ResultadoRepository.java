package co.edu.unicartagena.actividades.domain.repositories;

import co.edu.unicartagena.actividades.domain.entities.Resultado;

import java.util.Optional;

public interface ResultadoRepository {

    Optional<Resultado> findByIdMocion(Integer idMocion);
}
