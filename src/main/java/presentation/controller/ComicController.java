package presentation.controller;

import application.usecases.*;
import domain.entities.Comic;
import domain.valueobjects.Divisa;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * Controller especializado para la gestión de cómics.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo maneja operaciones de cómics
 * - Separación de concerns por contexto de dominio
 * - Delega lógica de negocio a casos de uso específicos
 */
public class ComicController {
    
    private final Scanner scanner;
    private final AgregarLibroCasoUso agregarLibroCasoUso;
    private final BuscarComicsCasoUso buscarComicsCasoUso;
    private final ActualizarComicCasoUso actualizarComicCasoUso;
    private final EliminarLibroCasoUso eliminarLibroCasoUso;
    private final ConsultarCatalogoCasoUso consultarCatalogoCasoUso;
    private final ConsultarDisponibilidadComicCasoUso consultarDisponibilidadComicCasoUso;
    
    public ComicController(Scanner scanner,
                          AgregarLibroCasoUso agregarLibroCasoUso,
                          BuscarComicsCasoUso buscarComicsCasoUso,
                          ActualizarComicCasoUso actualizarComicCasoUso,
                          EliminarLibroCasoUso eliminarLibroCasoUso,
                          ConsultarCatalogoCasoUso consultarCatalogoCasoUso,
                          ConsultarDisponibilidadComicCasoUso consultarDisponibilidadComicCasoUso) {
        this.scanner = scanner;
        this.agregarLibroCasoUso = agregarLibroCasoUso;
        this.buscarComicsCasoUso = buscarComicsCasoUso;
        this.actualizarComicCasoUso = actualizarComicCasoUso;
        this.eliminarLibroCasoUso = eliminarLibroCasoUso;
        this.consultarCatalogoCasoUso = consultarCatalogoCasoUso;
        this.consultarDisponibilidadComicCasoUso = consultarDisponibilidadComicCasoUso;
    }
    
