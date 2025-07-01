package domain.services;

import domain.entities.Comic;
import domain.entities.Reserva;
import domain.entities.Venta;
import domain.enums.EstadoReserva;
import interfaces.domain.IInventarioService;
import interfaces.repository.IComicRepository;
import interfaces.repository.IReservaRepository;
import interfaces.repository.IVentaRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class InventarioService implements IInventarioService {
    
    private final IComicRepository comicRepository;
    private final IReservaRepository reservaRepository;
    private final IVentaRepository ventaRepository;
    
    /**
     * Constructor que recibe las dependencias necesarias (Dependency Injection).
     * 
     * @param comicRepository Repositorio de comics
     * @param reservaRepository Repositorio de reservas
     * @param ventaRepository Repositorio de ventas
     */
    public InventarioService(IComicRepository comicRepository,
                            IReservaRepository reservaRepository,
                            IVentaRepository ventaRepository) {
        if (comicRepository == null) {
            throw new IllegalArgumentException("El repositorio de comics no puede ser nulo");
        }
        if (reservaRepository == null) {
            throw new IllegalArgumentException("El repositorio de reservas no puede ser nulo");
        }
        if (ventaRepository == null) {
            throw new IllegalArgumentException("El repositorio de ventas no puede ser nulo");
        }
        
        this.comicRepository = comicRepository;
        this.reservaRepository = reservaRepository;
        this.ventaRepository = ventaRepository;
    }
    
    @Override
    public List<Comic> obtenerComicsDisponibles() {
        List<Comic> todosLosComics = comicRepository.buscarTodos();
        List<Reserva> reservasActivas = reservaRepository.buscarPorEstado(EstadoReserva.ACTIVO);
        
        // Obtener IDs de comics con reservas activas
        Set<String> comicsReservados = reservasActivas.stream()
                .map(reserva -> reserva.getComic().getId())
                .collect(Collectors.toSet());
        
        // Filtrar comics que no están reservados
        return todosLosComics.stream()
                .filter(comic -> !comicsReservados.contains(comic.getId()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Comic> obtenerComicsReservados() {
        List<Reserva> reservasActivas = reservaRepository.buscarPorEstado(EstadoReserva.ACTIVO);
        
        return reservasActivas.stream()
                .map(Reserva::getComic)
                .distinct()
                .collect(Collectors.toList());
    }
    
    @Override
    public Map<Comic, Long> obtenerComicsMasVendidos(int limite) {
        if (limite <= 0) {
            throw new IllegalArgumentException("El límite debe ser mayor a 0");
        }
        
        List<Venta> todasLasVentas = ventaRepository.buscarTodas();
        
        Map<Comic, Long> resultado = new LinkedHashMap<>();
        todasLasVentas.stream()
                .collect(Collectors.groupingBy(Venta::getComic, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Comic, Long>comparingByValue().reversed())
                .limit(limite)
                .forEach(entry -> resultado.put(entry.getKey(), entry.getValue()));
        
        return resultado;
    }
    
    @Override
    public Map<Comic, Long> obtenerComicsMasReservados(int limite) {
        if (limite <= 0) {
            throw new IllegalArgumentException("El límite debe ser mayor a 0");
        }
        
        List<Reserva> todasLasReservas = reservaRepository.buscarTodas();
        
        Map<Comic, Long> resultado = new LinkedHashMap<>();
        todasLasReservas.stream()
                .collect(Collectors.groupingBy(Reserva::getComic, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<Comic, Long>comparingByValue().reversed())
                .limit(limite)
                .forEach(entry -> resultado.put(entry.getKey(), entry.getValue()));
        
        return resultado;
    }
    
    @Override
    public Map<String, Long> obtenerEstadisticasInventario() {
        Map<String, Long> estadisticas = new HashMap<>();
        
        // Total de comics
        long totalComics = comicRepository.buscarTodos().size();
        estadisticas.put("Total Comics", totalComics);
        
        // Comics disponibles
        long comicsDisponibles = obtenerComicsDisponibles().size();
        estadisticas.put("Comics Disponibles", comicsDisponibles);
        
        // Comics reservados
        long comicsReservados = obtenerComicsReservados().size();
        estadisticas.put("Comics Reservados", comicsReservados);
        
        // Total de ventas
        long totalVentas = ventaRepository.buscarTodas().size();
        estadisticas.put("Total Ventas", totalVentas);
        
        // Total de reservas activas
        long reservasActivas = reservaRepository.buscarPorEstado(EstadoReserva.ACTIVO).size();
        estadisticas.put("Reservas Activas", reservasActivas);
        
        // Comics sin actividad
        long comicsSinActividad = obtenerComicsSinActividad().size();
        estadisticas.put("Comics Sin Actividad", comicsSinActividad);
        
        return estadisticas;
    }
    
    @Override
    public boolean verificarDisponibilidad(Comic comic) {
        if (comic == null) {
            return false;
        }
        
        List<Reserva> reservasDelComic = reservaRepository.buscarPorComic(comic);
        
        return reservasDelComic.stream()
                .noneMatch(reserva -> reserva.getEstadoReserva() == EstadoReserva.ACTIVO);
    }
    
    @Override
    public List<Comic> obtenerComicsSinActividad() {
        List<Comic> todosLosComics = comicRepository.buscarTodos();
        List<Venta> todasLasVentas = ventaRepository.buscarTodas();
        List<Reserva> todasLasReservas = reservaRepository.buscarTodas();
        
        // IDs de comics con ventas
        Set<String> comicsConVentas = todasLasVentas.stream()
                .map(venta -> venta.getComic().getId())
                .collect(Collectors.toSet());
        
        // IDs de comics con reservas
        Set<String> comicsConReservas = todasLasReservas.stream()
                .map(reserva -> reserva.getComic().getId())
                .collect(Collectors.toSet());
        
        // Filtrar comics sin actividad
        return todosLosComics.stream()
                .filter(comic -> !comicsConVentas.contains(comic.getId()) && 
                                !comicsConReservas.contains(comic.getId()))
                .collect(Collectors.toList());
    }
}