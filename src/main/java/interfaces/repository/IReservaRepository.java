package interfaces.repository;

import domain.entities.Reserva;
import domain.entities.Usuario;
import domain.entities.Comic;
import domain.enums.EstadoReserva;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface IReservaRepository {
    void guardar(Reserva reserva);
    Optional<Reserva> buscarPorId(String id);
    List<Reserva> buscarTodas();
    List<Reserva> buscarPorUsuario(Usuario usuario);
    List<Reserva> buscarPorComic(Comic comic);
    List<Reserva> buscarPorEstado(EstadoReserva estado);
    List<Reserva> buscarReservasExpiradas();
    List<Reserva> buscarPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    void actualizar(Reserva reserva);
    void eliminar(String id);
}