package co.edu.unicartagena.actividades.domain.services;

import co.edu.unicartagena.actividades.domain.entities.*;
import co.edu.unicartagena.actividades.domain.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AsambleaServiceTest {

    @Mock
    private PersonaRepository personaRepository;
    @Mock
    private PropiedadHorizontalRepository phRepository;
    @Mock
    private OpcionRepository opcionRepository;
    @Mock
    private MocionRepository mocionRepository;
    @Mock
    private ResultadoRepository resultadoRepository;
    @Mock
    private AsistenteRepository asistenteRepository;

    @InjectMocks
    private AsambleaService asambleaService;

    List<String> quorumData = new ArrayList<>();
    List<String> proposiciones = new ArrayList<>();
    Map<Object, Object> resultadosResponse = new HashMap<>();
    Mocion mocion;
    Asistente asistente;
    Resultado resultado;
    Opcion opcion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        asistente = new Asistente();
        asistente.setIdAsistente(1);
        asistente.setIdAsamblea(1);
        asistente.setIdPersona(1);
        asistente.setRol("PROPIETARIO");
        asistente.setHoraLlegada(LocalDateTime.now());
        asistente.setHoraSalida(LocalDateTime.now());
        asistente.setIdRepresentado(0);

        mocion = new Mocion();
        mocion.setIdMocion(1);
        mocion.setIdAsamblea(1);
        mocion.setDescripcionMocion("Aprobar estados de cuenta");
        mocion.setEstado(false);
        mocion.setTipo("ESTADOS_CUENTA");

        resultado = new Resultado();
        resultado.setIdResultado(1);
        resultado.setIdMocion(1);
        resultado.setCoeficientesPorOpcion("80,20");
        resultado.setPersonasPorOpcion("2,1");
        resultado.setDescripcionesMociones("SI#CustmSpace#NO");

        List<Float> coeficientes = new LinkedList<>();
        coeficientes.add(80f);
        coeficientes.add(20f);
        resultadosResponse.put("coeficientesPorOpcion", coeficientes);
        resultadosResponse.put("titulo", "Aprobar estados de cuenta");
        List<String> descripciones = new LinkedList<>();
        descripciones.add("SI");
        descripciones.add("NO");
        resultadosResponse.put("descripciones", descripciones);
        resultadosResponse.put("esPlancha", false);
        List<Integer> votosPorOpcion = new LinkedList<>();
        votosPorOpcion.add(2);
        votosPorOpcion.add(1);
        resultadosResponse.put("votosPorOpcion", votosPorOpcion);

        opcion = new Opcion();
        opcion.setIdOpcion(1);
        opcion.setDescripcion("SI");
        opcion.setIdMocion(1);

        quorumData.add("1");
        quorumData.add("0");
        quorumData.add("100.0");

        proposiciones.add("SI");
        proposiciones.add("NO");

    }

    @Test
    void getQuorum() {
        when(phRepository.findIdSecretario(1)).thenReturn(Optional.of(1));
        when(phRepository.findIdAsamblea(1)).thenReturn(Optional.of(1));
        when(asistenteRepository.findByIdAsamblea(1)).thenReturn(Optional.of(Arrays.asList(asistente)));
        when(personaRepository.findCoeficienteByIdPersona(1)).thenReturn(Float.valueOf("100"));
        when(phRepository.findTotalCoeficiente(1)).thenReturn(Float.valueOf("100"));
        when(phRepository.findTotalPropietarios(1)).thenReturn(1);
        assertEquals(quorumData, asambleaService.getQuorum("1"));
    }

    @Test
    void registerProposition() {
        when(phRepository.findIdSecretario(1)).thenReturn(Optional.of(1));
        when(phRepository.findIdAsamblea(1)).thenReturn(Optional.of(1));
        when(phRepository.findTotalCoeficiente(1)).thenReturn(Float.valueOf("100"));
        when(phRepository.findTotalPropietarios(1)).thenReturn(1);
        when(phRepository.mocionActiva(1)).thenReturn(Optional.of(1));
        assertEquals("2",
                asambleaService.registerProposition(1, "ESTADOS_FINANCIEROS", "Aumentar $80.000", proposiciones));
    }

    @Test
    void registerVote() {
        //Taking direct flow for vote without restrictions
        when(personaRepository.findIdPropiedadByIdPersona(1)).thenReturn(1);
        when(phRepository.findIdSecretario(1)).thenReturn(Optional.of(1));
        when(phRepository.findIdAsamblea(1)).thenReturn(Optional.of(1));
        when(asistenteRepository.findByIdAsamblea(1)).thenReturn(Optional.of(Arrays.asList(asistente)));
        //Ignoring restrictions
        when(phRepository.mocionActiva(1)).thenReturn(Optional.of(1));
        //Ignored votado
        when(opcionRepository.findByIdMocion(1)).thenReturn(Arrays.asList(opcion));
        assertEquals(1, asambleaService.registerVote(1, "SI", "0.0.0.0"));

    }

    @Test
    void resultadosSecretario() {
        when(phRepository.findIdSecretario(1)).thenReturn(Optional.of(1));
        when(phRepository.findIdAsamblea2(1)).thenReturn(Optional.of(1));
        when(mocionRepository.findByIdAsamblea(1)).thenReturn(Optional.of(Arrays.asList(mocion)));
        when(resultadoRepository.findByIdMocion(1)).thenReturn(Optional.of(resultado));
        assertEquals(asambleaService.resultadosSecretario(1), resultadosResponse);
    }
}
