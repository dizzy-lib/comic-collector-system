package application.usecases;

import domain.entities.Usuario;
import exceptions.UsuarioNoEncontradoException;
import exceptions.EmailYaExisteException;
import interfaces.domain.IUsuarioService;

/**
 * Caso de uso para actualizar la información de un usuario.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo actualiza información del usuario
 * - Delega la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 */
public class ActualizarUsuarioCasoUso {
    
    private final IUsuarioService usuarioService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param usuarioService Servicio de dominio para gestión de usuarios
     */
    public ActualizarUsuarioCasoUso(IUsuarioService usuarioService) {
        if (usuarioService == null) {
            throw new IllegalArgumentException("El servicio de usuario no puede ser nulo");
        }
        this.usuarioService = usuarioService;
    }
    
    /**
     * Ejecuta el caso de uso para actualizar un usuario.
     * 
     * @param usuario El usuario a actualizar
     * @return El usuario actualizado
     * @throws IllegalArgumentException si el usuario es nulo
     * @throws UsuarioNoEncontradoException si el usuario no existe
     * @throws EmailYaExisteException si el email ya está en uso
     */
    public Usuario ejecutar(Usuario usuario) {
        return usuarioService.actualizarUsuario(usuario);
    }
}