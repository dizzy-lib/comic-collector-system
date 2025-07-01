package presentation.controller;

import application.usecases.ProcesarReservasExpiradasCasoUso;
import domain.entities.Reserva;

import java.util.List;
import java.util.Scanner;

/**
 * Controller especializado para operaciones del sistema.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo maneja operaciones administrativas del sistema
 * - Separación de concerns por contexto de dominio
 * - Delega lógica de negocio a casos de uso específicos
 */
public class SistemaController {
    
    private final Scanner scanner;
    private final ProcesarReservasExpiradasCasoUso procesarReservasExpiradasCasoUso;
    
    // Referencia para mostrar reservas
    private final ReservaController reservaController;
    
    public SistemaController(Scanner scanner,
                           ProcesarReservasExpiradasCasoUso procesarReservasExpiradasCasoUso,
                           ReservaController reservaController) {
        this.scanner = scanner;
        this.procesarReservasExpiradasCasoUso = procesarReservasExpiradasCasoUso;
        this.reservaController = reservaController;
    }
    
    public void mostrarMenu() {
        while (true) {
            System.out.println("\n┌───── OPERACIONES DEL SISTEMA ─────┐");
            System.out.println("│ 1. ⏰ Procesar Reservas Expiradas   │");
            System.out.println("│ 0. ⬅️  Volver al Menú Principal     │");
            System.out.println("└───────────────────────────────────────┘");
            System.out.print("Selecciona una opción: ");
            
            int opcion = leerOpcion();
            
            try {
                switch (opcion) {
                    case 1 -> procesarReservasExpiradas();
                    case 0 -> { return; }
                    default -> System.out.println("❌ Opción no válida");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
                pausar();
            }
        }
    }
    
    private void procesarReservasExpiradas() {
        System.out.println("\n═══ PROCESAR RESERVAS EXPIRADAS ═══");
        
        System.out.print("¿Estás seguro de procesar reservas expiradas? (s/N): ");
        String confirmacion = scanner.nextLine().trim().toLowerCase();
        
        if (confirmacion.equals("s") || confirmacion.equals("si")) {
            try {
                List<Reserva> reservasExpiradas = procesarReservasExpiradasCasoUso.ejecutar();
                
                if (reservasExpiradas.isEmpty()) {
                    System.out.println("✅ No hay reservas expiradas para procesar.");
                } else {
                    System.out.println("⏰ Reservas procesadas como expiradas:");
                    System.out.println("─".repeat(60));
                    
                    for (int i = 0; i < reservasExpiradas.size(); i++) {
                        System.out.printf("%d. ", i + 1);
                        reservaController.mostrarReserva(reservasExpiradas.get(i));
                        System.out.println();
                    }
                    System.out.printf("\n📊 Total de reservas expiradas procesadas: %d\n", reservasExpiradas.size());
                }
            } catch (Exception e) {
                System.out.println("❌ Error al procesar reservas expiradas: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Operación cancelada.");
        }
        
        pausar();
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