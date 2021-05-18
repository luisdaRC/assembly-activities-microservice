package co.edu.unicartagena.actividades.domain.services;

import co.edu.unicartagena.actividades.domain.exceptions.BusinessException;
import co.edu.unicartagena.actividades.domain.repositories.PersonaRepository;
import co.edu.unicartagena.actividades.domain.repositories.PropiedadHorizontalRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

// 1. Okay, ya tengo el idAsamblea, ahora consultar si hay una moción con estado true en esa asamblea para poder insertar sin problema
// 2. Insertar en tabla mocion con el título y un estado true. Ver documento de interaces para saber cuando se debe terminar una votación y poder insertar otra proposición
// 3. Consultar el id de esa moción recién insertada con el id asamblea y el estado true para insertar las proposiciones en si (la lista)
// 4. Insertar cada una de las descripciones de las proposiciones con el idMocion en tabla opcion
// 5. Listo.

        return "";
    }


}
