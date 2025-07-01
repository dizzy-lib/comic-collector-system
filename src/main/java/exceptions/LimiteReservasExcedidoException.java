package exceptions;

public class LimiteReservasExcedidoException extends RuntimeException {
    public LimiteReservasExcedidoException(String mensaje) {
        super(mensaje);
    }
}