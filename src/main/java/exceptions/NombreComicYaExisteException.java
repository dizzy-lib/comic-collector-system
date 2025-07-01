package exceptions;

public class NombreComicYaExisteException extends RuntimeException {
    public NombreComicYaExisteException(String mensaje) {
        super(mensaje);
    }
}