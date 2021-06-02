package co.edu.unicartagena.actividades.infrastructure.persistence.repositories;

import co.edu.unicartagena.actividades.domain.entities.Voto;
import co.edu.unicartagena.actividades.domain.repositories.VotoRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VotoJpaRepository extends JpaRepository<Voto,Integer>, VotoRepository {

    Optional<List<Voto>> findByIdMocion(Integer idMocion);

}
