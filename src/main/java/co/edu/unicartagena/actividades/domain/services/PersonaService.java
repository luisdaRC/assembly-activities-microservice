package co.edu.unicartagena.actividades.domain.services;

import co.edu.unicartagena.actividades.domain.entities.Asistente;
import co.edu.unicartagena.actividades.domain.entities.Persona;
import co.edu.unicartagena.actividades.domain.exceptions.BusinessException;
import co.edu.unicartagena.actividades.domain.repositories.AsistenteRepository;
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
    AsistenteRepository asistenteRepository;

    public PersonaService(PersonaRepository personaRepository,
                          PropiedadHorizontalRepository phRepository,
                          AsistenteRepository asistenteRepository){
        this.personaRepository = personaRepository;
        this.asistenteRepository = asistenteRepository;
        this.phRepository = phRepository;
    }

    public List<Persona> obtenerPersonas(String idPropiedad){
        System.out.println("Verificando existencia de Propiedad con id en PersonaService: " + idPropiedad);
        if(!phRepository.findPHById(Integer.parseInt(idPropiedad)).isPresent())
            throw new BusinessException("La propiedad indicada no existe");

        List<Persona> propietarios = personaRepository.findByIdPropiedadAndRol(Integer.parseInt(idPropiedad),"PROPIETARIO");
        Integer idSecretario = phRepository.findIdSecretario(Integer.parseInt(idPropiedad)).get();
        Integer idAsamblea = phRepository.findIdAsamblea(idSecretario).get();
        Optional<List<Integer>> idsAsistentes = personaRepository.findAllAsistentesByIdAsamblea(idAsamblea);
        if(idsAsistentes.get().size() == 0){
            return propietarios;
        } else {
            List<Persona> ausentes = new LinkedList<>();
            for(Persona propietario : propietarios) {
                if (!idsAsistentes.get().contains(propietario.getIdPersona())) {
                    ausentes.add(propietario);
                }
            }
            return ausentes;
        }
    }

    public List<Persona> obtenerAsistentes(String idPropiedad){
        System.out.println("Verificando existencia de Propiedad con id en PersonaService: " + idPropiedad);
        if(!phRepository.findPHById(Integer.parseInt(idPropiedad)).isPresent())
            throw new BusinessException("La propiedad indicada no existe");

        if(!phRepository.findIdSecretario(Integer.parseInt(idPropiedad)).isPresent())
            throw new BusinessException("El secretario indicado no existe");

        Integer idSecretario = phRepository.findIdSecretario(Integer.parseInt(idPropiedad)).get();

        if(!phRepository.findIdAsamblea(idSecretario).isPresent())
            throw new BusinessException("No hay asamblea transcurriendo en este momento");

        Integer idAsamblea = phRepository.findIdAsamblea(idSecretario).get();

        Optional<List<Asistente>> listaAsistentes = asistenteRepository.findByIdAsamblea(idAsamblea);
        List<Persona> asistentes = new ArrayList<>();

        if(listaAsistentes.isPresent()){
            for(Asistente asistente: listaAsistentes.get()){
                if(asistente.getIdPersona() <= 0){
                    asistentes.add(personaRepository.findPersonaById(asistente.getIdRepresentado()));
                } else {
                    asistentes.add(personaRepository.findPersonaById(asistente.getIdPersona()));
                }
            }
        } else
            throw new BusinessException("No hay personas presentes en la asamblea");

        return asistentes;
    }

    public Integer registrarAsistente(Integer idPersona, Integer idPropiedad){

        Integer idSecretario = phRepository.findIdSecretario(idPropiedad).get();

        Optional<Integer> idAsamblea = phRepository.findIdAsamblea(idSecretario);
        if(!idAsamblea.isPresent())
            return 3;//"No hay asamblea transcurriendo en este momento"

        Float totalCoeficientes = phRepository.findTotalCoeficiente(idPropiedad);
        Integer totalPropietarios = phRepository.findTotalPropietarios(idPropiedad);

        if(totalCoeficientes.intValue() != totalPropietarios && totalCoeficientes.intValue() != 100){
            return 5;//Los coeficientes de copropiedad no est??n debidamente registrados
        }

        Optional<Asistente> existeAsistente = asistenteRepository.findByIdAsambleaAndIdPersona(idAsamblea.get(), idPersona);

        if(existeAsistente.isPresent()){
            System.out.println("Line 103 of PersonaService" + existeAsistente.get());
            LocalDateTime llegada = LocalDateTime.now();
            asistenteRepository.updateLlegadaAndSalida(existeAsistente.get().getIdAsistente(), llegada);
            return 1; //Propietario reingresado a la asamblea
        }

        Optional<List<Asistente>> listaAsistentes = asistenteRepository.findByIdAsamblea(idAsamblea.get());

        if (listaAsistentes.isPresent())
            for (Asistente asistente: listaAsistentes.get()){
                if (asistente.getIdRepresentado() == idPersona)
                    return 4; //El propietario esta siendo representado por un delegado
            }

        LocalDateTime llegada = LocalDateTime.now();
        phRepository.saveAsistente(idAsamblea.get(), idPersona, "PROPIETARIO", llegada, llegada);

        return 2;//"Propietario registrado correctamente."
    }

    public Integer registrarAbandono(Integer idPersona, Integer idPropiedad){
        Integer idSecretario = phRepository.findIdSecretario(idPropiedad).get();
        Integer idAsamblea = phRepository.findIdAsamblea(idSecretario).get();
        LocalDateTime horaSalida = LocalDateTime.now();
        personaRepository.registrarAbandono(idPersona, idAsamblea, horaSalida);
        return 1;// Asamblea abandonada con ??xito
    }

    public Map<Object, Object> verificarCandidato(String numDoc, String tipoDoc){
        Optional<Persona> persona = personaRepository.findByTipoDocumentoAndNumeroDocumento(numDoc, tipoDoc);
        Map<Object, Object> model = new HashMap<>();
        System.out.println(persona);
        if (!persona.isPresent()){
            model.put("id", 0);
        } else if (persona.get().getMoroso() != null && persona.get().getMoroso()){
            model.put("id", 1);
            model.put("nombre", persona.get().getNombres() + " " + persona.get().getApellidos());
        } else {
            model.put("id", 2);
            model.put("nombre", persona.get().getNombres() + " " + persona.get().getApellidos());
        }
        return model;
    }
}
