package co.edu.unicartagena.actividades.infrastructure.persistence.repositories;

import co.edu.unicartagena.actividades.domain.entities.Resultado;
import co.edu.unicartagena.actividades.domain.repositories.ResultadoRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultadoJpaRepository extends JpaRepository<Resultado,Integer>, ResultadoRepository {

    Optional<Resultado> findByIdMocion(Integer idMocion);
}
