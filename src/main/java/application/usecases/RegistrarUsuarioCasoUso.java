package application.usecases;

import domain.entities.Usuario;
import exceptions.EmailYaExisteException;
import interfaces.domain.IUsuarioService;

/**
 * Caso de uso para registrar un nuevo usuario en el sistema.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Orquesta la operación de registro delegando la lógica de negocio al servicio de dominio
 * - Actúa como punto de entrada desde la capa de presentación
 * - Mantiene la separación entre casos de uso y lógica de dominio
 */
public class RegistrarUsuarioCasoUso {
    
    private final IUsuarioService usuarioService;
    
    /**
     * Constructor que recibe las dependencias necesarias.
     * 
     * @param usuarioService Servicio de dominio para gestión de usuarios
     */
    public RegistrarUsuarioCasoUso(IUsuarioService usuarioService) {
        if (usuarioService == null) {
            throw new IllegalArgumentException("El servicio de usuario no puede ser nulo");
        }
        this.usuarioService = usuarioService;
    }
    
    /**
     * Ejecuta el caso de uso de registro de usuario.
     * 
     * @param nombre El nombre del usuario
     * @param apellido El apellido del usuario
     * @param email El email del usuario
     * @return El usuario registrado
     * @throws IllegalArgumentException si los datos de entrada son inválidos
     * @throws EmailYaExisteException si el email ya está registrado
     */
    public Usuario ejecutar(String nombre, String apellido, String email) {
        // Delegar toda la lógica de negocio al servicio de dominio
        return usuarioService.registrarUsuario(nombre, apellido, email);
    }
}
