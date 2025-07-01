package presentation.controller;

import application.usecases.*;

import java.util.Scanner;

/**
 * Controller principal que coordina todos los controllers especializados.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Actúa como orquestador de controllers especializados
 * - Cada controller maneja su propio contexto de dominio
 * - Mantiene responsabilidades separadas y cohesión alta
 * - Sigue el patrón de composición sobre herencia
 */
public class ComicCollectorMainController {
    
    private final Scanner scanner;
    
    // Controllers especializados por contexto
    private final UsuarioController usuarioController;
    private final ComicController comicController;
    private final ReservaController reservaController;
    private final VentaController ventaController;
    private final InventarioController inventarioController;
    private final SistemaController sistemaController;
    
    public ComicCollectorMainController(
            // Casos de uso de Usuarios
            RegistrarUsuarioCasoUso registrarUsuarioCasoUso,
            BuscarUsuariosCasoUso buscarUsuariosCasoUso,
            ActualizarUsuarioCasoUso actualizarUsuarioCasoUso,
            EliminarUsuarioCasoUso eliminarUsuarioCasoUso,
            // Casos de uso de Cómics
            AgregarLibroCasoUso agregarLibroCasoUso,
            BuscarComicsCasoUso buscarComicsCasoUso,
            ActualizarComicCasoUso actualizarComicCasoUso,
            EliminarLibroCasoUso eliminarLibroCasoUso,
            ConsultarCatalogoCasoUso consultarCatalogoCasoUso,
            ConsultarDisponibilidadComicCasoUso consultarDisponibilidadComicCasoUso,
            // Casos de uso de Reservas
            ReservarLibroCasoUso reservarLibroCasoUso,
            CancelarReservaCasoUso cancelarReservaCasoUso,
            ConsultarReservasUsuarioCasoUso consultarReservasUsuarioCasoUso,
            // Casos de uso de Ventas
            ComprarLibroCasoUso comprarLibroCasoUso,
            // Casos de uso de Inventario y Reportes
            GenerarReporteInventarioCasoUso generarReporteInventarioCasoUso,
            GenerarReporteComicsPopularesCasoUso generarReporteComicsPopularesCasoUso,
            GenerarReporteComicsMasReservadosCasoUso generarReporteComicsMasReservadosCasoUso,
            ConsultarComicsReservadosCasoUso consultarComicsReservadosCasoUso,
            ConsultarComicsSinActividadCasoUso consultarComicsSinActividadCasoUso,
            // Casos de uso del Sistema
            ProcesarReservasExpiradasCasoUso procesarReservasExpiradasCasoUso) {
        
        this.scanner = new Scanner(System.in);
        
        // Inicializar controllers especializados
        this.usuarioController = new UsuarioController(
            scanner, registrarUsuarioCasoUso, buscarUsuariosCasoUso, 
            actualizarUsuarioCasoUso, eliminarUsuarioCasoUso
        );
        
        this.comicController = new ComicController(
            scanner, agregarLibroCasoUso, buscarComicsCasoUso, 
            actualizarComicCasoUso, eliminarLibroCasoUso, 
            consultarCatalogoCasoUso, consultarDisponibilidadComicCasoUso
        );
        
        this.reservaController = new ReservaController(
            scanner, reservarLibroCasoUso, cancelarReservaCasoUso, 
            consultarReservasUsuarioCasoUso, usuarioController, comicController
        );
        
        this.ventaController = new VentaController(
            scanner, comprarLibroCasoUso, usuarioController, comicController
        );
        
        this.inventarioController = new InventarioController(
            scanner, generarReporteInventarioCasoUso, generarReporteComicsPopularesCasoUso,
            generarReporteComicsMasReservadosCasoUso, consultarComicsReservadosCasoUso,
            consultarComicsSinActividadCasoUso, comicController
        );
        
        this.sistemaController = new SistemaController(
            scanner, procesarReservasExpiradasCasoUso, reservaController
        );
    }
    
    /**
     * Inicia la aplicación de terminal.
     */
    public void iniciar() {
        mostrarBienvenida();
        
        while (true) {
            try {
                mostrarMenuPrincipal();
                int opcion = leerOpcion();
                
                if (opcion == 0) {
                    mostrarDespedida();
                    break;
                }
                
                procesarOpcionPrincipal(opcion);
                
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
                System.out.println("Presiona Enter para continuar...");
                scanner.nextLine();
            }
        }
    }
    
    private void mostrarBienvenida() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║        COMIC COLLECTOR SYSTEM         ║");
        System.out.println("║     Sistema de Gestión de Cómics      ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
    }
    
    private void mostrarMenuPrincipal() {
        System.out.println("\n┌─────────────── MENÚ PRINCIPAL ───────────────┐");
        System.out.println("│ 1. 👤 Gestión de Usuarios                    │");
        System.out.println("│ 2. 📚 Gestión de Cómics                      │");
        System.out.println("│ 3. 📋 Gestión de Reservas                    │");
        System.out.println("│ 4. 💰 Gestión de Ventas                      │");
        System.out.println("│ 5. 📊 Inventario y Reportes                  │");
        System.out.println("│ 6. ⚙️  Operaciones del Sistema                │");
        System.out.println("│ 0. 🚪 Salir                                   │");
        System.out.println("└───────────────────────────────────────────────┘");
        System.out.print("Selecciona una opción: ");
    }
    
    private void procesarOpcionPrincipal(int opcion) {
        switch (opcion) {
            case 1 -> usuarioController.mostrarMenu();
            case 2 -> comicController.mostrarMenu();
            case 3 -> reservaController.mostrarMenu();
            case 4 -> ventaController.mostrarMenu();
            case 5 -> inventarioController.mostrarMenu();
            case 6 -> sistemaController.mostrarMenu();
            default -> System.out.println("❌ Opción no válida");
        }
    }
    
    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private void mostrarDespedida() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║     ¡Gracias por usar el sistema!     ║");
        System.out.println("║         Comic Collector System        ║");
        System.out.println("╚════════════════════════════════════════╝");
    }
}