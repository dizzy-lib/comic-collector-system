package interfaces.repository;

import domain.entities.Usuario;
import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository {
    void guardar(Usuario usuario);
    Optional<Usuario> buscarPorId(int id);
    Optional<Usuario> buscarPorEmail(String email);
    List<Usuario> buscarTodos();
    void actualizar(Usuario usuario);
    void eliminar(int id);
}