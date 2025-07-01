package infrastructure.repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import domain.entities.Comic;
import domain.valueobjects.Divisa;
import exceptions.ComicNoEncontradoException;
import interfaces.repository.IComicRepository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ComicRepository implements IComicRepository {
    private final List<Comic> comics = new ArrayList<>();
    private final String csvFilePath = "comics.csv";

    public ComicRepository() {
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] nextLine;
            boolean isHeader = true;
            while ((nextLine = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                Comic comic = new Comic(
                        nextLine[1],
                        nextLine[2],
                        Divisa.pesos(Double.parseDouble(nextLine[3]))
                );
                comics.add(comic);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private void guardarCSV() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            writer.writeNext(new String[]{"id", "nombre", "descripcion", "precio"});
            for (Comic comic : comics) {
                writer.writeNext(new String[]{
                        comic.getId(),
                        comic.getNombre(),
                        comic.getDescription(),
                        String.valueOf(comic.getPrecio())
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void guardar(Comic comic) {
        if (comic == null) {
            throw new IllegalArgumentException("El cómic no puede ser nulo");
        }
        comics.add(comic);
        guardarCSV();
    }

    @Override
    public Optional<Comic> buscarPorId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        return comics.stream()
                .filter(comic -> comic.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Comic> buscarTodos() {
        return new ArrayList<>(comics);
    }

    @Override
    public List<Comic> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return comics.stream()
                .filter(comic -> comic.getNombre().toLowerCase().contains(nombre.toLowerCase().trim()))
                .collect(Collectors.toList());
    }

    @Override
    public void actualizar(Comic comic) {
        if (comic == null) {
            throw new IllegalArgumentException("El cómic no puede ser nulo");
        }

        for (int i = 0; i < comics.size(); i++) {
            if (comics.get(i).getId().equals(comic.getId())) {
                comics.set(i, comic);
                guardarCSV();
                return;
            }
        }

        throw new ComicNoEncontradoException("Cómic no encontrado con ID: " + comic.getId());
    }

    @Override
    public void eliminar(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID no puede ser nulo o vacío");
        }

        boolean removed = comics.removeIf(comic -> comic.getId().equals(id));

        if (!removed) {
            throw new ComicNoEncontradoException("Cómic no encontrado con ID: " + id);
        }

        guardarCSV();
    }
}