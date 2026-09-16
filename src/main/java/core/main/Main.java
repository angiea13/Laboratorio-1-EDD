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
        if (args.length > 0 && "--gui".equals(args[0])) {
            MenuGrafico.main(java.util.Arrays.copyOfRange(args, 1, args.length));
            return;
        }
        Path carpeta = Path.of("datos");
        if (args.length > 0) carpeta = Path.of(args[0]);
        try (Scanner entrada = new Scanner(System.in);
             ArchivoSesiones sesiones = new ArchivoSesiones(carpeta.resolve("sesiones.dat"));
             ArchivoAprendices aprendices = new ArchivoAprendices(carpeta.resolve("aprendices.dat"))) {
            ArchivoInstructoresIndexado archivo = ArchivoInstructoresIndexado.abrirParaMenu(carpeta);
            ejecutarMenu(entrada, archivo, new ReporteServicio(archivo, sesiones),
                    new AprendizServicio(aprendices), new SesionServicio(sesiones, aprendices, archivo), sesiones);
        } catch (IOException error) {
            System.err.println("No fue posible abrir los archivos de la academia: " + error.getMessage());
        }
    }

    private static void ejecutarMenu(Scanner entrada, ArchivoInstructoresIndexado servicio, ReporteServicio reportes,
                                     AprendizServicio aprendices, SesionServicio asignaciones, ArchivoSesiones sesiones) {
        String opcion;
        do {
            System.out.println("\n--- Gestión de instructores ---");
            System.out.println("1. Registrar  2. Consultar  3. Modificar  4. Eliminar  5. Listar");
            System.out.println("6. Instructores disponibles  7. Sesiones programadas");
            System.out.println("8. Reiniciar sesiones mensuales de aprendices  0. Salir");
            System.out.println("9. Registrar aprendiz  10. Consultar aprendiz  11. Modificar aprendiz");
            System.out.println("12. Eliminar aprendiz  13. Listar aprendices");
            System.out.println("14. Asignar sesión  15. Cancelar sesión  16. Reiniciar instructores  17. Consultar sesión por código");
            System.out.print("Opción: ");
            if (!entrada.hasNextLine()) return;
            opcion = entrada.nextLine().trim();
            try {
                procesar(opcion, entrada, servicio, reportes, aprendices, asignaciones, sesiones);
            } catch (ValidacionException error) {
                System.out.println("Aviso: " + error.getMessage());
            } catch (IOException error) {
                System.out.println("Error al acceder al archivo: " + error.getMessage());
            } catch (java.util.NoSuchElementException error) {
                System.out.println("Entrada finalizada. No se guardó el formulario incompleto.");
                return;
            }
        } while (!"0".equals(opcion));
    }

    private static void procesar(String opcion, Scanner entrada, ArchivoInstructoresIndexado servicio,
                                 ReporteServicio reportes, AprendizServicio aprendices,
                                 SesionServicio asignaciones, ArchivoSesiones sesiones)
            throws IOException, ValidacionException {
        switch (opcion) {
            case "14" -> {
                System.out.print("Código único: "); String codigo = entrada.nextLine();
                System.out.print("Cédula del aprendiz: "); String cedula = entrada.nextLine();
                Aprendiz aprendiz = aprendices.consultar(cedula);
                if (aprendiz == null) throw new ValidacionException("El aprendiz no está registrado.");
                System.out.println("Aprendiz: " + aprendiz.nombre());
                System.out.print("Especialidad: "); String especialidad = entrada.nextLine();
                System.out.print("Fecha (AAAA-MM-DD): ");
                LocalDate fecha;
                try { fecha = LocalDate.parse(entrada.nextLine().trim()); }
                catch (DateTimeParseException e) { throw new ValidacionException("Fecha inválida. Use AAAA-MM-DD."); }
                var disponibles = asignaciones.disponibles(especialidad, fecha);
                if (disponibles.isEmpty()) throw new ValidacionException("No hay instructores disponibles.");
                for (Instructor instructor : disponibles) mostrar(instructor);
                System.out.print("Cédula del instructor elegido: ");
                mostrarSesion(asignaciones.asignar(codigo, cedula, especialidad, entrada.nextLine(), fecha));
            }
            case "15" -> {
                System.out.print("Código de sesión: ");
                asignaciones.cancelar(entrada.nextLine());
                System.out.println("Sesión cancelada.");
            }
            case "16" -> System.out.println("Instructores reiniciados: " + servicio.reiniciarContadoresMensuales());
            case "17" -> {
                System.out.print("Código de sesión: ");
                Sesion sesion = sesiones.buscar(entrada.nextLine());
                if (sesion == null) throw new ValidacionException("La sesión no existe.");
                mostrarSesion(sesion);
            }
            case "1" -> {
                guardarInstructor(entrada, servicio, false);
            }
            case "2" -> {
                System.out.print("Cédula: ");
                String[] registro = servicio.consultar(ArchivoInstructoresIndexado.leerCedula(entrada.nextLine()));
                if (registro != null) mostrar(new Instructor(registro[1], registro[0], registro[2],
                        registro[3], Integer.parseInt(registro[4])));
            }
            case "3" -> {
                System.out.println("Digite la cédula existente y los datos nuevos:");
                guardarInstructor(entrada, servicio, true);
            }
            case "4" -> {
                System.out.print("Cédula: ");
                servicio.eliminar(ArchivoInstructoresIndexado.leerCedula(entrada.nextLine()));
            }
            case "5" -> {
                Instructor[] instructores = servicio.listar();
                if (instructores.length == 0) System.out.println("No hay instructores registrados.");
                else for (Instructor instructor : instructores) mostrar(instructor);
            }
            case "6" -> mostrarInstructoresDisponibles(entrada, reportes);
            case "7" -> mostrarSesionesProgramadas(entrada, reportes);
            case "8" -> {
                int cantidad = aprendices.reiniciarContadoresMensuales();
                System.out.println("Reinicio completado. Contadores reiniciados: " + cantidad + ".");
            }
            case "0" -> System.out.println("Hasta luego.");
            case "9" -> {
                aprendices.crear(leerAprendiz(entrada));
                System.out.println("Aprendiz registrado correctamente.");
            }
            case "10" -> {
                System.out.print("Cédula del aprendiz: ");
                Aprendiz aprendiz = aprendices.consultar(entrada.nextLine());
                if (aprendiz == null) System.out.println("No existe un aprendiz con esa cédula.");
                else mostrarAprendiz(aprendiz);
            }
            case "11" -> {
                System.out.println("Digite la cédula existente, los datos nuevos y todas sus especialidades:");
                aprendices.modificar(leerAprendiz(entrada));
                System.out.println("Aprendiz modificado correctamente.");
            }
            case "12" -> {
                System.out.print("Cédula del aprendiz: ");
                aprendices.eliminar(entrada.nextLine());
                System.out.println("Aprendiz eliminado correctamente.");
            }
            case "13" -> {
                java.util.List<Aprendiz> lista = aprendices.listar();
                if (lista.isEmpty()) System.out.println("No hay aprendices registrados.");
                for (Aprendiz aprendiz : lista) mostrarAprendiz(aprendiz);
            }
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

    private static void guardarInstructor(Scanner entrada, ArchivoInstructoresIndexado archivo, boolean modificar)
            throws IOException, ValidacionException {
        Instructor instructor = leerInstructor(entrada);
        // Validar antes de convertir a short evita que un número grande se desborde.
        if (instructor.sesionesRealizadas() < 0 || instructor.sesionesRealizadas() > 15)
            throw new ValidacionException("Las sesiones deben estar entre 0 y 15.");
        archivo.guardar(instructor.cedula(), instructor.nombre(), instructor.especialidad(),
                instructor.telefono(), (short) instructor.sesionesRealizadas(), modificar);
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

    private static Aprendiz leerAprendiz(Scanner entrada) throws ValidacionException {
        System.out.print("Nombre: "); String nombre = entrada.nextLine();
        System.out.print("Cédula: "); String cedula = entrada.nextLine();
        System.out.print("Teléfono: "); String telefono = entrada.nextLine();
        System.out.print("Cantidad de especialidades: ");
        int cantidad;
        try { cantidad = Integer.parseInt(entrada.nextLine().trim()); }
        catch (NumberFormatException e) { throw new ValidacionException("La cantidad de especialidades debe ser un entero."); }
        if (cantidad < 1 || cantidad > 10) throw new ValidacionException("La cantidad de especialidades debe estar entre 1 y 10.");
        java.util.Map<String, Integer> especialidades = new java.util.LinkedHashMap<>();
        System.out.println("Catálogo: Música, Pintura, Escritura, Baile.");
        for (int i = 0; i < cantidad; i++) {
            System.out.print("Especialidad " + (i + 1) + ": ");
            String especialidad = CatalogoEspecialidades.validar(entrada.nextLine());
            if (especialidades.containsKey(especialidad)) throw new ValidacionException("La especialidad está repetida.");
            System.out.print("Sesiones de " + especialidad + " (0-4): ");
            int sesiones;
            try { sesiones = Integer.parseInt(entrada.nextLine().trim()); }
            catch (NumberFormatException e) { throw new ValidacionException("Las sesiones deben ser un entero."); }
            especialidades.put(especialidad, sesiones);
        }
        return new Aprendiz(nombre, cedula, telefono, especialidades);
    }

    private static void mostrarAprendiz(Aprendiz aprendiz) {
        System.out.println(aprendiz.nombre() + " | C.C. " + aprendiz.cedula() + " | Tel. " + aprendiz.telefono());
        for (java.util.Map.Entry<String, Integer> especialidad : aprendiz.sesionesPorEspecialidad().entrySet())
            System.out.println("  " + especialidad.getKey() + ": " + especialidad.getValue() + " sesiones");
    }

    private static void mostrar(Instructor i) {
        System.out.printf("%s | C.C. %s | %s | Tel. %s | Sesiones: %d%n",
                i.nombre(), i.cedula(), i.especialidad(), i.telefono(), i.sesionesRealizadas());
    }
}
