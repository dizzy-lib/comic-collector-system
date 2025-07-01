package infrastructure.repository;

import domain.entities.Usuario;
import exceptions.UsuarioNoEncontradoException;
import interfaces.repository.IUsuarioRepository;
import java.util.*;

public class UsuarioRepository implements IUsuarioRepository {
    private final Map<Integer, Usuario> usuarios = new HashMap<>();
    private int idGenerator = 1;

    @Override
    public void guardar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        
        int id = idGenerator++;
        usuario.setId(id);
        usuarios.put(id, usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return usuarios.values().stream()
                .filter(usuario -> email.equalsIgnoreCase(usuario.getEmail()))
                .findFirst();
    }

    @Override
    public List<Usuario> buscarTodos() {
        return new ArrayList<>(usuarios.values());
    }

    @Override
    public void actualizar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        
        int id = usuario.getId();
        if (!usuarios.containsKey(id)) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + id);
        }
        
        usuarios.put(id, usuario);
    }

    @Override
    public void eliminar(int id) {
        if (!usuarios.containsKey(id)) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + id);
        }
        usuarios.remove(id);
    }
}