package domain.services;

import domain.entities.Venta;
import domain.entities.Usuario;
import domain.entities.Comic;
import domain.entities.Reserva;
import domain.enums.EstadoReserva;
import exceptions.VentaNoProcesableException;
import exceptions.ComicNoDisponibleParaVentaException;
import interfaces.domain.IVentaService;
import interfaces.repository.IComicRepository;
import interfaces.repository.IVentaRepository;
import interfaces.repository.IReservaRepository;

import java.util.List;
import java.util.Optional;


public class VentaService implements IVentaService {
    
    private final IVentaRepository ventaRepository;
    private final IReservaRepository reservaRepository;
    private final IComicRepository comicRepository;
    
    /**
     * Constructor que recibe las dependencias necesarias (Dependency Injection).
     * 
     * @param ventaRepository Repositorio de ventas
     * @param reservaRepository Repositorio de reservas
     */
    public VentaService(
            IVentaRepository ventaRepository,
            IReservaRepository reservaRepository,
            IComicRepository comicRepository
    ) {
        if (ventaRepository == null) {
            throw new IllegalArgumentException("El repositorio de ventas no puede ser nulo");
        }
        if (reservaRepository == null) {
            throw new IllegalArgumentException("El repositorio de reservas no puede ser nulo");
        }
        
        this.ventaRepository = ventaRepository;
        this.reservaRepository = reservaRepository;
        this.comicRepository = comicRepository;
    }
    
    @Override
    public boolean puedeComprar(Usuario usuario, Comic comic) {
        if (usuario == null || comic == null) {
            return false;
        }
        
        // Verificar si el cómic está disponible para este usuario
        return estaDisponibleParaVenta(comic, usuario);
    }
    
    @Override
    public Venta procesarVenta(Usuario usuario, Comic comic) {
        // Validaciones de entrada
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        if (comic == null) {
            throw new IllegalArgumentException("El cómic no puede ser nulo");
        }
        
        // Verificar disponibilidad
        if (!estaDisponibleParaVenta(comic, usuario)) {
            throw new ComicNoDisponibleParaVentaException(
                String.format("El cómic '%s' no está disponible para venta", comic.getNombre())
            );
        }
        
        // Verificar y procesar reserva si existe
        Optional<Reserva> reservaExistente = verificarYProcesarReserva(usuario, comic);
        
        // Crear la venta
        Venta nuevaVenta = new Venta(usuario, comic);
        
        try {
            // Guardar la venta
            ventaRepository.guardar(nuevaVenta);
            
            // Si había una reserva, cancelarla después de crear la venta exitosamente
            if (reservaExistente.isPresent()) {
                Reserva reserva = reservaExistente.get();
                reserva.setReservaInactiva();
                reservaRepository.actualizar(reserva);
            }

            comicRepository.eliminar(comic.getId());

            return nuevaVenta;
            
        } catch (Exception e) {
            throw new VentaNoProcesableException(
                String.format("Error al procesar la venta: %s", e.getMessage())
            );
        }
    }
    
    @Override
    public Optional<Reserva> verificarYProcesarReserva(Usuario usuario, Comic comic) {
        if (usuario == null || comic == null) {
            return Optional.empty();
        }
        
        // Buscar reservas activas del usuario para este cómic
        List<Reserva> reservasDelUsuario = reservaRepository.buscarPorUsuario(usuario);
        
        return reservasDelUsuario.stream()
                .filter(reserva -> reserva.getEstadoReserva() == EstadoReserva.ACTIVO)
                .filter(reserva -> reserva.getComic().getId().equals(comic.getId()))
                .findFirst();
    }
    
    @Override
    public boolean estaDisponibleParaVenta(Comic comic, Usuario usuario) {
        if (comic == null || usuario == null) {
            return false;
        }
        
        // Buscar todas las reservas activas para este cómic
        List<Reserva> reservasDelComic = reservaRepository.buscarPorComic(comic);
        
        List<Reserva> reservasActivas = reservasDelComic.stream()
                .filter(reserva -> reserva.getEstadoReserva() == EstadoReserva.ACTIVO)
                .toList();
        
        // Si no hay reservas activas, está disponible
        if (reservasActivas.isEmpty()) {
            return true;
        }
        
        // Si hay reservas activas, verificar si alguna pertenece al usuario
        return reservasActivas.stream()
                .anyMatch(reserva -> reserva.getUsuario().getId() == usuario.getId());
    }
}