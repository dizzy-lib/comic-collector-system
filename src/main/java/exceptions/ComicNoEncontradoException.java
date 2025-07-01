package exceptions;

public class ComicNoEncontradoException extends RuntimeException {
    public ComicNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}