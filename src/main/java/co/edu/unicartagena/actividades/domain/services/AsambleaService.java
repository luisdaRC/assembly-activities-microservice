package co.edu.unicartagena.actividades.domain.services;

import co.edu.unicartagena.actividades.domain.entities.*;
import co.edu.unicartagena.actividades.domain.exceptions.BusinessException;
import co.edu.unicartagena.actividades.domain.repositories.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class AsambleaService {

    PersonaRepository personaRepository;
    PropiedadHorizontalRepository phRepository;
    OpcionRepository opcionRepository;
    MocionRepository mocionRepository;
    ResultadoRepository resultadoRepository;
    VotoRepository votoRepository;

    public AsambleaService(PersonaRepository personaRepository,
                          PropiedadHorizontalRepository phRepository,
                           OpcionRepository opcionRepository,
                           MocionRepository mocionRepository,
                           ResultadoRepository resultadoRepository,
                           VotoRepository votoRepository){
        this.personaRepository = personaRepository;
        this.phRepository = phRepository;
        this.opcionRepository = opcionRepository;
        this.mocionRepository = mocionRepository;
        this.resultadoRepository = resultadoRepository;
        this.votoRepository = votoRepository;
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

        return "1";
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

        //Cuantos votos por cada opción y a cuanto equivale en porcentajes de coeficiente de copropiedad cada opción
        Optional<List<Voto>> votos = votoRepository.findByIdMocion(idMocion);
        if(!votos.isPresent() || votos.get().size()<2){
            return 0;//No hay votos aún para la moción ó hay uno solo.
        }
        phRepository.changeStatus(idMocion, false);

        Map<Integer, List<Integer>> votosPorOpcion = new HashMap<>();
        //Votos por opción
        for (Voto voto: votos.get()){
            List<Integer> idsPersonas = new LinkedList<>();
            if(!votosPorOpcion.containsKey(voto.getIdOpcion())){
                idsPersonas.add(voto.getIdPersona());
                votosPorOpcion.put(voto.getIdOpcion(), idsPersonas);
            }else{
                idsPersonas = votosPorOpcion.get(voto.getIdOpcion());
                idsPersonas.add(voto.getIdPersona());
                votosPorOpcion.replace(voto.getIdOpcion(), idsPersonas);
            }
        }

        Map<Integer, Float> coeficientesPorOpcion = new HashMap<>();
        Map<Integer, Integer> personasPorOpcion = new HashMap<>();
        Set<Integer> idsOpciones = new HashSet<>();
        idsOpciones = votosPorOpcion.keySet();

        for(Integer key: idsOpciones){
            List<Integer> idsPersonas = new LinkedList<>();
            idsPersonas = votosPorOpcion.get(key);
            Float coeficientesPersonas = Float.valueOf(0);

            for(Integer idPersona: idsPersonas){
                coeficientesPersonas += personaRepository.findCoeficienteByIdBienPrivado(idPersona);
            }
            coeficientesPorOpcion.put(key, coeficientesPersonas);
            personasPorOpcion.put(key, votosPorOpcion.get(key).size());
        }

        List<Opcion> opciones = opcionRepository.findByIdMocion(idMocion);
        String listaDescripcionOpciones = "";
        String listaCoeficientes = "";
        String listaPersonasPorOpcion = "";
        Integer cont = 0;

        for(Integer id: idsOpciones){
            listaDescripcionOpciones += opciones.get(cont).getDescripcion() + "#CustmSpace#";
            listaCoeficientes += coeficientesPorOpcion.get(id).toString() + ",";
            listaPersonasPorOpcion += personasPorOpcion.get(id).toString() + ",";
            cont++;
        }

        phRepository.saveResultados(idMocion, listaDescripcionOpciones, listaCoeficientes, listaPersonasPorOpcion);

        return 1;
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
            if(opciones.get().get(0).startsWith("Plancha")){
                model.put("esPlancha",true);
            } else {
                model.put("esPlancha", false); // Esto para mostrar opciones a propietario
            }
            model.put("opciones", opciones.get());

        }else{
            model.put("mocionActiva", false);
            model.put("estado", "El propietario ha abandonado la asamblea.");
        }
        return model;
    }

    public Integer registerVote(Integer idPersona, String eleccion){
    //Entero para retornar distintos estados (propietario moroso, error con la bd, voto exitoso)

       // try {
            Integer idPropiedad = personaRepository.findIdPropiedadByIdPersona(idPersona);
            // Voy a asumir que las restricciones ya están registradas.
            Optional<String> restricciones = phRepository.findRestrictionByIdPH(idPropiedad);

            if (restricciones.isPresent()) {
                //Verificar el tipo de la moción activa
                Optional<Integer> idSecretario = phRepository.findIdSecretario(idPropiedad);
                Optional<Integer> idAsamblea = phRepository.findIdAsamblea(idSecretario.get());
                Optional<Integer> idMocion = phRepository.mocionActiva(idAsamblea.get());
                Optional<Integer> votado = phRepository.votoPropietario(idPersona, idMocion.get());
                if(votado.isPresent())
                    return 3;

                Optional<String> tipoMocion = phRepository.findTipoMocionActiva(idAsamblea.get());
                Persona persona = personaRepository.findPersonaById(idPersona);
                if (persona.getMoroso() && restricciones.get().equals("TODAS"))
                    return 0;//Hay una restricción que le impide votar
                if(tipoMocion.isPresent())
                    if (restricciones.get().contains(tipoMocion.get()) && persona.getMoroso())
                        return 0;

                List<Opcion> opciones = opcionRepository.findByIdMocion(idMocion.get());
                for (Opcion op : opciones)
                    if (op.getDescripcion().equals(eleccion)) {
                        personaRepository.doVote(idMocion.get(), op.getIdOpcion(), idPersona);
                        break;
                    }
                return 1; //Voto exitoso

            } else {
                //Guardar voto
                Optional<Integer> idSecretario = phRepository.findIdSecretario(idPropiedad);
                Optional<Integer> idAsamblea = phRepository.findIdAsamblea(idSecretario.get());
                Optional<Integer> idMocion = phRepository.mocionActiva(idAsamblea.get());
                Optional<Integer> votado = phRepository.votoPropietario(idPersona, idMocion.get());
                if(votado.isPresent())
                    return 3;
                List<Opcion> opciones = opcionRepository.findByIdMocion(idMocion.get());
                for (Opcion op : opciones)
                    if (op.getDescripcion().equals(eleccion)) {
                        personaRepository.doVote(idMocion.get(), op.getIdOpcion(), idPersona);
                        break;
                    }
                return 1; //Voto exitoso
            }
    /*    }catch (Exception e){
            System.out.println("Excepción en método votar. "+e.getMessage());
            return 2;//Error al intentar registrar el voto. Consulte con su administrador.
        }*/
    }

    public Map<Object, Object> resultadosSecretario(Integer idPropiedad){
        Integer idSecretario = phRepository.findIdSecretario(idPropiedad).get();
        Integer idAsamblea = phRepository.findIdAsamblea(idSecretario).get();
        return getAllResults(idAsamblea);
    }

    public Map<Object, Object> resultadosPropietario(Integer idPersona){
        Optional<Integer> idAsamblea = phRepository.findIdAsambleaByIdPersona(idPersona);
        return getAllResults(idAsamblea.get());
    }

    public Map<Object, Object> getAllResults(Integer idAsamblea){
        Optional<List<Mocion>> currentMociones = mocionRepository.findByIdAsamblea(idAsamblea);
        Integer ultimoId = 0;
        String titulo = "";

        if(!currentMociones.isPresent()){
            Map<Object, Object> model = new HashMap<>();
            model.put("hayMocion", false);
            return model;
        }
        Map<Object, Object> model = new HashMap<>();

        for(Mocion mocion: currentMociones.get()){
            if(mocion.getIdMocion() > ultimoId){
                ultimoId = mocion.getIdMocion();
                titulo = mocion.getDescripcionMocion();
            }
        }

        Optional<Resultado> resultado = resultadoRepository.findByIdMocion(ultimoId);

        List<String> opciones = new ArrayList();
        List<Integer> votosPorOpcion = new ArrayList();
        List<Float> coeficientesPorVoto = new ArrayList();

        if(resultado.get().getDescripcionesMociones().startsWith("Plancha")){
            model.put("esPlancha", true);
        } else {
            model.put("esPlancha", false);
        }
        opciones = Arrays.asList(resultado.get().getDescripcionesMociones().split("#CustmSpace#"));
        model.put("titulo", titulo);
        model.put("descripciones", opciones);

        //Getting votes by option from db
        opciones = Arrays.asList(resultado.get().getPersonasPorOpcion().split(","));
        for (String nVotos: opciones){
            votosPorOpcion.add(Integer.parseInt(nVotos));
        }
        model.put("votosPorOpcion", votosPorOpcion);

        //Getting coeficientes by option
        opciones = Arrays.asList(resultado.get().getCoeficientesPorOpcion().split(","));
        Float total = Float.valueOf(0);
        for (String coeficientes: opciones){
            total += Float.parseFloat(coeficientes);
        }

        for(String coeficientes: opciones){
            coeficientesPorVoto.add(Float.parseFloat(coeficientes)/total*100);
        }

        model.put("coeficientesPorOpcion", coeficientesPorVoto);

        return model;
    }

}
