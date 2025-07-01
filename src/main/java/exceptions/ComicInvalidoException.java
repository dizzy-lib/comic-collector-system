package exceptions;

public class ComicInvalidoException extends RuntimeException {
    public ComicInvalidoException(String mensaje) {
        super(mensaje);
    }
}