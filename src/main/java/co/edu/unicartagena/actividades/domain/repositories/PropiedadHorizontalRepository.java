package co.edu.unicartagena.actividades.domain.repositories;

import co.edu.unicartagena.actividades.domain.entities.*;

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
    Optional<Mocion> findMocionActivaObject(Integer idAsamblea);
    Optional<Integer> saveMocion(String tipo, String titulo, Integer idAsamblea, Boolean estado);
    Optional<Integer> saveOpciones(Integer idMocion, String prop);
    Integer changeStatus(Integer idMocion, Boolean estado);
    Optional<Integer> findIdAsambleaByIdPersona(Integer idPersona);
    String findDescripcion(Integer idMocion);
    Optional<List<String>> findAllOpciones(Integer idMocion);
    Optional<List<Opcion>> findAllObjectOpciones(Integer idMocion);
    Optional<String> findRestrictionByIdPH(Integer idPropiedad);
    Optional<List<Mocion>> findAllCurrentMociones(Integer idAsamblea);
    Optional<List<Voto>> findAllVotos(Integer idMocion);
    Optional<Integer> saveResultados(Integer idMocion, String opciones, String coeficientes, String personasPorOpcion);

}
