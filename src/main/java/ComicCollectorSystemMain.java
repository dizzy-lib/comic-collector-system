import application.usecases.*;
import domain.services.*;
import infrastructure.repository.*;
import interfaces.domain.*;
import interfaces.repository.*;
import presentation.controller.ComicCollectorMainController;

/**
 * Clase principal ejecutable del Comic Collector System.
 * 
 * PRINCIPIOS DE CLEAN ARCHITECTURE:
 * - Composition Root: punto único de configuración de dependencias
 * - Dependency Injection manual: inyección de dependencias sin framework
 * - Separación de capas: infraestructura → dominio → aplicación → presentación
 * - Inversión de dependencias: las capas externas dependen de las internas
 */
public class ComicCollectorSystemMain {
    
    public static void main(String[] args) {
        try {
            // Mensaje de inicio
            System.out.println("🚀 Iniciando Comic Collector System...");
            
            // Configurar e iniciar la aplicación
            ComicCollectorSystemMain app = new ComicCollectorSystemMain();
            app.iniciarAplicacion();
            
        } catch (Exception e) {
            System.err.println("❌ Error crítico al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Configura todas las dependencias e inicia la aplicación.
     * Sigue el patrón Composition Root para centralizar la configuración.
     */
    private void iniciarAplicacion() {
        
        // ═══════════════════════════════════════════════════════════════
        //                   CAPA DE INFRAESTRUCTURA
        // ═══════════════════════════════════════════════════════════════
        
        System.out.println("📊 Inicializando repositorios...");
        
        // Repositorios (implementaciones concretas)
        IUsuarioRepository usuarioRepository = new UsuarioRepository();
        IComicRepository comicRepository = new ComicRepository();
        IReservaRepository reservaRepository = new ReservaRepository();
        IVentaRepository ventaRepository = new VentaRepository();
        
        // ═══════════════════════════════════════════════════════════════
        //                      CAPA DE DOMINIO
        // ═══════════════════════════════════════════════════════════════
        
        System.out.println("⚙️ Inicializando servicios de dominio...");
        
        // Servicios de dominio (lógica de negocio)
        IUsuarioService usuarioService = new UsuarioService(
            usuarioRepository, 
            reservaRepository, 
            ventaRepository
        );
        
        IComicService comicService = new ComicService(
            comicRepository, 
            reservaRepository, 
            ventaRepository
        );
        
        IReservaService reservaService = new ReservaService(
            reservaRepository
        );
        
        IVentaService ventaService = new VentaService(ventaRepository, reservaRepository, comicRepository);
        
        IInventarioService inventarioService = new InventarioService(
            comicRepository, 
            reservaRepository, 
            ventaRepository
        );
        
        // ═══════════════════════════════════════════════════════════════
        //                    CAPA DE APLICACIÓN
        // ═══════════════════════════════════════════════════════════════
        
        System.out.println("📋 Inicializando casos de uso...");
        
        // Casos de uso de Usuarios
        RegistrarUsuarioCasoUso registrarUsuarioCasoUso = new RegistrarUsuarioCasoUso(usuarioService);
        BuscarUsuariosCasoUso buscarUsuariosCasoUso = new BuscarUsuariosCasoUso(usuarioService);
        ActualizarUsuarioCasoUso actualizarUsuarioCasoUso = new ActualizarUsuarioCasoUso(usuarioService);
        EliminarUsuarioCasoUso eliminarUsuarioCasoUso = new EliminarUsuarioCasoUso(usuarioService);
        
        // Casos de uso de Cómics
        AgregarLibroCasoUso agregarLibroCasoUso = new AgregarLibroCasoUso(comicService);
        BuscarComicsCasoUso buscarComicsCasoUso = new BuscarComicsCasoUso(comicService);
        ActualizarComicCasoUso actualizarComicCasoUso = new ActualizarComicCasoUso(comicService);
        EliminarLibroCasoUso eliminarLibroCasoUso = new EliminarLibroCasoUso(comicService);
        ConsultarCatalogoCasoUso consultarCatalogoCasoUso = new ConsultarCatalogoCasoUso(comicService);
        ConsultarDisponibilidadComicCasoUso consultarDisponibilidadComicCasoUso = new ConsultarDisponibilidadComicCasoUso(inventarioService);
        
        // Casos de uso de Reservas
        ReservarLibroCasoUso reservarLibroCasoUso = new ReservarLibroCasoUso(reservaService);
        CancelarReservaCasoUso cancelarReservaCasoUso = new CancelarReservaCasoUso(reservaService);
        ConsultarReservasUsuarioCasoUso consultarReservasUsuarioCasoUso = new ConsultarReservasUsuarioCasoUso(reservaService);
        ProcesarReservasExpiradasCasoUso procesarReservasExpiradasCasoUso = new ProcesarReservasExpiradasCasoUso(reservaService);
        
        // Casos de uso de Ventas
        ComprarLibroCasoUso comprarLibroCasoUso = new ComprarLibroCasoUso(ventaService);
        
        // Casos de uso de Inventario y Reportes
        GenerarReporteInventarioCasoUso generarReporteInventarioCasoUso = new GenerarReporteInventarioCasoUso(inventarioService);
        GenerarReporteComicsPopularesCasoUso generarReporteComicsPopularesCasoUso = new GenerarReporteComicsPopularesCasoUso(inventarioService);
        GenerarReporteComicsMasReservadosCasoUso generarReporteComicsMasReservadosCasoUso = new GenerarReporteComicsMasReservadosCasoUso(inventarioService);
        ConsultarComicsReservadosCasoUso consultarComicsReservadosCasoUso = new ConsultarComicsReservadosCasoUso(inventarioService);
        ConsultarComicsSinActividadCasoUso consultarComicsSinActividadCasoUso = new ConsultarComicsSinActividadCasoUso(inventarioService);
        
        // ═══════════════════════════════════════════════════════════════
        //                   CAPA DE PRESENTACIÓN
        // ═══════════════════════════════════════════════════════════════
        
        System.out.println("🖥️ Inicializando controladores...");
        
        // Controller principal (orquestador)
        ComicCollectorMainController mainController = new ComicCollectorMainController(
            // Usuarios
            registrarUsuarioCasoUso,
            buscarUsuariosCasoUso,
            actualizarUsuarioCasoUso,
            eliminarUsuarioCasoUso,
            // Cómics
            agregarLibroCasoUso,
            buscarComicsCasoUso,
            actualizarComicCasoUso,
            eliminarLibroCasoUso,
            consultarCatalogoCasoUso,
            consultarDisponibilidadComicCasoUso,
            // Reservas
            reservarLibroCasoUso,
            cancelarReservaCasoUso,
            consultarReservasUsuarioCasoUso,
            // Ventas
            comprarLibroCasoUso,
            // Inventario y Reportes
            generarReporteInventarioCasoUso,
            generarReporteComicsPopularesCasoUso,
            generarReporteComicsMasReservadosCasoUso,
            consultarComicsReservadosCasoUso,
            consultarComicsSinActividadCasoUso,
            // Sistema
            procesarReservasExpiradasCasoUso
        );
        
        // ═══════════════════════════════════════════════════════════════
        //                      INICIO DE LA APLICACIÓN
        // ═══════════════════════════════════════════════════════════════
        
        System.out.println("✅ Sistema inicializado correctamente\n");
        
        // Iniciar la aplicación
        mainController.iniciar();
        
        // Mensaje de cierre
        System.out.println("\n👋 Comic Collector System finalizado correctamente.");
    }
}