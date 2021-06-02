package co.edu.unicartagena.actividades.domain.repositories;

import co.edu.unicartagena.actividades.domain.entities.Opcion;

import java.util.List;

public interface OpcionRepository {

    List<Opcion> findAll();
    List<Opcion> findByIdMocion(Integer idMocion);
}
