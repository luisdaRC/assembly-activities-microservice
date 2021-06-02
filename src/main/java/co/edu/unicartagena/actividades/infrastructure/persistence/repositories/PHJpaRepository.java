package co.edu.unicartagena.actividades.infrastructure.persistence.repositories;

import co.edu.unicartagena.actividades.domain.entities.*;
import co.edu.unicartagena.actividades.domain.repositories.PropiedadHorizontalRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PHJpaRepository extends JpaRepository<PropiedadHorizontal,Integer>, PropiedadHorizontalRepository {

    @Query(value = "SELECT * FROM propiedadhorizontal WHERE idpropiedadhorizontal = :id", nativeQuery = true)
    Optional<PropiedadHorizontal> findPHById(@Param("id") Integer id);

    @Query(value = "SELECT idpersonalapoyo FROM personalapoyo WHERE propiedadhorizontal_idph = :id" +
            " AND rol = 'SECRETARIO' AND estado = true", nativeQuery = true)
    Optional<Integer> findIdSecretario(@Param("id") Integer id);

    @Query(value = "SELECT idasamblea FROM asamblea WHERE personalapoyo_idpa = :idSecretario AND fechainicio = fechafin", nativeQuery = true)
    Optional<Integer> findIdAsamblea(@Param("idSecretario") Integer idSecretario);

    @Query(value = "SELECT horallegada FROM asistente WHERE horallegada = " +
            "(SELECT MAX(horallegada) FROM asistente WHERE persona_idpersona = :idPersona AND asamblea_idasamblea = :idAsamblea)", nativeQuery = true)
    Optional<LocalDateTime> propietarioHoraLlegada(@Param("idAsamblea") Integer idAsamblea, @Param("idPersona") Integer idPersona);

    @Query(value = "SELECT horasalida FROM asistente WHERE horallegada = " +
            "(SELECT MAX(horallegada) FROM asistente WHERE persona_idpersona = :idPersona AND asamblea_idasamblea = :idAsamblea)", nativeQuery = true)
    Optional<LocalDateTime> propietarioHoraSalida(@Param("idAsamblea") Integer idAsamblea, @Param("idPersona") Integer idPersona);

    @Modifying
    @Query(value = "INSERT INTO asistente VALUES (DEFAULT, :idAsamblea, :idPersona, :rol, :horaLlegada, :horaSalida)", nativeQuery = true)
    @Transactional
    Optional<Integer> saveAsistente(@Param("idAsamblea") Integer idAsamblea, @Param("idPersona") Integer idPersona,
                            @Param("rol") String rol, @Param("horaLlegada") LocalDateTime horaLlegada,
                            @Param("horaSalida") LocalDateTime horaSalida);

    @Query(value = "SELECT sum(coeficientecopropiedad) FROM bienprivado WHERE propiedadhorizontal_idph = :idPropiedad", nativeQuery = true)
    Float findTotalCoeficiente(@Param("idPropiedad") Integer idPropiedad);

    @Query(value = "SELECT count(*) FROM bienprivado WHERE propiedadhorizontal_idph = :idPropiedad", nativeQuery = true)
    Integer findTotalPropietarios(@Param("idPropiedad") Integer idPropiedad);

    @Query(value = "SELECT idmocion FROM mocion WHERE asamblea_idasamblea = :idAsamblea AND estado = true", nativeQuery = true)
    Optional<Integer> mocionActiva(@Param("idAsamblea") Integer idAsamblea);

    @Query(value = "SELECT tipo FROM mocion WHERE asamblea_idasamblea = :idAsamblea AND estado = true", nativeQuery = true)
    Optional<String> findTipoMocionActiva(@Param("idAsamblea") Integer idAsamblea);

    @Modifying
    @Query(value = "INSERT INTO mocion VALUES (DEFAULT, :idAsamblea, :titulo, :estado, :tipo)", nativeQuery = true)
    @Transactional
    Optional<Integer> saveMocion(@Param("tipo") String tipo, @Param("titulo") String titulo, @Param("idAsamblea") Integer idAsamblea, @Param("estado") Boolean estado);

    @Modifying
    @Query(value = "INSERT INTO opcion VALUES (DEFAULT, :idMocion, :prop)", nativeQuery = true)
    @Transactional
    Optional<Integer> saveOpciones(@Param("idMocion") Integer idMocion, @Param("prop") String prop);

    @Modifying
    @Query(value = "UPDATE mocion SET estado = :estado WHERE idmocion = :idMocion", nativeQuery = true)
    Integer changeStatus(@Param("idMocion") Integer idMocion, @Param("estado") Boolean estado);

    @Query(value = "SELECT asamblea_idasamblea FROM asistente WHERE persona_idpersona = :idPersona " +
            "AND horallegada = horasalida", nativeQuery = true)
    Optional<Integer> findIdAsambleaByIdPersona(@Param("idPersona") Integer idPersona);

    @Query(value = "SELECT descripcionmocion FROM mocion WHERE idmocion = :idMocion", nativeQuery = true)
    String findDescripcion(@Param("idMocion") Integer idMocion);

    @Query(value = "SELECT descripcion FROM opcion WHERE mocion_idmocion = :idMocion", nativeQuery = true)
    Optional<List<String>> findAllOpciones(@Param("idMocion") Integer idMocion);

    @Query(value = "SELECT * FROM opcion WHERE mocion_idmocion = :idMocion", nativeQuery = true)
    Optional<List<Opcion>> findAllObjectOpciones(@Param("idMocion") Integer idMocion);

    @Query(value = "SELECT restriccion FROM restriccion WHERE propiedadhorizontal_idph = :idPropiedad", nativeQuery = true)
    Optional<String> findRestrictionByIdPH(@Param("idPropiedad") Integer idPropiedad);

    @Modifying
    @Query(value = "INSERT INTO resultado VALUES (DEFAULT, :idMocion, :opciones, :coeficientes, :personasPorOpcion)", nativeQuery = true)
    @Transactional
    Optional<Integer> saveResultados(@Param("idMocion") Integer idMocion,
                                     @Param("opciones") String opciones,
                                     @Param("coeficientes") String coeficientes,
                                     @Param("personasPorOpcion") String personasPorOpcion);

    @Query(value = "SELECT idvoto FROM voto WHERE persona_idpersona = :idPersona AND mocion_idmocion = :idMocion", nativeQuery = true)
    Optional<Integer> votoPropietario(@Param("idPersona") Integer idPersona, @Param("idMocion") Integer idMocion);
}
