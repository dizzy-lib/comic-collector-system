package exceptions;

public class VentaNoEncontradaException extends RuntimeException {
    public VentaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}