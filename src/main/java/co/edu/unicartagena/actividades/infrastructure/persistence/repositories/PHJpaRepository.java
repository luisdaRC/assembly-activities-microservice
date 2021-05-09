package co.edu.unicartagena.actividades.infrastructure.persistence.repositories;

import co.edu.unicartagena.actividades.domain.entities.Asistente;
import co.edu.unicartagena.actividades.domain.entities.PropiedadHorizontal;
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

}
