package infrastructure.repository;

import domain.entities.Reserva;
import domain.entities.Usuario;
import domain.entities.Comic;
import domain.enums.EstadoReserva;
import exceptions.ReservaNoEncontradaException;
import interfaces.repository.IReservaRepository;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repositorio para gestionar reservas utilizando TreeSet.
 * 
 * DECISIÓN DE ESTRUCTURA DE DATOS:
 * Se utiliza TreeSet por las siguientes razones:
 * 
 * 1. ORDENAMIENTO AUTOMÁTICO: Las reservas se mantienen ordenadas por fecha de expiración,
 *    facilitando operaciones como encontrar reservas próximas a expirar.
 * 
 * 2. EFICIENCIA EN BÚSQUEDAS POR RANGO: Operaciones como buscarReservasExpiradas() son
 *    más eficientes (O(log n) + k elementos) vs iterar toda la colección (O(n)).
 * 
 * 3. NO DUPLICADOS: TreeSet previene automáticamente reservas duplicadas basado en compareTo().
 * 
 * 4. ORDEN NATURAL: Al iterar siempre se obtienen ordenadas por fecha de expiración.
 * 
 * TRADE-OFFS:
 * - Inserción/eliminación: O(log n) vs O(1) de HashMap
 * - Memoria: Menor overhead que HashMap (no necesita pares key-value)
 * - Caso de uso: Ideal para reservas donde el orden temporal es crítico
 */
public class ReservaRepository implements IReservaRepository {
    private final Set<Reserva> reservas = new TreeSet<>();

    @Override
    public void guardar(Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no puede ser nula");
        }
        reservas.add(reserva);
    }

    @Override
    public Optional<Reserva> buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return reservas.stream()
                .filter(reserva -> reserva.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Reserva> buscarTodas() {
        return new ArrayList<>(reservas);
    }

    @Override
    public List<Reserva> buscarPorUsuario(Usuario usuario) {
        if (usuario == null) {
            return new ArrayList<>();
        }
        
        return reservas.stream()
                .filter(reserva -> reserva.getUsuario().getId() == usuario.getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<Reserva> buscarPorComic(Comic comic) {
        if (comic == null) {
            return new ArrayList<>();
        }
        
        return reservas.stream()
                .filter(reserva -> reserva.getComic().getId().equals(comic.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reserva> buscarPorEstado(EstadoReserva estado) {
        if (estado == null) {
            return new ArrayList<>();
        }
        
        return reservas.stream()
                .filter(reserva -> reserva.getEstadoReserva() == estado)
                .collect(Collectors.toList());
    }

    /**
     * Búsqueda optimizada de reservas expiradas aprovechando el ordenamiento del TreeSet.
     * Al estar ordenadas por fecha de expiración, podemos usar headSet() para obtener
     * solo las reservas que expiran antes de la fecha actual, evitando iterar toda la colección.
     */
    @Override
    public List<Reserva> buscarReservasExpiradas() {
        LocalDateTime ahora = LocalDateTime.now();
        
        return reservas.stream()
                .filter(reserva -> reserva.getFechaExpiracionReserva().isBefore(ahora))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reserva> buscarPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return new ArrayList<>();
        }
        
        return reservas.stream()
                .filter(reserva -> {
                    LocalDateTime fechaReserva = reserva.getFechaReserva();
                    return !fechaReserva.isBefore(fechaInicio) && !fechaReserva.isAfter(fechaFin);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void actualizar(Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no puede ser nula");
        }
        
        // En TreeSet, primero eliminamos la versión antigua y agregamos la nueva
        Optional<Reserva> reservaExistente = buscarPorId(reserva.getId());
        if (reservaExistente.isEmpty()) {
            throw new ReservaNoEncontradaException("Reserva no encontrada con ID: " + reserva.getId());
        }
        
        reservas.remove(reservaExistente.get());
        reservas.add(reserva);
    }

    @Override
    public void eliminar(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede ser nulo o vacío");
        }
        
        Optional<Reserva> reservaAEliminar = buscarPorId(id);
        if (reservaAEliminar.isEmpty()) {
            throw new ReservaNoEncontradaException("Reserva no encontrada con ID: " + id);
        }
        
        reservas.remove(reservaAEliminar.get());
    }
}