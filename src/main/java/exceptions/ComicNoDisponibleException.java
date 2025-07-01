package exceptions;

public class ComicNoDisponibleException extends RuntimeException {
    public ComicNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}