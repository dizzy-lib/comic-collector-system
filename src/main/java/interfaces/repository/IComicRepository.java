package interfaces.repository;

import domain.entities.Comic;
import java.util.List;
import java.util.Optional;

public interface IComicRepository {
    void guardar(Comic comic);
    Optional<Comic> buscarPorId(String id);
    List<Comic> buscarTodos();
    List<Comic> buscarPorNombre(String nombre);
    void actualizar(Comic comic);
    void eliminar(String id);
}