    public void mostrarMenu() {
        while (true) {
            System.out.println("\n┌─────────── GESTIÓN DE CÓMICS ──────────┐");
            System.out.println("│ 1. ➕ Agregar Cómic                      │");
            System.out.println("│ 2. 🔍 Buscar Cómics                      │");
            System.out.println("│ 3. 📋 Ver Catálogo Completo              │");
            System.out.println("│ 4. ✏️  Actualizar Cómic                   │");
            System.out.println("│ 5. 🗑️  Eliminar Cómic                     │");
            System.out.println("│ 6. ✅ Consultar Disponibilidad            │");
            System.out.println("│ 0. ⬅️  Volver al Menú Principal           │");
            System.out.println("└───────────────────────────────────────────┘");
            System.out.print("Selecciona una opción: ");
            
            int opcion = leerOpcion();
            
            try {
                switch (opcion) {
                    case 1 -> agregarComic();
                    case 2 -> buscarComics();
                    case 3 -> verCatalogo();
                    case 4 -> actualizarComic();
                    case 5 -> eliminarComic();
                    case 6 -> consultarDisponibilidad();
                    case 0 -> { return; }
                    default -> System.out.println("❌ Opción no válida");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
                pausar();
            }
        }
    }
    
    private void agregarComic() {
        System.out.println("\n═══ AGREGAR NUEVO CÓMIC ═══");
        
        System.out.print("Nombre del cómic: ");
        String nombre = scanner.nextLine().trim();
        
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine().trim();
        
        System.out.print("Precio (en pesos): ");
        String precioStr = scanner.nextLine().trim();
        
        try {
            BigDecimal precio = new BigDecimal(precioStr);
            Divisa divisa = Divisa.pesos(precio);
            
            Comic comic = agregarLibroCasoUso.ejecutar(nombre, descripcion, divisa);
            System.out.println("✅ Cómic agregado exitosamente:");
            mostrarComic(comic);
        } catch (NumberFormatException e) {
            System.out.println("❌ Error: Precio debe ser un número válido");
        } catch (Exception e) {
            System.out.println("❌ Error al agregar cómic: " + e.getMessage());
        }
        
        pausar();
    }
    
    private void buscarComics() {
        System.out.println("\n═══ BUSCAR CÓMICS ═══");
        System.out.print("Criterio de búsqueda (nombre o descripción): ");
        String criterio = scanner.nextLine().trim();
        
        try {
            List<Comic> comics = buscarComicsCasoUso.ejecutar(criterio);
            
            if (comics.isEmpty()) {
                System.out.println("❌ No se encontraron cómics con ese criterio.");
            } else {
                System.out.println("📋 Cómics encontrados:");
                System.out.println("─".repeat(80));
                for (int i = 0; i < comics.size(); i++) {
                    System.out.printf("%d. ", i + 1);
                    mostrarComic(comics.get(i));
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error al buscar cómics: " + e.getMessage());
        }
        
        pausar();
    }
    
    private void verCatalogo() {
        System.out.println("\n═══ CATÁLOGO COMPLETO ═══");
        
        try {
            List<Comic> comics = consultarCatalogoCasoUso.ejecutar();
            
            if (comics.isEmpty()) {
                System.out.println("❌ No hay cómics en el catálogo.");
            } else {
                System.out.println("📚 Cómics disponibles:");
                System.out.println("─".repeat(80));
                for (int i = 0; i < comics.size(); i++) {
                    System.out.printf("%d. ", i + 1);
                    mostrarComic(comics.get(i));
                    System.out.println();
                }
                System.out.printf("\n📊 Total de cómics: %d\n", comics.size());
            }
        } catch (Exception e) {
            System.out.println("❌ Error al consultar catálogo: " + e.getMessage());
        }
        
        pausar();
    }
    
    private void actualizarComic() {
        System.out.println("\n═══ ACTUALIZAR CÓMIC ═══");
        
        Comic comic = seleccionarComic();
        if (comic == null) return;
        
        System.out.println("Datos actuales:");
        mostrarComicDetallado(comic);
        
        System.out.println("\nIngresa los nuevos datos (deja vacío para mantener el actual):");
        
        System.out.print("Nueva descripción [" + comic.getDescription() + "]: ");
        String nuevaDescripcion = scanner.nextLine().trim();
        if (nuevaDescripcion.isEmpty()) nuevaDescripcion = comic.getDescription();
        
        System.out.print("Nuevo precio [" + comic.getPrecio().getMonto() + "]: ");
        String nuevoPrecioStr = scanner.nextLine().trim();
        BigDecimal nuevoPrecio = comic.getPrecio().getMonto();
        if (!nuevoPrecioStr.isEmpty()) {
            try {
                nuevoPrecio = new BigDecimal(nuevoPrecioStr);
            } catch (NumberFormatException e) {
                System.out.println("❌ Precio inválido, manteniendo el actual.");
                nuevoPrecio = comic.getPrecio().getMonto();
            }
        }
        
        try {
            comic.setDescription(nuevaDescripcion);
            comic.setPrecio(Divisa.pesos(nuevoPrecio));
            
            Comic resultado = actualizarComicCasoUso.ejecutar(comic);
            System.out.println("✅ Cómic actualizado exitosamente:");
            mostrarComicDetallado(resultado);
        } catch (Exception e) {
            System.out.println("❌ Error al actualizar cómic: " + e.getMessage());
        }
        
        pausar();
    }
    
    private void eliminarComic() {
        System.out.println("\n═══ ELIMINAR CÓMIC ═══");
        
        Comic comic = seleccionarComic();
        if (comic == null) return;
        
        System.out.println("Cómic a eliminar:");
        mostrarComicDetallado(comic);
        
        try {
            boolean puedeEliminar = eliminarLibroCasoUso.puedeEliminar(comic);
            if (!puedeEliminar) {
                System.out.println("❌ Este cómic no puede ser eliminado porque tiene reservas activas o ventas asociadas.");
                pausar();
                return;
            }
        } catch (Exception e) {
            System.out.println("❌ Error al verificar si se puede eliminar: " + e.getMessage());
            pausar();
            return;
        }
        
        System.out.print("¿Estás seguro de eliminar este cómic? (s/N): ");
        String confirmacion = scanner.nextLine().trim().toLowerCase();
        
        if (confirmacion.equals("s") || confirmacion.equals("si")) {
            try {
                eliminarLibroCasoUso.ejecutar(comic);
                System.out.println("✅ Cómic eliminado exitosamente.");
            } catch (Exception e) {
                System.out.println("❌ Error al eliminar cómic: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Eliminación cancelada.");
        }
        
        pausar();
    }
    
    private void consultarDisponibilidad() {
        System.out.println("\n═══ CONSULTAR DISPONIBILIDAD ═══");
        
        Comic comic = seleccionarComic();
        if (comic == null) return;
        
        try {
            boolean disponible = consultarDisponibilidadComicCasoUso.ejecutar(comic);
            
            System.out.println("📊 Estado del cómic:");
            mostrarComicDetallado(comic);
            System.out.println();
            
            if (disponible) {
                System.out.println("✅ Estado: DISPONIBLE para reserva y venta");
            } else {
                System.out.println("❌ Estado: NO DISPONIBLE (puede estar reservado o sin stock)");
            }
        } catch (Exception e) {
            System.out.println("❌ Error al consultar disponibilidad: " + e.getMessage());
        }
        
        pausar();
    }
    
    /**
     * Método público para que otros controllers puedan seleccionar cómics.
     */
    public Comic seleccionarComic() {
        System.out.print("Ingresa criterio para buscar el cómic: ");
        String criterio = scanner.nextLine().trim();
        
        try {
            List<Comic> comics = buscarComicsCasoUso.ejecutar(criterio);
            
            if (comics.isEmpty()) {
                System.out.println("❌ No se encontraron cómics.");
                return null;
            }
            
            if (comics.size() == 1) {
                return comics.get(0);
            }
            
            System.out.println("Múltiples cómics encontrados:");
            for (int i = 0; i < comics.size(); i++) {
                System.out.printf("%d. ", i + 1);
                mostrarComic(comics.get(i));
                System.out.println();
            }
            
            System.out.print("Selecciona el número de cómic: ");
            int seleccion = leerOpcion();
            
            if (seleccion > 0 && seleccion <= comics.size()) {
                return comics.get(seleccion - 1);
            } else {
                System.out.println("❌ Selección no válida.");
                return null;
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al buscar cómics: " + e.getMessage());
            return null;
        }
    }
    
    public void mostrarComic(Comic comic) {
        System.out.printf("%s - %s", 
            comic.getNombre(),
            comic.getPrecio().toString());
    }
    
    public void mostrarComicDetallado(Comic comic) {
        System.out.printf("ID: %s\n", comic.getId());
        System.out.printf("Nombre: %s\n", comic.getNombre());
        System.out.printf("Descripción: %s\n", comic.getDescription());
        System.out.printf("Precio: %s\n", comic.getPrecio().toString());
    }
    
    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private void pausar() {
        System.out.println("\nPresiona Enter para continuar...");
        scanner.nextLine();
    }
}