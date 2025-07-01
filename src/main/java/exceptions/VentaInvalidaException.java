package exceptions;

public class VentaInvalidaException extends RuntimeException {
    public VentaInvalidaException(String mensaje) {
        super(mensaje);
    }
}