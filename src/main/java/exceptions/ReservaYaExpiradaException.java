package exceptions;

public class ReservaYaExpiradaException extends RuntimeException {
    public ReservaYaExpiradaException(String message) {
        super(message);
    }
}
