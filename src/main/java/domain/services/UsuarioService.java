package domain.services;

import domain.entities.Usuario;
import domain.entities.Reserva;
import domain.entities.Venta;
import domain.enums.EstadoReserva;
import exceptions.EmailYaExisteException;
import exceptions.UsuarioNoEliminableException;
import exceptions.UsuarioNoEncontradoException;
import interfaces.domain.IUsuarioService;
import interfaces.repository.IUsuarioRepository;
import interfaces.repository.IReservaRepository;
import interfaces.repository.IVentaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio de dominio para la gestión de usuarios.
 * 
 * PRINCIPIOS DE ARQUITECTURA LIMPIA APLICADOS:
 * 
 * 1. RESPONSABILIDAD ÚNICA: Se encarga únicamente de la lógica de negocio de usuarios
 *    que involucra validaciones complejas y coordinación con otros agregados.
 * 
 * 2. INVERSIÓN DE DEPENDENCIAS: Depende de abstracciones (repositorios) 
 *    no de implementaciones concretas.
 * 
 * 3. REGLAS DE NEGOCIO CENTRALIZADAS: Todas las políticas relacionadas con usuarios
 *    están definidas en este servicio.
 * 
 * 4. COORDINACIÓN DE AGREGADOS: Maneja la interacción entre Usuario, Reserva y Venta
 *    para operaciones complejas como eliminación de usuarios.
 * 
 * 5. VALIDACIONES DE INTEGRIDAD: Asegura que las operaciones mantengan la
 *    consistencia del dominio.
 */
public class UsuarioService implements IUsuarioService {
    
    // Reglas de negocio como constantes
    private static final int DIAS_LIMITE_VENTAS_RECIENTES = 30;
    
    private final IUsuarioRepository usuarioRepository;
    private final IReservaRepository reservaRepository;
    private final IVentaRepository ventaRepository;
    
    /**
     * Constructor que recibe las dependencias necesarias (Dependency Injection).
     * 
     * @param usuarioRepository Repositorio de usuarios
     * @param reservaRepository Repositorio de reservas
     * @param ventaRepository Repositorio de ventas
     */
    public UsuarioService(IUsuarioRepository usuarioRepository, 
                         IReservaRepository reservaRepository,
                         IVentaRepository ventaRepository) {
        if (usuarioRepository == null) {
            throw new IllegalArgumentException("El repositorio de usuarios no puede ser nulo");
        }
        if (reservaRepository == null) {
            throw new IllegalArgumentException("El repositorio de reservas no puede ser nulo");
        }
        if (ventaRepository == null) {
            throw new IllegalArgumentException("El repositorio de ventas no puede ser nulo");
        }
        
        this.usuarioRepository = usuarioRepository;
        this.reservaRepository = reservaRepository;
        this.ventaRepository = ventaRepository;
    }
    
    @Override
    public Usuario registrarUsuario(String nombre, String apellido, String email) {
        // Validaciones de entrada
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede ser nulo o vacío");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }
        
        // Verificar que el email no exista
        if (emailYaExiste(email)) {
            throw new EmailYaExisteException(
                String.format("Ya existe un usuario registrado con el email: %s", email)
            );
        }
        
        // Crear y guardar el usuario
        Usuario nuevoUsuario = new Usuario(nombre, apellido, email);
        usuarioRepository.guardar(nuevoUsuario);
        
        return nuevoUsuario;
    }
    
    @Override
    public boolean emailYaExiste(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        return usuarioRepository.buscarPorEmail(email.trim()).isPresent();
    }
    
    @Override
    public Usuario actualizarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        
        // Verificar que el usuario existe
        Optional<Usuario> usuarioExistente = usuarioRepository.buscarPorId(usuario.getId());
        if (usuarioExistente.isEmpty()) {
            throw new UsuarioNoEncontradoException(
                String.format("Usuario no encontrado con ID: %d", usuario.getId())
            );
        }
        
        // Verificar que el email no esté siendo usado por otro usuario
        Optional<Usuario> usuarioConMismoEmail = usuarioRepository.buscarPorEmail(usuario.getEmail());
        if (usuarioConMismoEmail.isPresent() && 
            usuarioConMismoEmail.get().getId() != usuario.getId()) {
            throw new EmailYaExisteException(
                String.format("El email %s ya está siendo usado por otro usuario", usuario.getEmail())
            );
        }
        
        // Actualizar el usuario
        usuarioRepository.actualizar(usuario);
        return usuario;
    }
    
    @Override
    public boolean puedeEliminarUsuario(Usuario usuario) {
        if (usuario == null) {
            return false;
        }
        
        // Verificar reservas activas
        List<Reserva> reservasActivas = reservaRepository.buscarPorUsuario(usuario)
                .stream()
                .filter(reserva -> reserva.getEstadoReserva() == EstadoReserva.ACTIVO)
                .toList();
        
        if (!reservasActivas.isEmpty()) {
            return false;
        }
        
        // Verificar ventas recientes (últimos 30 días)
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(DIAS_LIMITE_VENTAS_RECIENTES);
        List<Venta> ventasRecientes = ventaRepository.buscarPorUsuario(usuario)
                .stream()
                .filter(venta -> venta.getFechaVenta().isAfter(fechaLimite))
                .toList();
        
        return ventasRecientes.isEmpty();
    }
    
    @Override
    public void eliminarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        
        // Verificar que el usuario existe
        Optional<Usuario> usuarioExistente = usuarioRepository.buscarPorId(usuario.getId());
        if (usuarioExistente.isEmpty()) {
            throw new UsuarioNoEncontradoException(
                String.format("Usuario no encontrado con ID: %d", usuario.getId())
            );
        }
        
        // Verificar que puede ser eliminado
        if (!puedeEliminarUsuario(usuario)) {
            throw new UsuarioNoEliminableException(
                "No se puede eliminar el usuario porque tiene reservas activas o ventas recientes"
            );
        }
        
        // Eliminar el usuario
        usuarioRepository.eliminar(usuario.getId());
    }
    
    @Override
    public List<Usuario> buscarUsuarios(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return usuarioRepository.buscarTodos();
        }
        
        String criterioBusqueda = criterio.trim().toLowerCase();
        
        return usuarioRepository.buscarTodos().stream()
                .filter(usuario -> 
                    usuario.getNombre().toLowerCase().contains(criterioBusqueda) ||
                    usuario.getApellido().toLowerCase().contains(criterioBusqueda) ||
                    usuario.getEmail().toLowerCase().contains(criterioBusqueda)
                )
                .collect(Collectors.toList());
    }
}