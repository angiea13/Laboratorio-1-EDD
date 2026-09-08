package core.main;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/** Menú interactivo para administrar el archivo de instructores. */
public final class Main {
    private Main() { }

    public static void main(String[] args) {
        try (Scanner entrada = new Scanner(System.in);
             ArchivoInstructores archivo = new ArchivoInstructores(Path.of("datos", "instructores.dat"));
             ArchivoSesiones sesiones = new ArchivoSesiones(Path.of("datos", "sesiones.dat"));
             ArchivoAprendices aprendices = new ArchivoAprendices(Path.of("datos", "aprendices.dat"))) {
            ejecutarMenu(entrada, new InstructorServicio(archivo), new ReporteServicio(archivo, sesiones),
                    new AprendizServicio(aprendices));
        } catch (IOException error) {
            System.err.println("No fue posible abrir el archivo de instructores: " + error.getMessage());
        }
    }

    private static void ejecutarMenu(Scanner entrada, InstructorServicio servicio, ReporteServicio reportes,
                                     AprendizServicio aprendices) {
        String opcion;
        do {
            System.out.println("\n--- Gestión de instructores ---");
            System.out.println("1. Registrar  2. Consultar  3. Modificar  4. Eliminar  5. Listar");
            System.out.println("6. Instructores disponibles  7. Sesiones programadas");
            System.out.println("8. Reiniciar sesiones mensuales de aprendices  0. Salir");
            System.out.print("Opción: ");
            opcion = entrada.nextLine().trim();
            try {
                procesar(opcion, entrada, servicio, reportes, aprendices);
            } catch (ValidacionException error) {
                System.out.println("Aviso: " + error.getMessage());
            } catch (IOException error) {
                System.out.println("Error al acceder al archivo: " + error.getMessage());
            }
        } while (!"0".equals(opcion));
    }

    private static void procesar(String opcion, Scanner entrada, InstructorServicio servicio,
                                 ReporteServicio reportes, AprendizServicio aprendices)
            throws IOException, ValidacionException {
        switch (opcion) {
            case "1" -> {
                servicio.crear(leerInstructor(entrada));
                System.out.println("Instructor registrado correctamente.");
            }
            case "2" -> {
                System.out.print("Cédula: ");
                servicio.consultar(entrada.nextLine()).ifPresentOrElse(
                        Main::mostrar, () -> System.out.println("Aviso: el instructor no existe."));
            }
            case "3" -> {
                System.out.println("Digite la cédula existente y los datos nuevos:");
                servicio.modificar(leerInstructor(entrada));
                System.out.println("Instructor modificado correctamente.");
            }
            case "4" -> {
                System.out.print("Cédula: ");
                servicio.eliminar(entrada.nextLine());
                System.out.println("Instructor eliminado correctamente.");
            }
            case "5" -> {
                var instructores = servicio.listar();
                if (instructores.isEmpty()) System.out.println("No hay instructores registrados.");
                else instructores.forEach(Main::mostrar);
            }
            case "6" -> mostrarInstructoresDisponibles(entrada, reportes);
            case "7" -> mostrarSesionesProgramadas(entrada, reportes);
            case "8" -> {
                int cantidad = aprendices.reiniciarContadoresMensuales();
                System.out.println("Reinicio completado. Contadores reiniciados: " + cantidad + ".");
            }
            case "0" -> System.out.println("Hasta luego.");
            default -> System.out.println("Aviso: opción no válida.");
        }
    }

    private static void mostrarInstructoresDisponibles(Scanner entrada, ReporteServicio reportes)
            throws IOException, ValidacionException {
        System.out.print("Especialidad: ");
        var resultado = reportes.instructoresDisponibles(entrada.nextLine());
        if (resultado.isEmpty()) System.out.println("No hay instructores disponibles para esa especialidad.");
        else resultado.forEach(Main::mostrar);
    }

    private static void mostrarSesionesProgramadas(Scanner entrada, ReporteServicio reportes)
            throws IOException, ValidacionException {
        System.out.print("Filtrar por 1. Fecha  2. Instructor: ");
        String filtro = entrada.nextLine().trim();
        java.util.List<Sesion> resultado;
        if ("1".equals(filtro)) {
            System.out.print("Fecha (AAAA-MM-DD): ");
            try {
                resultado = reportes.sesionesPorFecha(LocalDate.parse(entrada.nextLine().trim()));
            } catch (DateTimeParseException error) {
                throw new ValidacionException("La fecha debe tener el formato AAAA-MM-DD.");
            }
        } else if ("2".equals(filtro)) {
            System.out.print("Cédula del instructor: ");
            resultado = reportes.sesionesPorInstructor(entrada.nextLine());
        } else {
            throw new ValidacionException("El filtro seleccionado no es válido.");
        }
        if (resultado.isEmpty()) System.out.println("No hay sesiones programadas para el filtro indicado.");
        else resultado.forEach(Main::mostrarSesion);
    }

    private static void mostrarSesion(Sesion s) {
        System.out.printf("%s | %s | %s | Instructor: %s (%s) | Aprendiz: %s (%s)%n",
                s.fecha(), s.codigo(), s.especialidad(), s.nombreInstructor(), s.cedulaInstructor(),
                s.nombreAprendiz(), s.cedulaAprendiz());
    }

    private static Instructor leerInstructor(Scanner entrada) {
        System.out.print("Nombre: "); String nombre = entrada.nextLine();
        System.out.print("Cédula: "); String cedula = entrada.nextLine();
        System.out.print("Especialidad: "); String especialidad = entrada.nextLine();
        System.out.print("Teléfono: "); String telefono = entrada.nextLine();
        System.out.print("Sesiones realizadas (0-15): "); String sesiones = entrada.nextLine().trim();
        int cantidad;
        try { cantidad = Integer.parseInt(sesiones); }
        catch (NumberFormatException error) { cantidad = -1; }
        return new Instructor(nombre, cedula, especialidad, telefono, cantidad);
    }

    private static void mostrar(Instructor i) {
        System.out.printf("%s | C.C. %s | %s | Tel. %s | Sesiones: %d%n",
                i.nombre(), i.cedula(), i.especialidad(), i.telefono(), i.sesionesRealizadas());
    }
}
