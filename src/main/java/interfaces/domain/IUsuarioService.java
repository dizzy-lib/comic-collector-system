package interfaces.domain;

import domain.entities.Usuario;
import java.util.List;

/**
 * Interfaz del servicio de dominio para la gestión de usuarios.
 * 
 * Define las operaciones de negocio complejas relacionadas con usuarios
 * que involucran validaciones y reglas que no pertenecen a la entidad Usuario.
 */
public interface IUsuarioService {
    
    /**
     * Registra un nuevo usuario aplicando todas las reglas de negocio.
     * 
     * @param nombre El nombre del usuario
     * @param apellido El apellido del usuario  
     * @param email El email del usuario
     * @return El usuario registrado
     * @throws IllegalStateException si no se puede registrar el usuario
     */
    Usuario registrarUsuario(String nombre, String apellido, String email);
    
    /**
     * Verifica si un email ya está registrado en el sistema.
     * 
     * @param email El email a verificar
     * @return true si el email ya existe, false en caso contrario
     */
    boolean emailYaExiste(String email);
    
    /**
     * Actualiza la información de un usuario aplicando validaciones.
     * 
     * @param usuario El usuario a actualizar
     * @return El usuario actualizado
     * @throws IllegalStateException si no se puede actualizar
     */
    Usuario actualizarUsuario(Usuario usuario);
    
    /**
     * Verifica si un usuario puede ser eliminado del sistema.
     * No se puede eliminar si tiene reservas activas o ventas recientes.
     * 
     * @param usuario El usuario a verificar
     * @return true si puede ser eliminado, false en caso contrario
     */
    boolean puedeEliminarUsuario(Usuario usuario);
    
    /**
     * Elimina un usuario del sistema aplicando todas las reglas de negocio.
     * 
     * @param usuario El usuario a eliminar
     * @throws IllegalStateException si no se puede eliminar el usuario
     */
    void eliminarUsuario(Usuario usuario);
    
    /**
     * Busca usuarios por criterios específicos.
     * 
     * @param criterio El criterio de búsqueda (nombre, apellido, email)
     * @return Lista de usuarios que coinciden con el criterio
     */
    List<Usuario> buscarUsuarios(String criterio);
}