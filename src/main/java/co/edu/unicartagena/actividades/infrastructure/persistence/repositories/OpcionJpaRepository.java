package co.edu.unicartagena.actividades.infrastructure.persistence.repositories;

import co.edu.unicartagena.actividades.domain.entities.Opcion;
import co.edu.unicartagena.actividades.domain.repositories.OpcionRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpcionJpaRepository extends JpaRepository<Opcion,Integer>, OpcionRepository {

    List<Opcion> findAll();
    List<Opcion> findByIdMocion(Integer idMocion);
}
