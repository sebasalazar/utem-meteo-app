package cl.utem.meteo.exception;

public class ValidationException extends RuntimeException {

    public ValidationException() {
        super("Ocurrió un error de validación");
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
