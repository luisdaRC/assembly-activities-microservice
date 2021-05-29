package co.edu.unicartagena.actividades.domain.services;

import co.edu.unicartagena.actividades.domain.exceptions.BusinessException;
import co.edu.unicartagena.actividades.domain.repositories.PersonaRepository;
import co.edu.unicartagena.actividades.domain.repositories.PropiedadHorizontalRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class AsambleaService {

    PersonaRepository personaRepository;
    PropiedadHorizontalRepository phRepository;

    public AsambleaService(PersonaRepository personaRepository,
                          PropiedadHorizontalRepository phRepository){
        this.personaRepository = personaRepository;
        this.phRepository = phRepository;
    }

    public Integer terminarAsamblea(String idPropiedad){

            Integer idSecretario = phRepository.findIdSecretario(Integer.parseInt(idPropiedad)).get();

            Integer idAsamblea = phRepository.findIdAsamblea(idSecretario).get();

            Optional<List<Integer>> idsAsistentes = personaRepository.findAllAsistentesByIdAsamblea(idAsamblea);

            for (Integer idPersona : idsAsistentes.get()) {
                LocalDateTime horaSalida = LocalDateTime.now();
                personaRepository.registrarAbandono(idPersona, idAsamblea, horaSalida);
            }

        return 1;
    }

    public List<String> getQuorum(String idPropiedad){
        List<String> toReturn = new ArrayList<>();
        Float coeficientesAsistentes = Float.valueOf(0);

        Integer idSecretario = phRepository.findIdSecretario(Integer.parseInt(idPropiedad)).get();

        Integer idAsamblea = phRepository.findIdAsamblea(idSecretario).get();

        Optional<List<Integer>> idsAsistentes = personaRepository.findAllAsistentesByIdAsamblea(idAsamblea);

        if(idsAsistentes.isPresent()){
            toReturn.add(String.valueOf(idsAsistentes.get().size()));
            for(Integer idPersona : idsAsistentes.get())
                coeficientesAsistentes += personaRepository.findCoeficienteByIdBienPrivado(idPersona);
        }else{
            toReturn.add(String.valueOf(0));
        }

        Float totalCoeficientes = phRepository.findTotalCoeficiente(Integer.parseInt(idPropiedad));
        Integer totalPropietarios = phRepository.findTotalPropietarios(Integer.parseInt(idPropiedad));
        toReturn.add(String.valueOf(totalPropietarios-Integer.parseInt(toReturn.get(0))));
        toReturn.add(String.valueOf(coeficientesAsistentes/totalCoeficientes*100));

        System.out.println(toReturn);
        return toReturn;
    }

    public String registerProposicion(Integer idPropiedad, String titulo, List<String> proposiciones){

        Integer idSecretario = phRepository.findIdSecretario(idPropiedad).get();
        Integer idAsamblea = phRepository.findIdAsamblea(idSecretario).get();

        if(phRepository.mocionActiva(idAsamblea).isPresent())
            throw new BusinessException("Hay una moción activa en este momento en la asamblea.");

        // Agregar estado en el front, como cuando se quiere registrar secretario/revisor en administrador

        phRepository.saveMocion(titulo, idAsamblea, true);
        Integer idMocion = phRepository.mocionActiva(idAsamblea).get();

        for(String prop: proposiciones)
            phRepository.saveOpciones(idMocion, prop);

        return "La proposición ha sido guardada correctamente.";
    }

    public Boolean getExisteMocion(Integer idPropiedad){

        Integer idSecretario = phRepository.findIdSecretario(idPropiedad).get();
        Integer idAsamblea = phRepository.findIdAsamblea(idSecretario).get();
        return phRepository.mocionActiva(idAsamblea).isPresent();
    }

    public Integer detenerVotacion(Integer idPropiedad){

        Integer idSecretario = phRepository.findIdSecretario(idPropiedad).get();
        Integer idAsamblea = phRepository.findIdAsamblea(idSecretario).get();
        Integer idMocion = phRepository.mocionActiva(idAsamblea).get();

        return phRepository.changeStatus(idMocion, false);
    }

    public Map<Object, Object> getMocionPropietario(Integer idPersona){
        Optional<Integer> idAsamblea = phRepository.findIdAsambleaByIdPersona(idPersona);
        Map<Object, Object> model = new HashMap<>();

        if(idAsamblea.isPresent()) {
            Optional<Integer> idMocion = phRepository.mocionActiva(idAsamblea.get());
            if(!idMocion.isPresent()){
                model.put("mocionActiva", false);
                model.put("estado","No hay moción activa actualmente en la asamblea.");
                return model;
            }
            model.put("mocionActiva", true);
            String titulo = phRepository.findDescripcion(idMocion.get());
            model.put("titulo", titulo);
            Optional<List<String>> opciones = phRepository.findAllOpciones(idMocion.get());
            model.put("opciones", opciones.get());

        }else{
            model.put("mocionActiva", false);
            model.put("estado", "El propietario ha abandonado la asamblea.");
        }
        return model;
    }

    public Integer registerVote(Integer idPersona, String eleccion){
    //Entero para retornar distintos estados (propietario moroso, error con la bd, voto exitoso)
        /**
         * Para registrar un voto necesito el idPH (solo superadmin en localstorage)para saber qué restricciones de voto tiene la PH
         * (de admin, consejo de admin, proposiciones en gral. etc) - Esto varía según el reglamento de la propiedad.
         * Entonces, como primer paso necesito crear tabla restricciones con campos: 1.ID 2.idPh 3.Restricción
         * Luego, consultar si hay campos de restricción con ese idPH. Si no tiene, se registra el voto sin verificar restricción en moción
         * Sino, verificar si el tipo de moción de la moción actual (estado true) es el de la restricción.
         * -Si si, y el propietario es moroso, retornar que el propietario es moro y por lo tanto no puede realizar ese voto.
         * -Si no es el de la restricción, el propietario puede votar normal y se registra su voto consultando la lista de las opciones
         * con el idMocion (hacer select con list a los 3 campos de esas opciones, el objeto como tal), luego se compara la descripción
         * de cada uno de las opciones con la eleccion del parámetro de esta función xD.
         * Se toma el id de esa opción y se inserta en la tabla voto junto al idPersona del parámetro y al idMocion anteriormente consultado.
         * Listo.
         */

        return 0;
    }

}
