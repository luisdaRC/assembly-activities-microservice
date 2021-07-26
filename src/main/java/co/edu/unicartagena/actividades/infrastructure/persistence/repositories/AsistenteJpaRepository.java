package co.edu.unicartagena.actividades.infrastructure.persistence.repositories;

import co.edu.unicartagena.actividades.domain.entities.Asistente;
import co.edu.unicartagena.actividades.domain.repositories.AsistenteRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AsistenteJpaRepository extends JpaRepository<Asistente,Integer>, AsistenteRepository {

    Optional<List<Asistente>> findByIdAsamblea(Integer idAsamblea);
}
