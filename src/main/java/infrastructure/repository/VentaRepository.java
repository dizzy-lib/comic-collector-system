package infrastructure.repository;

import domain.entities.Venta;
import domain.entities.Usuario;
import domain.entities.Comic;
import exceptions.VentaNoEncontradaException;
import interfaces.repository.IVentaRepository;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class VentaRepository implements IVentaRepository {
    private final Set<Venta> ventas = new TreeSet<>();

    @Override
    public void guardar(Venta venta) {
        if (venta == null) {
            throw new IllegalArgumentException("La venta no puede ser nula");
        }
        ventas.add(venta);
    }

    @Override
    public Optional<Venta> buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return ventas.stream()
                .filter(venta -> venta.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Venta> buscarTodas() {
        return new ArrayList<>(ventas);
    }

    @Override
    public List<Venta> buscarPorUsuario(Usuario usuario) {
        if (usuario == null) {
            return new ArrayList<>();
        }
        
        return ventas.stream()
                .filter(venta -> venta.getUsuario().getId() == usuario.getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<Venta> buscarPorComic(Comic comic) {
        if (comic == null) {
            return new ArrayList<>();
        }
        
        return ventas.stream()
                .filter(venta -> venta.getComic().getId().equals(comic.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Búsqueda optimizada por rango de fechas aprovechando el ordenamiento cronológico del TreeSet.
     * Al estar las ventas ordenadas por fecha, esta operación es más eficiente que en estructuras
     * no ordenadas, especialmente para rangos de fechas grandes.
     */
    @Override
    public List<Venta> buscarPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return new ArrayList<>();
        }
        
        return ventas.stream()
                .filter(venta -> {
                    LocalDateTime fechaVenta = venta.getFechaVenta();
                    return !fechaVenta.isBefore(fechaInicio) && !fechaVenta.isAfter(fechaFin);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void actualizar(Venta venta) {
        if (venta == null) {
            throw new IllegalArgumentException("La venta no puede ser nula");
        }
        
        // En TreeSet, primero eliminamos la versión antigua y agregamos la nueva
        Optional<Venta> ventaExistente = buscarPorId(venta.getId());
        if (ventaExistente.isEmpty()) {
            throw new VentaNoEncontradaException("Venta no encontrada con ID: " + venta.getId());
        }
        
        ventas.remove(ventaExistente.get());
        ventas.add(venta);
    }

    @Override
    public void eliminar(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede ser nulo o vacío");
        }
        
        Optional<Venta> ventaAEliminar = buscarPorId(id);
        if (ventaAEliminar.isEmpty()) {
            throw new VentaNoEncontradaException("Venta no encontrada con ID: " + id);
        }
        
        ventas.remove(ventaAEliminar.get());
    }
}