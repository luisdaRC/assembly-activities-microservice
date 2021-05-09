package co.edu.unicartagena.actividades.domain.exceptions;

public class BusinessException extends RuntimeException {

    private final Integer code;
    private final String message;

    public BusinessException(String message) {
        this.message = message;
        this.code = 404;
    }

    public BusinessException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }
}
