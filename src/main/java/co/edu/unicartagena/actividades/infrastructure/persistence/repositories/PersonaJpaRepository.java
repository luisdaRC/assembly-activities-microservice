package co.edu.unicartagena.actividades.infrastructure.persistence.repositories;

import co.edu.unicartagena.actividades.domain.entities.Persona;
import co.edu.unicartagena.actividades.domain.repositories.PersonaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaJpaRepository extends JpaRepository<Persona,Integer>, PersonaRepository {

    @Query(value = "SELECT persona.* FROM persona " +
            "JOIN bienprivado ON persona.bienprivado_idbienprivado = bienprivado.idbienprivado " +
            "JOIN propiedadhorizontal ON bienprivado.propiedadhorizontal_idph = :idPropiedad " +
            "WHERE persona.rol = :rol " +
            "GROUP BY persona.idpersona", nativeQuery = true)
    List<Persona> findByIdPropiedadAndRol(@Param("idPropiedad") Integer idPropiedad,
                                          @Param("rol") String rol);

    @Query(value = "SELECT propiedadhorizontal_idph FROM bienprivado WHERE idbienprivado = " +
            "(SELECT bienprivado_idbienprivado FROM persona WHERE idpersona = :idPersona)", nativeQuery = true)
    Integer findIdPropiedadByIdPersona(@Param("idPersona") Integer idPersona);

    @Query(value = "SELECT persona_idpersona from asistente where horallegada = horasalida AND " +
            "rol='PROPIETARIO' AND asamblea_idasamblea = :idAsamblea", nativeQuery = true)
    Optional<List<Integer>> findAsistenteByIdAsamblea(@Param("idAsamblea") Integer idAsamblea);

    @Query(value = "SELECT persona_idpersona FROM asistente WHERE horallegada = horasalida AND " +
            "asamblea_idasamblea = :idAsamblea", nativeQuery = true)
    Optional<List<Integer>> findAllAsistentesByIdAsamblea(@Param("idAsamblea") Integer idAsamblea);


    @Query(value = "SELECT * FROM persona where idpersona = :idPersona", nativeQuery = true)
    Persona findPersonaById(@Param("idPersona") Integer idPersona);

    @Modifying
    @Query(value = "UPDATE asistente SET horasalida = :horaSalida WHERE persona_idpersona = :idPersona " +
            "AND asamblea_idasamblea = :idAsamblea", nativeQuery = true)
    Integer registrarAbandono(@Param("idPersona") Integer idPersona,
                              @Param("idAsamblea") Integer idAsamblea,
                              @Param("horaSalida") LocalDateTime horaSalida);

    @Query(value = "SELECT coeficientecopropiedad FROM bienprivado WHERE idbienprivado = " +
            "(SELECT bienprivado_idbienprivado FROM persona WHERE idpersona = :idPersona)", nativeQuery = true)
    Float findCoeficienteByIdBienPrivado(@Param("idPersona") Integer idPersona);

    @Query(value = "INSERT INTO voto VALUES (DEFAULT, :idMocion, :idOpcion, :idPersona)", nativeQuery = true)
    Integer doVote(@Param("idMocion") Integer idMocion, @Param("idOpcion") Integer idOpcion, @Param("idPersona") Integer idPersona);
}
