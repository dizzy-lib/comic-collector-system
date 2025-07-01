package exceptions;

public class ComicNoDisponibleParaVentaException extends RuntimeException {
    public ComicNoDisponibleParaVentaException(String mensaje) {
        super(mensaje);
    }
}