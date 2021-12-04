package co.edu.unicartagena.actividades.infrastructure.persistence.repositories;

import co.edu.unicartagena.actividades.domain.entities.Asistente;
import co.edu.unicartagena.actividades.domain.repositories.AsistenteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenteJpaRepository extends JpaRepository<Asistente,Integer>, AsistenteRepository {

    @Query(value = "SELECT * FROM asistente WHERE asamblea_idasamblea = :idAsamblea AND horallegada = horasalida", nativeQuery = true)
    Optional<List<Asistente>> findByIdAsamblea(@Param("idAsamblea") Integer idAsamblea);
}
