package infrastructure.repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import domain.entities.Usuario;
import exceptions.UsuarioNoEncontradoException;
import interfaces.repository.IUsuarioRepository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class UsuarioRepository implements IUsuarioRepository {
    private final Map<Integer, Usuario> usuarios = new HashMap<>();
    private final String csvFilePath = "usuarios.csv";
    private int idGenerator = 1;

    public UsuarioRepository() {
        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] nextLine;
            boolean isHeader = true;
            while ((nextLine = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                int id = Integer.parseInt(nextLine[0]);
                Usuario usuario = new Usuario(
                        nextLine[1], // nombre
                        nextLine[2], // apellido
                        nextLine[3]  // email
                );
                usuarios.put(id, usuario);
                if (id >= idGenerator) {
                    idGenerator = id + 1;
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private void guardarCSV() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            writer.writeNext(new String[]{"id", "nombre", "apellido", "email"});
            for (Usuario u : usuarios.values()) {
                writer.writeNext(new String[]{
                        String.valueOf(u.getId()),
                        u.getNombre(),
                        u.getApellido(),
                        u.getEmail()
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void guardar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }

        int id = idGenerator++;
        usuario.setId(id);
        usuarios.put(id, usuario);
        guardarCSV();
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) {
        return Optional.ofNullable(usuarios.get(id));
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }

        return usuarios.values().stream()
                .filter(usuario -> email.equalsIgnoreCase(usuario.getEmail()))
                .findFirst();
    }

    @Override
    public List<Usuario> buscarTodos() {
        return new ArrayList<>(usuarios.values());
    }

    @Override
    public void actualizar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }

        int id = usuario.getId();
        if (!usuarios.containsKey(id)) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + id);
        }

        usuarios.put(id, usuario);
        guardarCSV();
    }

    @Override
    public void eliminar(int id) {
        if (!usuarios.containsKey(id)) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado con ID: " + id);
        }

        usuarios.remove(id);
        guardarCSV();
    }
}