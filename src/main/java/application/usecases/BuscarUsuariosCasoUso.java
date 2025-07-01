package application.usecases;

import domain.entities.Usuario;
import interfaces.domain.IUsuarioService;
import java.util.List;

/**
 * Caso de uso para buscar usuarios por criterio.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo busca usuarios por criterio
 * - Delega la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 */
public class BuscarUsuariosCasoUso {
    
    private final IUsuarioService usuarioService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param usuarioService Servicio de dominio para gestión de usuarios
     */
    public BuscarUsuariosCasoUso(IUsuarioService usuarioService) {
        if (usuarioService == null) {
            throw new IllegalArgumentException("El servicio de usuario no puede ser nulo");
        }
        this.usuarioService = usuarioService;
    }
    
    /**
     * Ejecuta el caso de uso para buscar usuarios por criterio.
     * 
     * @param criterio El criterio de búsqueda
     * @return Lista de usuarios que coinciden con el criterio
     * @throws IllegalArgumentException si el criterio es nulo o vacío
     */
    public List<Usuario> ejecutar(String criterio) {
        return usuarioService.buscarUsuarios(criterio);
    }
}