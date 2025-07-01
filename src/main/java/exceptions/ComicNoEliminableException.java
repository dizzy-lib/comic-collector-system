package exceptions;

public class ComicNoEliminableException extends RuntimeException {
    public ComicNoEliminableException(String mensaje) {
        super(mensaje);
    }
}