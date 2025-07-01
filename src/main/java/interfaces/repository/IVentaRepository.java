package interfaces.repository;

import domain.entities.Venta;
import domain.entities.Usuario;
import domain.entities.Comic;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface IVentaRepository {
    void guardar(Venta venta);
    Optional<Venta> buscarPorId(String id);
    List<Venta> buscarTodas();
    List<Venta> buscarPorUsuario(Usuario usuario);
    List<Venta> buscarPorComic(Comic comic);
    List<Venta> buscarPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    void actualizar(Venta venta);
    void eliminar(String id);
}