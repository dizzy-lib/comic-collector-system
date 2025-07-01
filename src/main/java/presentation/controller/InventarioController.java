package presentation.controller;

import application.usecases.*;
import domain.entities.Comic;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Controller especializado para inventario y reportes.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo maneja consultas de inventario y generación de reportes
 * - Separación de concerns por contexto de dominio
 * - Delega lógica de negocio a casos de uso específicos
 */
public class InventarioController {
    
    private final Scanner scanner;
    private final GenerarReporteInventarioCasoUso generarReporteInventarioCasoUso;
    private final GenerarReporteComicsPopularesCasoUso generarReporteComicsPopularesCasoUso;
    private final GenerarReporteComicsMasReservadosCasoUso generarReporteComicsMasReservadosCasoUso;
    private final ConsultarComicsReservadosCasoUso consultarComicsReservadosCasoUso;
    private final ConsultarComicsSinActividadCasoUso consultarComicsSinActividadCasoUso;
    
    // Referencia para mostrar comics
    private final ComicController comicController;
    
    public InventarioController(Scanner scanner,
                              GenerarReporteInventarioCasoUso generarReporteInventarioCasoUso,
                              GenerarReporteComicsPopularesCasoUso generarReporteComicsPopularesCasoUso,
                              GenerarReporteComicsMasReservadosCasoUso generarReporteComicsMasReservadosCasoUso,
                              ConsultarComicsReservadosCasoUso consultarComicsReservadosCasoUso,
                              ConsultarComicsSinActividadCasoUso consultarComicsSinActividadCasoUso,
                              ComicController comicController) {
        this.scanner = scanner;
        this.generarReporteInventarioCasoUso = generarReporteInventarioCasoUso;
        this.generarReporteComicsPopularesCasoUso = generarReporteComicsPopularesCasoUso;
        this.generarReporteComicsMasReservadosCasoUso = generarReporteComicsMasReservadosCasoUso;
        this.consultarComicsReservadosCasoUso = consultarComicsReservadosCasoUso;
        this.consultarComicsSinActividadCasoUso = consultarComicsSinActividadCasoUso;
        this.comicController = comicController;
    }
    
