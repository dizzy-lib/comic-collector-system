package exceptions;

public class EmailInvalidoException extends RuntimeException {
    public EmailInvalidoException(String mensaje) {
        super(mensaje);
    }
}