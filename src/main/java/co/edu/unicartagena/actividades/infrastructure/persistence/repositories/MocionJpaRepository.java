package co.edu.unicartagena.actividades.infrastructure.persistence.repositories;

import co.edu.unicartagena.actividades.domain.entities.Mocion;
import co.edu.unicartagena.actividades.domain.repositories.MocionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MocionJpaRepository extends JpaRepository<Mocion,Integer>, MocionRepository {

    Optional<List<Mocion>> findByIdAsamblea(Integer idAsamblea);
    Mocion findByIdMocion(Integer idMocion);
}
