package co.edu.unicartagena.actividades.domain.services;

import co.edu.unicartagena.actividades.domain.entities.*;
import co.edu.unicartagena.actividades.domain.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

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
    private VotoRepository votoRepository;
    @Mock
    private AsistenteRepository asistenteRepository;

    @InjectMocks
    private AsambleaService asambleaService;

    Asistente asistente;
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

        opcion = new Opcion();
        opcion.setIdOpcion(1);
        opcion.setDescripcion("SI");
        opcion.setIdMocion(1);
    }
/*
    @Test
    void getQuorum() {
    }

    @Test
    void registerProposition() {
    }
*/
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
/*
    @Test
    void resultadosSecretario() {
    }*/
}