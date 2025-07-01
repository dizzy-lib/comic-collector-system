package interfaces.domain;

import domain.entities.Comic;
import domain.entities.Usuario;
import domain.valueobjects.Divisa;

import java.time.LocalDateTime;

public interface IVenta {
    String getId();
    Usuario getUsuario();
    Comic getComic();
    LocalDateTime getFechaVenta();
    Divisa getPrecioFinal();
}
