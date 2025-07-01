package domain.services;

import domain.entities.Comic;
import domain.entities.Reserva;
import domain.entities.Venta;
import domain.enums.EstadoReserva;
import domain.valueobjects.Divisa;
import exceptions.ComicNoEliminableException;
import exceptions.ComicNoEncontradoException;
import exceptions.NombreComicYaExisteException;
import interfaces.domain.IComicService;
import interfaces.repository.IComicRepository;
import interfaces.repository.IReservaRepository;
import interfaces.repository.IVentaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ComicService implements IComicService {
    
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
    public ComicService(IComicRepository comicRepository,
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
    public Comic agregarComic(String nombre, String descripcion, Divisa precio) {
        // Validaciones de entrada (las validaciones básicas las hace la entidad)
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cómic no puede ser nulo o vacío");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción del cómic no puede ser nula o vacía");
        }
        if (precio == null) {
            throw new IllegalArgumentException("El precio del cómic no puede ser nulo");
        }
        
        // Verificar que el nombre no exista
//        if (nombreComicYaExiste(nombre)) {
//            throw new NombreComicYaExisteException(
//                String.format("Ya existe un cómic con el nombre: %s", nombre)
//            );
//        }
//
        // Crear y guardar el cómic
        Comic nuevoComic = new Comic(nombre, descripcion, precio);
        comicRepository.guardar(nuevoComic);
        
        return nuevoComic;
    }
    
    @Override
    public boolean puedeEliminarComic(Comic comic) {
        if (comic == null) {
            return false;
        }
        
        // Verificar reservas activas
        List<Reserva> reservasActivas = reservaRepository.buscarPorComic(comic)
                .stream()
                .filter(reserva -> reserva.getEstadoReserva() == EstadoReserva.ACTIVO)
                .toList();
        
        if (!reservasActivas.isEmpty()) {
            return false;
        }
        
        // Verificar si tiene ventas asociadas
        List<Venta> ventasDelComic = ventaRepository.buscarPorComic(comic);
        
        return ventasDelComic.isEmpty();
    }
    
    @Override
    public void eliminarComic(Comic comic) {
        if (comic == null) {
            throw new IllegalArgumentException("El cómic no puede ser nulo");
        }
        
        // Verificar que el cómic existe
        Optional<Comic> comicExistente = comicRepository.buscarPorId(comic.getId());
        if (comicExistente.isEmpty()) {
            throw new ComicNoEncontradoException(
                String.format("Cómic no encontrado con ID: %s", comic.getId())
            );
        }
        
        // Verificar que puede ser eliminado
        if (!puedeEliminarComic(comic)) {
            throw new ComicNoEliminableException(
                "No se puede eliminar el cómic porque tiene reservas activas o ventas asociadas"
            );
        }
        
        // Eliminar el cómic
        comicRepository.eliminar(comic.getId());
    }
    
    @Override
    public Comic actualizarComic(Comic comic) {
        if (comic == null) {
            throw new IllegalArgumentException("El cómic no puede ser nulo");
        }
        
        // Verificar que el cómic existe
        Optional<Comic> comicExistente = comicRepository.buscarPorId(comic.getId());
        if (comicExistente.isEmpty()) {
            throw new ComicNoEncontradoException(
                String.format("Cómic no encontrado con ID: %s", comic.getId())
            );
        }
        
        // Verificar que el nombre no esté siendo usado por otro cómic
        List<Comic> comicsConMismoNombre = comicRepository.buscarPorNombre(comic.getNombre());
        boolean nombreDuplicado = comicsConMismoNombre.stream()
                .anyMatch(c -> !c.getId().equals(comic.getId()));
        
        if (nombreDuplicado) {
            throw new NombreComicYaExisteException(
                String.format("El nombre %s ya está siendo usado por otro cómic", comic.getNombre())
            );
        }
        
        // Actualizar el cómic
        comicRepository.actualizar(comic);
        return comic;
    }
    
    @Override
    public List<Comic> buscarComics(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return comicRepository.buscarTodos();
        }
        
        String criterioBusqueda = criterio.trim().toLowerCase();
        
        return comicRepository.buscarTodos().stream()
                .filter(comic -> 
                    comic.getNombre().toLowerCase().contains(criterioBusqueda) ||
                    comic.getDescription().toLowerCase().contains(criterioBusqueda)
                )
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean nombreComicYaExiste(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        
        List<Comic> comicsConNombre = comicRepository.buscarPorNombre(nombre.trim());
        return !comicsConNombre.isEmpty();
    }
    
    @Override
    public List<Comic> obtenerComicsDisponibles() {
        List<Comic> todosLosComics = comicRepository.buscarTodos();
        List<Reserva> reservasActivas = reservaRepository.buscarPorEstado(EstadoReserva.ACTIVO);
        
        // Obtener IDs de comics con reservas activas
        List<String> comicsReservados = reservasActivas.stream()
                .map(reserva -> reserva.getComic().getId())
                .toList();
        
        // Filtrar comics que no están reservados
        return todosLosComics.stream()
                .filter(comic -> !comicsReservados.contains(comic.getId()))
                .collect(Collectors.toList());
    }
}