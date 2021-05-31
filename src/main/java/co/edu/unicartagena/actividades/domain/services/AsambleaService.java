package co.edu.unicartagena.actividades.domain.services;

import co.edu.unicartagena.actividades.domain.entities.Mocion;
import co.edu.unicartagena.actividades.domain.entities.Opcion;
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

    public String registerProposition(Integer idPropiedad, String tipo, String titulo, List<String> proposiciones){

        Integer idSecretario = phRepository.findIdSecretario(idPropiedad).get();
        Integer idAsamblea = phRepository.findIdAsamblea(idSecretario).get();

        if(phRepository.mocionActiva(idAsamblea).isPresent())
            throw new BusinessException("Hay una moción activa en este momento en la asamblea.");

        // Agregar estado en el front, como cuando se quiere registrar secretario/revisor en administrador

        phRepository.saveMocion(tipo, titulo, idAsamblea, true);
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

        try {
            Integer idPropiedad = personaRepository.findIdPropiedadByIdPersona(idPersona);
            // Voy a asumir que las restricciones ya están registradas.
            Optional<String> restricciones = phRepository.findRestrictionByIdPH(idPropiedad);
            if (restricciones.isPresent()) {
                //Verificar el tipo de la moción activa
                Optional<Integer> idSecretario = phRepository.findIdSecretario(idPropiedad);
                Optional<Integer> idAsamblea = phRepository.findIdAsamblea(idSecretario.get());
                Optional<Mocion> mocion = phRepository.findMocionActivaObject(idAsamblea.get());
                String tipoMocion = mocion.get().getTipo();
                Persona persona = personaRepository.findPersonaById(idPersona);
                if (persona.getMoroso() && restricciones.get().equals("TODAS"))
                    return 0;//Hay una restricción que le impide votar
                if (restricciones.get().contains(tipoMocion) && persona.getMoroso())
                    return 0;

                Optional<List<Opcion>> opciones = phRepository.findAllObjectOpciones(mocion.get().getIdMocion());
                for (Opcion op : opciones.get())
                    if (op.getDescripcion().equals(eleccion)) {
                        personaRepository.doVote(mocion.get().getIdMocion(), op.getIdOpcion(), idPersona);
                        break;
                    }
                return 1; //Voto exitoso

            } else {
                //Guardar voto
                Optional<Integer> idSecretario = phRepository.findIdSecretario(idPropiedad);
                Optional<Integer> idAsamblea = phRepository.findIdAsamblea(idSecretario.get());
                Optional<Integer> idMocion = phRepository.mocionActiva(idAsamblea.get());
                Optional<List<Opcion>> opciones = phRepository.findAllObjectOpciones(idMocion.get());
                for (Opcion op : opciones.get())
                    if (op.getDescripcion().equals(eleccion)) {
                        personaRepository.doVote(idMocion.get(), op.getIdOpcion(), idPersona);
                        break;
                    }
                return 1; //Voto exitoso
            }
        }catch (Exception e){
            System.out.println("Excepción en método votar");
            return 2;//Error al intentar registrar el voto. Consulte con su administrador.
        }
    }

    public Integer resultadosSecretario(Integer idPropiedad){//O id secretario mejor
        // 1. Obtener el id de la última moción (el mayor id de la lista de mociones de esa asamblea)
        // 2. Obtener la descripción o título de esa moción para mostrar en front
        // 3. Obtener la lista de objetos de voto de esa moción (Ahí también se tiene el idPropietario para coeficiente)
        // 4. Mostrar dos gráficas: 1. Votación individual y 2. Votación con coeficientes
        // Crear tabla resultados con llave foranea de moción que contenga los datos de aquí
        // (en Strings, total de votos por opción y total de coeficiente acumulado por opción, con las opciones en string also)
        // para evitar muchas consultas

        return 0;
    }

}
