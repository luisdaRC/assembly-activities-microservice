package co.edu.unicartagena.actividades.application.commands;

public interface Command<R, M> {
    R ejecutar(M command);
}
