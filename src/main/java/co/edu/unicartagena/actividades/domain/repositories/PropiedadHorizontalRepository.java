package co.edu.unicartagena.actividades.domain.repositories;

import co.edu.unicartagena.actividades.domain.entities.Asistente;
import co.edu.unicartagena.actividades.domain.entities.PropiedadHorizontal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PropiedadHorizontalRepository {

    Optional<PropiedadHorizontal> findPHById(Integer id);
    Optional<Integer> findIdSecretario(Integer idPropiedad);
    Optional<Integer> findIdAsamblea(Integer idSecretario);
    Optional<LocalDateTime> propietarioHoraLlegada(Integer idAsamblea, Integer idPersona);
    Optional<LocalDateTime> propietarioHoraSalida(Integer idAsamblea, Integer idPersona);
    Optional<Integer> saveAsistente(Integer idAsamblea, Integer idPersona, String rol, LocalDateTime horaLlegada, LocalDateTime horaSalida);
    Float findTotalCoeficiente(Integer idPropiedad);
    Integer findTotalPropietarios(Integer idPropiedad);
    Optional<Integer> mocionActiva(Integer idAsamblea);
    Optional<Integer> saveMocion(String titulo, Integer idAsamblea, Boolean estado);
    Optional<Integer> saveOpciones(Integer idMocion, String prop);
    Integer changeStatus(Integer idMocion, Boolean estado);

}
