package cl.utem.meteo.exception;

public class AuthException extends RuntimeException {

    public AuthException() {
        super("Credenciales incorrectas");
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
