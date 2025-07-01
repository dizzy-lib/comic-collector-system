package presentation.controller;

import application.usecases.*;
import domain.entities.Usuario;

import java.util.List;
import java.util.Scanner;

/**
 * Controller especializado para la gestión de usuarios.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Responsabilidad única: solo maneja operaciones de usuarios
 * - Separación de concerns por contexto de dominio
 * - Delega lógica de negocio a casos de uso específicos
 */
public class UsuarioController {
    
    private final Scanner scanner;
    private final RegistrarUsuarioCasoUso registrarUsuarioCasoUso;
    private final BuscarUsuariosCasoUso buscarUsuariosCasoUso;
    private final ActualizarUsuarioCasoUso actualizarUsuarioCasoUso;
    private final EliminarUsuarioCasoUso eliminarUsuarioCasoUso;
    
    public UsuarioController(Scanner scanner,
                           RegistrarUsuarioCasoUso registrarUsuarioCasoUso,
                           BuscarUsuariosCasoUso buscarUsuariosCasoUso,
                           ActualizarUsuarioCasoUso actualizarUsuarioCasoUso,
                           EliminarUsuarioCasoUso eliminarUsuarioCasoUso) {
        this.scanner = scanner;
        this.registrarUsuarioCasoUso = registrarUsuarioCasoUso;
        this.buscarUsuariosCasoUso = buscarUsuariosCasoUso;
        this.actualizarUsuarioCasoUso = actualizarUsuarioCasoUso;
        this.eliminarUsuarioCasoUso = eliminarUsuarioCasoUso;
    }
    
    public void mostrarMenu() {
        while (true) {
            System.out.println("\n┌─────────── GESTIÓN DE USUARIOS ──────────┐");
            System.out.println("│ 1. ➕ Registrar Usuario                   │");
            System.out.println("│ 2. 🔍 Buscar Usuarios                     │");
            System.out.println("│ 3. ✏️  Actualizar Usuario                  │");
            System.out.println("│ 4. 🗑️  Eliminar Usuario                    │");
            System.out.println("│ 0. ⬅️  Volver al Menú Principal           │");
            System.out.println("└───────────────────────────────────────────┘");
            System.out.print("Selecciona una opción: ");
            
            int opcion = leerOpcion();
            
            try {
                switch (opcion) {
                    case 1 -> registrarUsuario();
                    case 2 -> buscarUsuarios();
                    case 3 -> actualizarUsuario();
                    case 4 -> eliminarUsuario();
                    case 0 -> { return; }
                    default -> System.out.println("❌ Opción no válida");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
                pausar();
            }
        }
    }
    
    private void registrarUsuario() {
        System.out.println("\n═══ REGISTRAR NUEVO USUARIO ═══");
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine().trim();
        
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        
        try {
            Usuario usuario = registrarUsuarioCasoUso.ejecutar(nombre, apellido, email);
            System.out.println("✅ Usuario registrado exitosamente:");
            mostrarUsuario(usuario);
        } catch (Exception e) {
            System.out.println("❌ Error al registrar usuario: " + e.getMessage());
        }
        
        pausar();
    }
    
    private void buscarUsuarios() {
        System.out.println("\n═══ BUSCAR USUARIOS ═══");
        System.out.print("Criterio de búsqueda (nombre, apellido o email): ");
        String criterio = scanner.nextLine().trim();
        
        try {
            List<Usuario> usuarios = buscarUsuariosCasoUso.ejecutar(criterio);
            
            if (usuarios.isEmpty()) {
                System.out.println("❌ No se encontraron usuarios con ese criterio.");
            } else {
                System.out.println("📋 Usuarios encontrados:");
                System.out.println("─".repeat(60));
                for (int i = 0; i < usuarios.size(); i++) {
                    System.out.printf("%d. ", i + 1);
                    mostrarUsuario(usuarios.get(i));
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error al buscar usuarios: " + e.getMessage());
        }
        
        pausar();
    }
    
    private void actualizarUsuario() {
        System.out.println("\n═══ ACTUALIZAR USUARIO ═══");
        
        Usuario usuario = seleccionarUsuario();
        if (usuario == null) return;
        
        System.out.println("Datos actuales:");
        mostrarUsuario(usuario);
        
        System.out.println("\nActualmente solo se puede actualizar el email del usuario.");
        
        System.out.print("Nuevo email [" + usuario.getEmail() + "]: ");
        String nuevoEmail = scanner.nextLine().trim();
        if (nuevoEmail.isEmpty()) nuevoEmail = usuario.getEmail();
        
        try {
            if (!nuevoEmail.equals(usuario.getEmail())) {
                usuario.setEmail(nuevoEmail);
            } else {
                System.out.println("❌ No hay cambios que realizar.");
                pausar();
                return;
            }
            
            Usuario resultado = actualizarUsuarioCasoUso.ejecutar(usuario);
            System.out.println("✅ Usuario actualizado exitosamente:");
            mostrarUsuario(resultado);
        } catch (Exception e) {
            System.out.println("❌ Error al actualizar usuario: " + e.getMessage());
        }
        
        pausar();
    }
    
    private void eliminarUsuario() {
        System.out.println("\n═══ ELIMINAR USUARIO ═══");
        
        Usuario usuario = seleccionarUsuario();
        if (usuario == null) return;
        
        System.out.println("Usuario a eliminar:");
        mostrarUsuario(usuario);
        
        System.out.print("¿Estás seguro de eliminar este usuario? (s/N): ");
        String confirmacion = scanner.nextLine().trim().toLowerCase();
        
        if (confirmacion.equals("s") || confirmacion.equals("si")) {
            try {
                eliminarUsuarioCasoUso.ejecutar(usuario);
                System.out.println("✅ Usuario eliminado exitosamente.");
            } catch (Exception e) {
                System.out.println("❌ Error al eliminar usuario: " + e.getMessage());
            }
        } else {
            System.out.println("❌ Eliminación cancelada.");
        }
        
        pausar();
    }
    
    /**
     * Método público para que otros controllers puedan seleccionar usuarios.
     */
    public Usuario seleccionarUsuario() {
        System.out.print("Ingresa criterio para buscar el usuario: ");
        String criterio = scanner.nextLine().trim();
        
        try {
            List<Usuario> usuarios = buscarUsuariosCasoUso.ejecutar(criterio);
            
            if (usuarios.isEmpty()) {
                System.out.println("❌ No se encontraron usuarios.");
                return null;
            }
            
            if (usuarios.size() == 1) {
                return usuarios.get(0);
            }
            
            System.out.println("Múltiples usuarios encontrados:");
            for (int i = 0; i < usuarios.size(); i++) {
                System.out.printf("%d. ", i + 1);
                mostrarUsuario(usuarios.get(i));
                System.out.println();
            }
            
            System.out.print("Selecciona el número de usuario: ");
            int seleccion = leerOpcion();
            
            if (seleccion > 0 && seleccion <= usuarios.size()) {
                return usuarios.get(seleccion - 1);
            } else {
                System.out.println("❌ Selección no válida.");
                return null;
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error al buscar usuarios: " + e.getMessage());
            return null;
        }
    }
    
    public void mostrarUsuario(Usuario usuario) {
        System.out.printf("ID: %d | %s | %s", 
            usuario.getId(),
            usuario.getNombreCompleto(), 
            usuario.getEmail());
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