    public void mostrarMenu() {
        while (true) {
            System.out.println("\n┌────── INVENTARIO Y REPORTES ──────┐");
            System.out.println("│ 1. 📊 Estadísticas del Inventario    │");
            System.out.println("│ 2. 🏆 Cómics Más Vendidos           │");
            System.out.println("│ 3. 📋 Cómics Más Reservados         │");
            System.out.println("│ 4. 📝 Cómics Reservados Actualmente │");
            System.out.println("│ 5. 😴 Cómics Sin Actividad          │");
            System.out.println("│ 0. ⬅️  Volver al Menú Principal      │");
            System.out.println("└───────────────────────────────────────┘");
            System.out.print("Selecciona una opción: ");
            
            int opcion = leerOpcion();
            
            try {
                switch (opcion) {
                    case 1 -> mostrarEstadisticasInventario();
                    case 2 -> mostrarComicsMasVendidos();
                    case 3 -> mostrarComicsMasReservados();
                    case 4 -> mostrarComicsReservados();
                    case 5 -> mostrarComicsSinActividad();
                    case 0 -> { return; }
                    default -> System.out.println("❌ Opción no válida");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
                pausar();
            }
        }
    }
    
    private void mostrarEstadisticasInventario() {
        System.out.println("\n═══ ESTADÍSTICAS DEL INVENTARIO ═══");
        
        try {
            Map<String, Long> estadisticas = generarReporteInventarioCasoUso.ejecutar();
            
            System.out.println("📊 Resumen del Sistema:");
            System.out.println("─".repeat(40));
            
            for (Map.Entry<String, Long> entry : estadisticas.entrySet()) {
                System.out.printf("%-25s: %d\n", entry.getKey(), entry.getValue());
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al generar estadísticas: " + e.getMessage());
        }
        
        pausar();
    }
    
    private void mostrarComicsMasVendidos() {
        System.out.println("\n═══ CÓMICS MÁS VENDIDOS ═══");
        System.out.print("¿Cuántos cómics mostrar? (por defecto 10): ");
        String limitStr = scanner.nextLine().trim();
        
        int limite = obtenerLimite(limitStr, 10);
        
        try {
            Map<Comic, Long> comicsVendidos = generarReporteComicsPopularesCasoUso.ejecutar(limite);
            
            if (comicsVendidos.isEmpty()) {
                System.out.println("❌ No hay ventas registradas aún.");
            } else {
                System.out.println("🏆 Top " + limite + " Cómics Más Vendidos:");
                System.out.println("─".repeat(60));
                
                int posicion = 1;
                for (Map.Entry<Comic, Long> entry : comicsVendidos.entrySet()) {
                    System.out.printf("%d. %s - %d ventas\n", 
                        posicion++, 
                        entry.getKey().getNombre(), 
                        entry.getValue());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error al generar reporte: " + e.getMessage());
        }
        
        pausar();
    }
    
    private void mostrarComicsMasReservados() {
        System.out.println("\n═══ CÓMICS MÁS RESERVADOS ═══");
        System.out.print("¿Cuántos cómics mostrar? (por defecto 10): ");
        String limitStr = scanner.nextLine().trim();
        
        int limite = obtenerLimite(limitStr, 10);
        
        try {
            Map<Comic, Long> comicsReservados = generarReporteComicsMasReservadosCasoUso.ejecutar(limite);
            
            if (comicsReservados.isEmpty()) {
                System.out.println("❌ No hay reservas registradas aún.");
            } else {
                System.out.println("📋 Top " + limite + " Cómics Más Reservados:");
                System.out.println("─".repeat(60));
                
                int posicion = 1;
                for (Map.Entry<Comic, Long> entry : comicsReservados.entrySet()) {
                    System.out.printf("%d. %s - %d reservas\n", 
                        posicion++, 
                        entry.getKey().getNombre(), 
                        entry.getValue());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error al generar reporte: " + e.getMessage());
        }
        
        pausar();
    }
    
    private void mostrarComicsReservados() {
        System.out.println("\n═══ CÓMICS RESERVADOS ACTUALMENTE ═══");
        
        try {
            List<Comic> comicsReservados = consultarComicsReservadosCasoUso.ejecutar();
            
            if (comicsReservados.isEmpty()) {
                System.out.println("❌ No hay cómics reservados actualmente.");
            } else {
                System.out.println("📝 Cómics con reservas activas:");
                System.out.println("─".repeat(60));
                
                for (int i = 0; i < comicsReservados.size(); i++) {
                    System.out.printf("%d. ", i + 1);
                    comicController.mostrarComic(comicsReservados.get(i));
                    System.out.println();
                }
                System.out.printf("\n📊 Total de cómics reservados: %d\n", comicsReservados.size());
            }
        } catch (Exception e) {
            System.out.println("❌ Error al consultar cómics reservados: " + e.getMessage());
        }
        
        pausar();
    }
    
    private void mostrarComicsSinActividad() {
        System.out.println("\n═══ CÓMICS SIN ACTIVIDAD ═══");
        
        try {
            List<Comic> comicsSinActividad = consultarComicsSinActividadCasoUso.ejecutar();
            
            if (comicsSinActividad.isEmpty()) {
                System.out.println("✅ Todos los cómics tienen actividad (ventas o reservas).");
            } else {
                System.out.println("😴 Cómics sin ventas ni reservas:");
                System.out.println("─".repeat(60));
                
                for (int i = 0; i < comicsSinActividad.size(); i++) {
                    System.out.printf("%d. ", i + 1);
                    comicController.mostrarComic(comicsSinActividad.get(i));
                    System.out.println();
                }
                System.out.printf("\n📊 Total de cómics sin actividad: %d\n", comicsSinActividad.size());
            }
        } catch (Exception e) {
            System.out.println("❌ Error al consultar cómics sin actividad: " + e.getMessage());
        }
        
        pausar();
    }
    
    private int obtenerLimite(String limitStr, int porDefecto) {
        if (limitStr.isEmpty()) {
            return porDefecto;
        }
        
        try {
            int limite = Integer.parseInt(limitStr);
            return limite <= 0 ? porDefecto : limite;
        } catch (NumberFormatException e) {
            System.out.println("❌ Número inválido, usando " + porDefecto + " por defecto.");
            return porDefecto;
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