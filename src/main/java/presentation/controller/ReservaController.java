package presentation.controller;

import application.usecases.*;
import domain.entities.Comic;
import domain.entities.Reserva;
import domain.entities.Usuario;

import java.util.List;
import java.util.Scanner;

/**
 * Controller especializado para la gestión de reservas.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo maneja operaciones de reservas
 * - Separación de concerns por contexto de dominio
 * - Delega lógica de negocio a casos de uso específicos
 */
public class ReservaController {
    
    private final Scanner scanner;
    private final ReservarLibroCasoUso reservarLibroCasoUso;
    private final CancelarReservaCasoUso cancelarReservaCasoUso;
    private final ConsultarReservasUsuarioCasoUso consultarReservasUsuarioCasoUso;
    
    // Referencias a otros controllers para selección de entidades
    private final UsuarioController usuarioController;
    private final ComicController comicController;
    
    public ReservaController(Scanner scanner,
                           ReservarLibroCasoUso reservarLibroCasoUso,
                           CancelarReservaCasoUso cancelarReservaCasoUso,
                           ConsultarReservasUsuarioCasoUso consultarReservasUsuarioCasoUso,
                           UsuarioController usuarioController,
                           ComicController comicController) {
        this.scanner = scanner;
        this.reservarLibroCasoUso = reservarLibroCasoUso;
        this.cancelarReservaCasoUso = cancelarReservaCasoUso;
        this.consultarReservasUsuarioCasoUso = consultarReservasUsuarioCasoUso;
        this.usuarioController = usuarioController;
        this.comicController = comicController;
    }
    
    public void mostrarMenu() {
        while (true) {
            System.out.println("\n┌─────────── GESTIÓN DE RESERVAS ──────────┐");
            System.out.println("│ 1. 📋 Reservar Cómic                      │");
            System.out.println("│ 2. ❌ Cancelar Reserva                     │");
            System.out.println("│ 3. 👤 Ver Reservas de Usuario              │");
            System.out.println("│ 0. ⬅️  Volver al Menú Principal           │");
            System.out.println("└───────────────────────────────────────────┘");
            System.out.print("Selecciona una opción: ");
            
            int opcion = leerOpcion();
            
            try {
                switch (opcion) {
                    case 1 -> reservarComic();
                    case 2 -> cancelarReserva();
                    case 3 -> verReservasUsuario();
                    case 0 -> { return; }
                    default -> System.out.println("❌ Opción no válida");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
                pausar();
            }
        }
    }
    
    private void reservarComic() {
        System.out.println("\n═══ RESERVAR CÓMIC ═══");
        
        Usuario usuario = usuarioController.seleccionarUsuario();
        if (usuario == null) return;
        
        Comic comic = comicController.seleccionarComic();
        if (comic == null) return;
        
        try {
            boolean puedeReservar = reservarLibroCasoUso.puedeReservar(usuario, comic);
            if (!puedeReservar) {
                System.out.println("❌ Este usuario no puede reservar este cómic en este momento.");
                System.out.println("Posibles razones: límite de reservas alcanzado, cómic no disponible, etc.");
                pausar();
                return;
            }
        } catch (Exception e) {
            System.out.println("❌ Error al verificar si se puede reservar: " + e.getMessage());
            pausar();
            return;
        }
        
        System.out.println("\n📋 Resumen de la reserva:");
        System.out.println("Usuario: ");
        usuarioController.mostrarUsuario(usuario);
        System.out.println("\nCómic: ");
        comicController.mostrarComicDetallado(comic);
        
        System.out.print("\n¿Confirmar reserva? (s/N): ");
        String confirmacion = scanner.nextLine().trim().toLowerCase();
        
        if (confirmacion.equals("s") || confirmacion.equals("si")) {
            try {
                Reserva reserva = reservarLibroCasoUso.ejecutar(usuario, comic);
                System.out.println("✅ Reserva creada exitosamente:");
                mostrarReserva(reserva);
            } catch (Exception e) {
                System.out.println("❌ Error al crear reserva: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Reserva cancelada.");
        }
        
        pausar();
    }
    
    private void cancelarReserva() {
        System.out.println("\n═══ CANCELAR RESERVA ═══");
        
        Usuario usuario = usuarioController.seleccionarUsuario();
        if (usuario == null) return;
        
        try {
            List<Reserva> reservas = consultarReservasUsuarioCasoUso.ejecutar(usuario);
            
            if (reservas.isEmpty()) {
                System.out.println("❌ Este usuario no tiene reservas activas.");
                pausar();
                return;
            }
            
            System.out.println("📋 Reservas activas del usuario:");
            for (int i = 0; i < reservas.size(); i++) {
                System.out.printf("%d. ", i + 1);
                mostrarReserva(reservas.get(i));
                System.out.println();
            }
            
            System.out.print("Selecciona el número de reserva a cancelar: ");
            int seleccion = leerOpcion();
            
            if (seleccion > 0 && seleccion <= reservas.size()) {
                Reserva reserva = reservas.get(seleccion - 1);
                
                System.out.println("\nReserva a cancelar:");
                mostrarReserva(reserva);
                
                System.out.print("\n¿Confirmar cancelación? (s/N): ");
                String confirmacion = scanner.nextLine().trim().toLowerCase();
                
                if (confirmacion.equals("s") || confirmacion.equals("si")) {
                    try {
                        cancelarReservaCasoUso.ejecutar(reserva);
                        System.out.println("✅ Reserva cancelada exitosamente.");
                    } catch (Exception e) {
                        System.out.println("❌ Error al cancelar reserva: " + e.getMessage());
                    }
                } else {
                    System.out.println("❌ Cancelación de reserva cancelada.");
                }
            } else {
                System.out.println("❌ Selección no válida.");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al consultar reservas: " + e.getMessage());
        }
        
        pausar();
    }
    
    private void verReservasUsuario() {
        System.out.println("\n═══ VER RESERVAS DE USUARIO ═══");
        
        Usuario usuario = usuarioController.seleccionarUsuario();
        if (usuario == null) return;
        
        try {
            List<Reserva> reservas = consultarReservasUsuarioCasoUso.ejecutar(usuario);
            
            if (reservas.isEmpty()) {
                System.out.println("❌ Este usuario no tiene reservas activas.");
            } else {
                System.out.println("📋 Reservas activas del usuario:");
                System.out.println("─".repeat(60));
                for (int i = 0; i < reservas.size(); i++) {
                    System.out.printf("%d. ", i + 1);
                    mostrarReserva(reservas.get(i));
                    System.out.println();
                }
                System.out.printf("\n📊 Total de reservas activas: %d\n", reservas.size());
            }
        } catch (Exception e) {
            System.out.println("❌ Error al consultar reservas: " + e.getMessage());
        }
        
        pausar();
    }
    
    public void mostrarReserva(Reserva reserva) {
        System.out.printf("ID: %s | Comic: %s | Usuario: %s | Estado: %s", 
            reserva.getId(),
            reserva.getComic().getNombre(),
            reserva.getUsuario().getNombreCompleto(),
            reserva.getEstadoReserva());
        if (reserva.getFechaExpiracionReserva() != null) {
            System.out.printf(" | Expira: %s", reserva.getFechaExpiracionReserva());
        }
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