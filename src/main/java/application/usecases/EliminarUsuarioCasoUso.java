package application.usecases;

import domain.entities.Usuario;
import exceptions.UsuarioNoEliminableException;
import interfaces.domain.IUsuarioService;

/**
 * Caso de uso para eliminar un usuario del sistema.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo elimina usuarios
 * - Delega la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 */
public class EliminarUsuarioCasoUso {
    
    private final IUsuarioService usuarioService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param usuarioService Servicio de dominio para gestión de usuarios
     */
    public EliminarUsuarioCasoUso(IUsuarioService usuarioService) {
        if (usuarioService == null) {
            throw new IllegalArgumentException("El servicio de usuario no puede ser nulo");
        }
        this.usuarioService = usuarioService;
    }
    
    /**
     * Ejecuta el caso de uso para eliminar un usuario.
     * 
     * @param usuario El usuario a eliminar
     * @throws IllegalArgumentException si el usuario es nulo
     * @throws UsuarioNoEliminableException si el usuario no puede ser eliminado
     */
    public void ejecutar(Usuario usuario) {
        usuarioService.eliminarUsuario(usuario);
    }
}