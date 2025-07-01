package presentation.controller;

import application.usecases.ComprarLibroCasoUso;
import domain.entities.Comic;
import domain.entities.Usuario;
import domain.entities.Venta;

import java.util.Scanner;

/**
 * Controller especializado para la gestión de ventas.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo maneja operaciones de ventas
 * - Separación de concerns por contexto de dominio
 * - Delega lógica de negocio a casos de uso específicos
 */
public class VentaController {
    
    private final Scanner scanner;
    private final ComprarLibroCasoUso comprarLibroCasoUso;
    
    // Referencias a otros controllers para selección de entidades
    private final UsuarioController usuarioController;
    private final ComicController comicController;
    
    public VentaController(Scanner scanner,
                          ComprarLibroCasoUso comprarLibroCasoUso,
                          UsuarioController usuarioController,
                          ComicController comicController) {
        this.scanner = scanner;
        this.comprarLibroCasoUso = comprarLibroCasoUso;
        this.usuarioController = usuarioController;
        this.comicController = comicController;
    }
    
    public void mostrarMenu() {
        while (true) {
            System.out.println("\n┌─────────── GESTIÓN DE VENTAS ──────────┐");
            System.out.println("│ 1. 💰 Comprar Cómic                     │");
            System.out.println("│ 0. ⬅️  Volver al Menú Principal         │");
            System.out.println("└─────────────────────────────────────────┘");
            System.out.print("Selecciona una opción: ");
            
            int opcion = leerOpcion();
            
            try {
                switch (opcion) {
                    case 1 -> comprarComic();
                    case 0 -> { return; }
                    default -> System.out.println("❌ Opción no válida");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
                pausar();
            }
        }
    }
    
    private void comprarComic() {
        System.out.println("\n═══ COMPRAR CÓMIC ═══");
        
        Usuario usuario = usuarioController.seleccionarUsuario();
        if (usuario == null) return;
        
        Comic comic = comicController.seleccionarComic();
        if (comic == null) return;
        
        try {
            boolean puedeComprar = comprarLibroCasoUso.puedeComprar(usuario, comic);
            if (!puedeComprar) {
                System.out.println("❌ Este usuario no puede comprar este cómic en este momento.");
                System.out.println("Posibles razones: cómic no disponible, problemas con reservas, etc.");
                pausar();
                return;
            }
        } catch (Exception e) {
            System.out.println("❌ Error al verificar si se puede comprar: " + e.getMessage());
            pausar();
            return;
        }
        
        System.out.println("\n🛒 Resumen de la compra:");
        System.out.println("Usuario: ");
        usuarioController.mostrarUsuario(usuario);
        System.out.println("\nCómic: ");
        comicController.mostrarComicDetallado(comic);
        
        System.out.print("\n¿Confirmar compra? (s/N): ");
        String confirmacion = scanner.nextLine().trim().toLowerCase();
        
        if (confirmacion.equals("s") || confirmacion.equals("si")) {
            try {
                Venta venta = comprarLibroCasoUso.ejecutar(usuario, comic);
                System.out.println("✅ Compra realizada exitosamente:");
                mostrarVenta(venta);
            } catch (Exception e) {
                System.out.println("❌ Error al procesar compra: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Compra cancelada.");
        }
        
        pausar();
    }
    
    public void mostrarVenta(Venta venta) {
        System.out.printf("ID: %s | Comic: %s | Usuario: %s | Precio: %s | Fecha: %s", 
            venta.getId(),
            venta.getComic().getNombre(),
            venta.getUsuario().getNombreCompleto(),
            venta.getPrecioFinal().toString(),
            venta.getFechaVenta());
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