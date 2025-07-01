package exceptions;

public class UsuarioNoEliminableException extends RuntimeException {
    public UsuarioNoEliminableException(String mensaje) {
        super(mensaje);
    }
}