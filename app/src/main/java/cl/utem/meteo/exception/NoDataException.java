package cl.utem.meteo.exception;

public class NoDataException extends RuntimeException {

    public NoDataException() {
        super("No se han encontrado datos");
    }

    public NoDataException(String message) {
        super(message);
    }

    public NoDataException(String message, Throwable cause) {
        super(message, cause);
    }

}
