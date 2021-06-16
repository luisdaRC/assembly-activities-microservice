package co.edu.unicartagena.actividades.infrastructure.persistence.repositories;

import co.edu.unicartagena.actividades.domain.entities.Asistente;
import co.edu.unicartagena.actividades.domain.repositories.AsistenteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsistenteJpaRepository extends JpaRepository<Asistente,Integer>, AsistenteRepository {
    Asistente save(Asistente asistente);
}
