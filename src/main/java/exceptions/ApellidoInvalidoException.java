package exceptions;

public class ApellidoInvalidoException extends RuntimeException {
    public ApellidoInvalidoException(String mensaje) {
        super(mensaje);
    }
}