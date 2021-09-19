package co.edu.unicartagena.actividades;

import co.edu.unicartagena.actividades.domain.entities.Persona;
import co.edu.unicartagena.actividades.domain.repositories.*;
import co.edu.unicartagena.actividades.domain.services.AsambleaService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

@SpringBootTest
public class ActividadesApplicationTests {
/*
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

    private AsambleaService asambleaService;
*/
    @BeforeEach
    public void setup() {
/*        MockitoAnnotations.initMocks(this);
        asambleaService = new AsambleaService(personaRepository,
                phRepository, opcionRepository, mocionRepository, resultadoRepository, votoRepository, asistenteRepository);

        // Simulación del caso de un id de Persona para el que existe restricción de votación por concepto de moroso
        // y que de igual manera intenta registrar un voto para una determinada moción.
        when(asambleaService.registerVote(1, "SI", "0:0:0:0")).thenReturn(0);

        // Simulación del caso de un id de Persona que no tiene restricciones
        // y que puede realizar un voto para determinada moción de manera exitosa.
        when(asambleaService.registerVote(2, "$20.000", "0:0:0:0")).thenReturn(1);

        // Simulación del caso de un id de Persona que no ha sido anteriormente registrado en la asamblea
        // pero que de alguna manera intentó realizar su voto
        when(asambleaService.registerVote(3, "Cada 6 meses", "0:0:0:0")).thenReturn(2);

        // Simulación del caso de un id de Persona que ya ejerció su derecho al voto satisfactoriamente
        // pero que lo intenta realizar de nuevo.
        when(asambleaService.registerVote(4, "Primera semana de cada mes", "0:0:0:0")).thenReturn(3);
*/
    }
    // Se ingresan parámetros para cada uno de los escenarios posibles
    // y se comenta lo que significa cada uno.
    //0: Hay una restricción que le impide votar
    //1: Voto exitoso
    //2: El propietario no está presente en la asamblea
    //3: El propietario ya ha votado
/*    @Test
    void registerVoteWithStatus0Test() {
        //https://www.youtube.com/watch?v=jVQT2SzYb7A
        //Es necesario omckear los objetos para poder ejecutar los repositorios correctaente
        Integer voteResult = asambleaService.registerVote(1, "SI", "0:0:0:0");
        Assertions.assertThat(voteResult).isEqualTo(0);
    }

    @Test
    void registerVoteWithStatus1Test() {
        Integer voteResult = asambleaService.registerVote(2, "$20.000", "0:0:0:0");
        Assertions.assertThat(voteResult).isEqualTo(1);
    }

    @Test
    void registerVoteWithStatus2Test() {
        Integer voteResult = asambleaService.registerVote(3, "Cada 6 meses", "0:0:0:0");
        Assertions.assertThat(voteResult).isEqualTo(2);
    }

    @Test
    void registerVoteWithStatus3Test() {
        Integer voteResult = asambleaService.registerVote(4, "Primera semana de cada mes", "0:0:0:0");
        Assertions.assertThat(voteResult).isEqualTo(3);
    }

    @Test
    void getQuorumTest() {

    }

    @Test
    void registerPropositionTest() {
        List<String> proposiciones = new LinkedList();

    }

    @Test
    void resultadosSecretarioTest() {

    }*/

}
