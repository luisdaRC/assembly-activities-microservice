package co.edu.unicartagena.actividades.domain.services;

import co.edu.unicartagena.actividades.domain.entities.Asistente;
import co.edu.unicartagena.actividades.domain.entities.Persona;
import co.edu.unicartagena.actividades.domain.exceptions.BusinessException;
import co.edu.unicartagena.actividades.domain.repositories.PersonaRepository;
import co.edu.unicartagena.actividades.domain.repositories.PropiedadHorizontalRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class PersonaService {

    PersonaRepository personaRepository;
    PropiedadHorizontalRepository phRepository;

    public PersonaService(PersonaRepository personaRepository,
                          PropiedadHorizontalRepository phRepository){
        this.personaRepository = personaRepository;
        this.phRepository = phRepository;
    }

    public List<Persona> obtenerPersonas(String idPropiedad){
        System.out.println("Verificando existencia de Propiedad con id en PersonaService: " + idPropiedad);
        if(!phRepository.findPHById(Integer.parseInt(idPropiedad)).isPresent())
            throw new BusinessException("La propiedad indicada no existe");

        return personaRepository.findByIdPropiedadAndRol(Integer.parseInt(idPropiedad),"PROPIETARIO");
    }

    public List<Persona> obtenerAsistentes(String idPropiedad){
        System.out.println("Verificando existencia de Propiedad con id en PersonaService: " + idPropiedad);
        if(!phRepository.findPHById(Integer.parseInt(idPropiedad)).isPresent())
            throw new BusinessException("La propiedad indicada no existe");

        if(!phRepository.findIdSecretario(Integer.parseInt(idPropiedad)).isPresent())
            throw new BusinessException("El secretario indicada no existe");

        Integer idSecretario = phRepository.findIdSecretario(Integer.parseInt(idPropiedad)).get();

        if(!phRepository.findIdAsamblea(idSecretario).isPresent())
            throw new BusinessException("No hay asamblea transcurriendo en este momento");

        Integer idAsamblea = phRepository.findIdAsamblea(idSecretario).get();

        Optional<List<Integer>> listIdPersona = personaRepository.findAsistenteByIdAsamblea(idAsamblea);
        List<Persona> asistentes = new ArrayList<>();

        if(listIdPersona.isPresent()){
            for (Integer id:listIdPersona.get()){
                asistentes.add(personaRepository.findPersonaById(id));
            }
        } else
            throw new BusinessException("No hay personas presentes en la asamblea");

        return asistentes;
    }

    public String registrarAsistente(Integer idPersona, Integer idPropiedad){

        if(!phRepository.findPHById(idPropiedad).isPresent())
            return "La propiedad indicada no existe";

        Integer idSecretario = phRepository.findIdSecretario(idPropiedad).get();

        if(!phRepository.findIdAsamblea(idSecretario).isPresent())
            return "No hay asamblea transcurriendo en este momento";

        Integer idAsamblea = phRepository.findIdAsamblea(idSecretario).get();


        Optional<LocalDateTime> horaLlegada = phRepository.propietarioHoraLlegada(idAsamblea, idPersona);
        Optional<LocalDateTime> horaSalida = phRepository.propietarioHoraSalida(idAsamblea, idPersona);

        if(horaLlegada.isPresent() && horaSalida.isPresent())
            return "El propietario ya se encuentra registrado en la asamblea.";

        LocalDateTime llegada = LocalDateTime.now();
        phRepository.saveAsistente(idAsamblea, idPersona, "PROPIETARIO", llegada, llegada);

        return "Propietario registrado correctamente.";
    }

    public String registrarAbandono(Integer idPersona, Integer idPropiedad){
        Integer idSecretario = phRepository.findIdSecretario(idPropiedad).get();
        Integer idAsamblea = phRepository.findIdAsamblea(idSecretario).get();
        LocalDateTime horaSalida = LocalDateTime.now();
        personaRepository.registrarAbandono(idPersona, idAsamblea, horaSalida);
        return "El propietario abandonó exitosamente la asamblea.";
    }

    public Map<Object, Object> verificarCandidato(String numDoc, String tipoDoc){
        Optional<Persona> persona = personaRepository.findByTipoDocumentoAndNumeroDocumento(numDoc, tipoDoc);
        Map<Object, Object> model = new HashMap<>();
        if (!persona.isPresent()){
            model.put("id", 0);
        } else if (persona.get().getMoroso()){
            model.put("id", 1);
            model.put("nombre", persona.get().getNombres() + " " + persona.get().getApellidos());
        } else {
            model.put("id", 2);
            model.put("nombre", persona.get().getNombres() + " " + persona.get().getApellidos());
        }
        return model;
    }
}
