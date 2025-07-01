package exceptions;

public class VentaNoProcesableException extends RuntimeException {
    public VentaNoProcesableException(String mensaje) {
        super(mensaje);
    }
}