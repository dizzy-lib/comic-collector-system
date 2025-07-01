package interfaces.domain;

import domain.entities.Comic;
import domain.entities.Usuario;
import domain.enums.EstadoReserva;

import java.time.LocalDateTime;

public interface IReserva {
    LocalDateTime getFechaReserva();
    LocalDateTime getFechaExpiracionReserva();
    String getId();
    Comic getComic();
    Usuario getUsuario();
    EstadoReserva getEstadoReserva();
    void activarReserva();
    void setReservaInactiva();
}
