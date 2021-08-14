package co.edu.unicartagena.actividades.domain.services;

import co.edu.unicartagena.actividades.domain.entities.*;
import co.edu.unicartagena.actividades.domain.repositories.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
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
    AsistenteRepository asistenteRepository;

    public AsambleaService(PersonaRepository personaRepository,
                          PropiedadHorizontalRepository phRepository,
                           OpcionRepository opcionRepository,
                           MocionRepository mocionRepository,
                           ResultadoRepository resultadoRepository,
                           VotoRepository votoRepository,
                           AsistenteRepository asistenteRepository){
        this.personaRepository = personaRepository;
        this.phRepository = phRepository;
        this.opcionRepository = opcionRepository;
        this.mocionRepository = mocionRepository;
        this.resultadoRepository = resultadoRepository;
        this.votoRepository = votoRepository;
        this.asistenteRepository = asistenteRepository;
    }

    public Integer terminarAsamblea(String idPropiedad){

        Integer idSecretario = phRepository.findIdSecretario(Integer.parseInt(idPropiedad)).get();
        Integer idAsamblea = phRepository.findIdAsamblea(idSecretario).get();
        Optional<LocalDateTime> inicioAsamblea = phRepository.findInicioAsambleaById(idAsamblea);
        Optional<LocalDateTime> finAsamblea = phRepository.findFinAsambleaById(idAsamblea);

        if(!inicioAsamblea.get().equals(finAsamblea.get())){
            System.out.println(inicioAsamblea.get());
            System.out.println(finAsamblea.get());
            return 0;
        }

        Optional<List<Integer>> idsAsistentes = personaRepository.findAllAsistentesByIdAsamblea(idAsamblea);

        for (Integer idPersona : idsAsistentes.get()) {
             LocalDateTime horaSalida = LocalDateTime.now();
             personaRepository.registrarAbandono(idPersona, idAsamblea, horaSalida);
        }
        LocalDateTime fechaFin = LocalDateTime.now();
        phRepository.finalizarAsamblea(idAsamblea, fechaFin);

        return 1;// Asamblea terminada.
    }

    public Integer asambleaActiva(Integer idPropiedad){
        Optional<Integer> idSecretario = phRepository.findIdSecretario(idPropiedad);
        Optional<Integer> idAsamblea;
        if(idSecretario.isPresent())
            idAsamblea = phRepository.findIdAsamblea(idSecretario.get());
        else return 0;

        Optional<LocalDateTime> inicioAsamblea;
        Optional<LocalDateTime> finAsamblea;
        if(idAsamblea.isPresent()){
            inicioAsamblea = phRepository.findInicioAsambleaById(idAsamblea.get());
            finAsamblea = phRepository.findFinAsambleaById(idAsamblea.get());
        }else return 0;

        System.out.println(inicioAsamblea.get() + " : " + finAsamblea.get());

        if(inicioAsamblea.get().equals(finAsamblea.get())) return 1; //Hay asamblea activa
        else return 0; //Si Tienen diferentes fechas, ha terminado la asamblea
    }

    public List<String> getQuorum(String idPropiedad){
        List<String> toReturn = new ArrayList<>();
        Float coeficientesAsistentes = 0f;
        Integer idSecretario = phRepository.findIdSecretario(Integer.parseInt(idPropiedad)).get();
        Integer idAsamblea = phRepository.findIdAsamblea(idSecretario).get();
        Optional<List<Integer>> idsAsistentes = personaRepository.findAllAsistentesByIdAsamblea(idAsamblea);
        Optional<List<Asistente>> listaAsistentes = asistenteRepository.findByIdAsamblea(idAsamblea);

        if(listaAsistentes.isPresent()){
            toReturn.add(String.valueOf(listaAsistentes.get().size()));
            for(Asistente asistente: listaAsistentes.get()){
                if(asistente.getIdPersona() > 0){// Si es propietario
                    coeficientesAsistentes += personaRepository.findCoeficienteByIdBienPrivado(asistente.getIdPersona());
                }else{// Si es delegado
                    coeficientesAsistentes += personaRepository.findCoeficienteByIdBienPrivado(asistente.getIdRepresentado());
                }
            }
        }else{
            toReturn.add(String.valueOf(0));
        }

        Float totalCoeficientes = phRepository.findTotalCoeficiente(Integer.parseInt(idPropiedad));
        Integer totalPropietarios = phRepository.findTotalPropietarios(Integer.parseInt(idPropiedad));

        // If the totalCoeficientes!=totalPropietarios
        // it means that values for coeficientes are not by default and therefore is mandatory to evaluate
        // If totalCoeficiente is 100%. If it's not, then an assembly couldn't be initiated.

        if(totalCoeficientes.intValue() == totalPropietarios || totalCoeficientes.intValue() == 100){
            //Propietarios tienen coef. como 1 (default) o su suma es 100%
            toReturn.add(String.valueOf(totalPropietarios-Integer.parseInt(toReturn.get(0))));//Ausentes
            toReturn.add(String.valueOf(coeficientesAsistentes/totalCoeficientes*100));//Quorum

        }else{
            //NO se puede iniciar la asamblea debido a que los coeficientes no están registrados correctamente
            toReturn.add(String.valueOf(totalPropietarios));//Ausentes todos
            toReturn.add(String.valueOf(-1));//Quorum negativo
        }

        System.out.println(toReturn);
        return toReturn;
    }

    public String registerProposition(Integer idPropiedad, String tipo, String titulo, List<String> proposiciones){

        Integer idSecretario = phRepository.findIdSecretario(idPropiedad).get();
        Integer idAsamblea = phRepository.findIdAsamblea(idSecretario).get();

        if(phRepository.mocionActiva(idAsamblea).isPresent())
            return "2";//Hay una moción activa actualmente

        Float totalCoeficientes = phRepository.findTotalCoeficiente(idPropiedad);
        Integer totalPropietarios = phRepository.findTotalPropietarios(idPropiedad);

        if(totalCoeficientes.intValue() != totalPropietarios && totalCoeficientes.intValue() != 100){
            return "3";//Los coeficientes de copropiedad no están debidamente registrados
        }

        // Agregar estado en el front, como cuando se quiere registrar secretario/revisor en administrador

        phRepository.saveMocion(tipo, titulo, idAsamblea, true);
        Integer idMocion = phRepository.mocionActiva(idAsamblea).get();

        for(String prop: proposiciones)
            phRepository.saveOpciones(idMocion, prop);

        return "1";//Alright, Proposition saved.
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
            //Lista de los ids de personas que votaron por la opción contenida en key
            idsPersonas = votosPorOpcion.get(key);
            Float coeficientesPersonas = 0f;

            for(Integer idPersona: idsPersonas){
                //Simplemente suma 1(unos) o cualquier otro valor (hasta 50)
                //Se asume que los coeficientes han sido seteados correctamente en este punto,
                //dado que hay validaciones suficientes para determinarlo y corregirlo con anterioridad
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

    public Integer registerVote(Integer idPersona, String eleccion, String ipDirection){
    //Entero para retornar distintos estados (propietario moroso, error con la bd, voto exitoso)

        Integer idPropiedad = personaRepository.findIdPropiedadByIdPersona(idPersona);
        Optional<Integer> idSecretario = phRepository.findIdSecretario(idPropiedad);
        Optional<Integer> idAsamblea = phRepository.findIdAsamblea(idSecretario.get());

        //Verificar que el votante esté en asamblea
        Optional<List<Asistente>> listaAsistentes = asistenteRepository.findByIdAsamblea(idAsamblea.get());
        Boolean propietarioPresente = false;

        for(Asistente asistente: listaAsistentes.get()){
            if(asistente.getIdPersona() == idPersona || asistente.getIdRepresentado() == idPersona){
                propietarioPresente = true;
                break;
            }
        }

        if(!propietarioPresente)
            return 2;//El propietario no está presente en la asamblea

        // Voy a asumir que las restricciones ya están registradas.
        Optional<String> restricciones = phRepository.findRestrictionByIdPH(idPropiedad);

        if (restricciones.isPresent()) {
            //Verificar el tipo de la moción activa
            Optional<Integer> idMocion = phRepository.mocionActiva(idAsamblea.get());
            Optional<Integer> votado = phRepository.votoPropietario(idPersona, idMocion.get());
            if(votado.isPresent())
                return 3;//El propietario ya ha votado

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
                    personaRepository.doVote(idMocion.get(), op.getIdOpcion(), idPersona, ipDirection);
                    break;
                }
            return 1; //Voto exitoso

        } else {
            //Guardar voto
            Optional<Integer> idMocion = phRepository.mocionActiva(idAsamblea.get());
            Optional<Integer> votado = phRepository.votoPropietario(idPersona, idMocion.get());
            if(votado.isPresent())
                return 3;
            List<Opcion> opciones = opcionRepository.findByIdMocion(idMocion.get());
            for (Opcion op : opciones)
                if (op.getDescripcion().equals(eleccion)) {
                    personaRepository.doVote(idMocion.get(), op.getIdOpcion(), idPersona, ipDirection);
                    break;
                }
            return 1; //Voto exitoso
        }

    }

    public Map<Object, Object> resultadosSecretario(Integer idPropiedad){
        Integer idSecretario = phRepository.findIdSecretario(idPropiedad).get();
        Integer idAsamblea = phRepository.findIdAsamblea2(idSecretario).get();
        return getAllResults(idAsamblea, 0);
    }

    public List<Map<Object, Object>> resultadosRevisor(Integer idPropiedad){
        Integer idSecretario = phRepository.findIdSecretario(idPropiedad).get();
        Integer idAsamblea = phRepository.findIdAsamblea2(idSecretario).get();
        Optional<List<Mocion>> currentMociones = mocionRepository.findByIdAsamblea(idAsamblea);
        //System.out.println(currentMociones);
        List<Map<Object, Object>> allResults = new LinkedList<>();
        for(Mocion mocion: currentMociones.get()){
            Map<Object, Object> model;
            model = getAllResults(idAsamblea, mocion.getIdMocion());
            model.put("titulo", mocion.getDescripcionMocion());
            allResults.add(model);
            //System.out.println(allResults);
        }
        return allResults;
    }

    public Map<Object, Object> resultadosPropietario(Integer idPersona){
        Optional<Integer> idAsamblea = phRepository.findIdAsambleaByIdPersona(idPersona);
        if(!idAsamblea.isPresent()){
            Map<Object, Object> model = new HashMap<>();
            model.put("ausente", true);
            model.put("esPlancha", false);
            model.put("message", "El propietario no se encuentra participando en la asamblea");
            return model;
        }
        return getAllResults(idAsamblea.get(), 0);
    }

    public Map<Object, Object> getAllResults(Integer idAsamblea, Integer ultimoId) {
        String titulo = "";
        if (ultimoId == 0){
            Optional<List<Mocion>> currentMociones = mocionRepository.findByIdAsamblea(idAsamblea);
            //Looks a bit absurd but isPresent value was retrieving weird results, so to have more security
            if(currentMociones.isPresent()) {
                if (currentMociones.get().size() == 0) {
                    Map<Object, Object> model = new HashMap<>();
                    model.put("hayMocion", false);
                    return model;
                }
            }else{
                Map<Object, Object> model = new HashMap<>();
                model.put("hayMocion", false);
                return model;
            }

            for (Mocion mocion : currentMociones.get()) {
                if (mocion.getIdMocion() > ultimoId) {
                    ultimoId = mocion.getIdMocion();
                    titulo = mocion.getDescripcionMocion();
                }
            }
        }
        Map<Object, Object> model = new HashMap<>();
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
        Float total = 0f;
        for (String coeficientes: opciones){
            total += Float.parseFloat(coeficientes);
        }

        for(String coeficientes: opciones){
            coeficientesPorVoto.add(Float.parseFloat(coeficientes)/total*100);
        }

        model.put("coeficientesPorOpcion", coeficientesPorVoto);
        return model;
    }

    public Integer registrarPoder(Integer idPropiedad, String rol, String tipo, String numero,
                   String nombres, String apellidos, String tipoPropietario, String numeroPropietario) {

        try{
            Optional<Persona> existePropietario = personaRepository.
                    findByTipoDocumentoAndNumeroDocumento(tipoPropietario, numeroPropietario);

            if (!existePropietario.isPresent()) {
                return 0;
            }

            Float totalCoeficientes = phRepository.findTotalCoeficiente(idPropiedad);
            Integer totalPropietarios = phRepository.findTotalPropietarios(idPropiedad);

            if(totalCoeficientes.intValue() != totalPropietarios && totalCoeficientes.intValue() != 100){
                return 4;//Los coeficientes de copropiedad no están debidamente registrados
            }

            Integer idSecretario = phRepository.findIdSecretario(idPropiedad).get();
            Integer idAsamblea = phRepository.findIdAsamblea(idSecretario).get();

            Optional<LocalDateTime> horaLlegada = phRepository.propietarioHoraLlegada(idAsamblea, existePropietario.get().getIdPersona());
            Optional<LocalDateTime> horaSalida = phRepository.propietarioHoraSalida(idAsamblea, existePropietario.get().getIdPersona());

            if(horaLlegada.isPresent() && horaSalida.isPresent())
                return 3; //Propietario ya está registrado como asistente

            Optional<Persona> existeDelegado = personaRepository.
                    findByTipoDocumentoAndNumeroDocumento(tipo, numero);

            // Si es propietario
            if (existeDelegado.isPresent()) {
                LocalDateTime llegada = LocalDateTime.now();

                personaRepository.saveDelegadoAsistente(idAsamblea, existeDelegado.get().getIdPersona(),
                        existePropietario.get().getIdPersona(), rol, llegada, llegada);
                return 1;
            } else { //Si no es propietario
                int id = ThreadLocalRandom.current().nextInt(-2147483640, 0);
                personaRepository.saveDataDelegado(id, existePropietario.get().getIdBienPrivado(),tipo, numero, nombres, apellidos, rol, false);

                Optional<Persona> delegado = personaRepository.
                        findByTipoDocumentoAndNumeroDocumento(tipo, numero);
                LocalDateTime llegada = LocalDateTime.now();

                personaRepository.saveDelegadoAsistente(idAsamblea, delegado.get().getIdPersona(),
                        existePropietario.get().getIdPersona(), rol, llegada, llegada);
                return 1;
            }
        }catch(Exception e){
            return 2;
        }
    }
}
