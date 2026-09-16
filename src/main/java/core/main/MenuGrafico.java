package core.main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import javax.swing.*;

/** Menú de escritorio con Swing. Los datos siguen guardados en los archivos indexados. */
public final class MenuGrafico extends JFrame {
    private final ArchivoAprendices aprendices;
    private final ArchivoSesiones sesiones;
    private final ArchivoInstructoresIndexado instructores;
    private final SesionServicio servicio;
    private final JTextArea salida = new JTextArea(18, 90);

    MenuGrafico(Path carpeta) throws IOException {
        super("Academia de Artes Barranquilla");
        instructores = ArchivoInstructoresIndexado.abrirParaMenu(carpeta);
        aprendices = new ArchivoAprendices(carpeta.resolve("aprendices.dat"));
        try { sesiones = new ArchivoSesiones(carpeta.resolve("sesiones.dat")); }
        catch (IOException error) { aprendices.close(); throw error; }
        servicio = new SesionServicio(sesiones, aprendices, instructores);
        JPanel menu = new JPanel(new GridLayout(0, 3, 8, 8));
        String[] opciones = {"Registrar instructor", "Consultar instructor", "Modificar instructor",
                "Eliminar instructor", "Listar instructores", "Instructores disponibles",
                "Registrar aprendiz", "Consultar aprendiz", "Modificar aprendiz", "Eliminar aprendiz",
                "Listar aprendices", "Asignar sesión", "Consultar sesión por código", "Sesiones por fecha",
                "Sesiones por instructor", "Cancelar sesión", "Reiniciar instructores", "Reiniciar aprendices"};
        for (String opcion : opciones) {
            JButton boton = new JButton(opcion);
            boton.addActionListener(evento -> ejecutar(opcion));
            menu.add(boton);
        }
        salida.setEditable(false);
        salida.setLineWrap(true);
        salida.setWrapStyleWord(true);
        JPanel contenido = new JPanel(new BorderLayout(10, 10));
        contenido.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contenido.add(new JLabel("Administración de instructores, aprendices y sesiones"), BorderLayout.NORTH);
        contenido.add(menu, BorderLayout.CENTER);
        contenido.add(new JScrollPane(salida), BorderLayout.SOUTH);
        setContentPane(contenido);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosed(WindowEvent evento) {
                try { sesiones.close(); }
                catch (IOException error) { aviso(error.getMessage()); }
                try { aprendices.close(); }
                catch (IOException error) { aviso(error.getMessage()); }
            }
        });
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        Path carpeta = Path.of(args.length == 0 ? "datos" : args[0]);
        SwingUtilities.invokeLater(() -> {
            try { new MenuGrafico(carpeta).setVisible(true); }
            catch (IOException error) {
                JOptionPane.showMessageDialog(null, "No fue posible abrir los archivos: " + error.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Cancelar cualquier diálogo abandona el formulario antes de guardar.
    private String pedir(String campo) {
        String valor = JOptionPane.showInputDialog(this, campo);
        if (valor == null) throw new FormularioCancelado();
        return valor.trim();
    }

    private String especialidad() {
        Object valor = JOptionPane.showInputDialog(this, "Especialidad", "Especialidades",
                JOptionPane.QUESTION_MESSAGE, null, new String[]{"Música", "Pintura", "Escritura", "Baile"}, "Música");
        if (valor == null) throw new FormularioCancelado();
        return valor.toString();
    }

    private LocalDate fecha() { return LocalDate.parse(pedir("Fecha (AAAA-MM-DD)")); }
    private boolean confirmar(String texto) {
        return JOptionPane.showConfirmDialog(this, texto, "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    private void aviso(String texto) { JOptionPane.showMessageDialog(this, texto, "Aviso", JOptionPane.WARNING_MESSAGE); }

    private void ejecutar(String opcion) {
        try {
            switch (opcion) {
                case "Registrar instructor", "Modificar instructor" -> guardarInstructor(opcion.startsWith("Modificar"));
                case "Consultar instructor" -> {
                    String[] r = instructores.consultar(ArchivoInstructoresIndexado.leerCedula(pedir("Cédula")));
                    if (r == null) throw new ValidacionException("El instructor no existe.");
                    salida.setText("Cédula | Nombre | Especialidad | Teléfono | Sesiones\n" + String.join(" | ", r));
                }
                case "Eliminar instructor" -> {
                    int id = ArchivoInstructoresIndexado.leerCedula(pedir("Cédula"));
                    for (Sesion s : sesiones.listar())
                        if (s.cedulaInstructor().equals("" + id) && !s.fecha().isBefore(LocalDate.now()))
                            throw new ValidacionException("El instructor tiene sesiones pendientes. Cancele primero las futuras.");
                    if (confirmar("¿Eliminar este instructor?")) {
                        if (!instructores.eliminar(id)) throw new ValidacionException("No se pudo eliminar: verifique que exista.");
                        salida.setText("Instructor eliminado.");
                    }
                }
                case "Listar instructores" -> mostrar(java.util.Arrays.asList(instructores.listar()));
                case "Instructores disponibles" -> mostrar(servicio.disponibles(especialidad(), fecha()));
                case "Registrar aprendiz", "Modificar aprendiz" -> guardarAprendiz(opcion.startsWith("Modificar"));
                case "Consultar aprendiz" -> {
                    Aprendiz a = aprendices.buscar(pedir("Cédula del aprendiz"));
                    if (a == null) throw new ValidacionException("El aprendiz no existe.");
                    mostrar(java.util.List.of(a));
                }
                case "Eliminar aprendiz" -> {
                    String id = InstructorValidador.validarCedula(pedir("Cédula del aprendiz"));
                    for (Sesion s : sesiones.listar())
                        if (s.cedulaAprendiz().equals(id) && !s.fecha().isBefore(LocalDate.now()))
                            throw new ValidacionException("El aprendiz tiene sesiones pendientes. Cancele primero las futuras.");
                    if (confirmar("¿Eliminar este aprendiz?")) {
                        aprendices.eliminar(id); salida.setText("Aprendiz eliminado.");
                    }
                }
                case "Listar aprendices" -> mostrar(aprendices.listar());
                case "Asignar sesión" -> asignar();
                case "Consultar sesión por código" -> {
                    Sesion s = sesiones.buscar(ValidacionDatos.texto(pedir("Código"), "código", 30));
                    if (s == null) throw new ValidacionException("La sesión no existe.");
                    mostrar(java.util.List.of(s));
                }
                case "Sesiones por fecha" -> mostrar(sesiones.buscarPorFecha(fecha()));
                case "Sesiones por instructor" -> mostrar(sesiones.buscarPorInstructor("" + ArchivoInstructoresIndexado.leerCedula(pedir("Cédula del instructor"))));
                case "Cancelar sesión" -> {
                    String codigo = pedir("Código de la sesión");
                    if (confirmar("¿Cancelar la sesión " + codigo + "?")) {
                        servicio.cancelar(codigo); salida.setText("Sesión cancelada; cupos liberados.");
                    }
                }
                case "Reiniciar instructores" -> {
                    if (confirmar("¿Colocar en cero los contadores de todos los instructores?"))
                        salida.setText("Instructores reiniciados: " + instructores.reiniciarContadoresMensuales());
                }
                case "Reiniciar aprendices" -> {
                    if (confirmar("¿Colocar en cero los contadores de todos los aprendices?"))
                        salida.setText("Contadores reiniciados: " + aprendices.reiniciarContadoresPorEspecialidad());
                }
                default -> throw new IllegalArgumentException("Operación desconocida.");
            }
        } catch (FormularioCancelado error) {
            salida.setText("Operación cancelada.");
        } catch (java.time.format.DateTimeParseException error) {
            aviso("Fecha inválida. Use AAAA-MM-DD y una fecha existente.");
        } catch (NumberFormatException error) {
            aviso("Ingrese un número entero válido.");
        } catch (ValidacionException | IOException error) {
            aviso(error.getMessage());
        }
    }

    private void guardarInstructor(boolean modificar) throws IOException, ValidacionException {
        String cedula = pedir("Cédula"), nombre = pedir("Nombre"), especialidad = especialidad(), telefono = pedir("Teléfono");
        int cantidad = Integer.parseInt(pedir("Sesiones del mes actual (0-15)"));
        Instructor i = InstructorValidador.validarIndexado(new Instructor(nombre, cedula, especialidad, telefono, cantidad));
        if (!instructores.guardar(i.cedula(), i.nombre(), i.especialidad(), i.telefono(), (short) cantidad, modificar))
            throw new ValidacionException("No se pudo guardar. Para registrar, la cédula debe ser nueva; para modificar, debe existir.");
        salida.setText("Instructor guardado.");
    }

    private void guardarAprendiz(boolean modificar) throws IOException, ValidacionException {
        String cedula = pedir("Cédula"), nombre = pedir("Nombre"), telefono = pedir("Teléfono");
        int cantidad = Integer.parseInt(pedir("Cantidad de especialidades (1-4)"));
        if (cantidad < 1 || cantidad > 4) throw new ValidacionException("Ingrese de 1 a 4 especialidades.");
        LinkedHashMap<String, Integer> contadores = new LinkedHashMap<>();
        for (int n = 0; n < cantidad; n++) {
            String especialidad = especialidad();
            if (contadores.containsKey(especialidad)) throw new ValidacionException("Especialidad repetida.");
            contadores.put(especialidad, Integer.parseInt(pedir("Sesiones del mes actual en " + especialidad + " (0-4)")));
        }
        Aprendiz a = new Aprendiz(nombre, cedula, telefono, contadores);
        if (modificar) aprendices.actualizar(a); else aprendices.guardar(a);
        salida.setText("Aprendiz guardado.");
    }

    private void asignar() throws IOException, ValidacionException {
        String codigo = pedir("Código único de la sesión"), cedula = pedir("Cédula del aprendiz");
        Aprendiz aprendiz = aprendices.buscar(cedula);
        if (aprendiz == null) throw new ValidacionException("El aprendiz no está registrado.");
        String especialidad = especialidad();
        LocalDate fecha = fecha();
        java.util.List<Instructor> disponibles = servicio.disponibles(especialidad, fecha);
        if (disponibles.isEmpty()) throw new ValidacionException("No hay instructores disponibles para esa especialidad y fecha.");
        String[] opciones = new String[disponibles.size()];
        for (int n = 0; n < opciones.length; n++) opciones[n] = disponibles.get(n).cedula() + " - " + disponibles.get(n).nombre();
        Object seleccion = JOptionPane.showInputDialog(this, "Aprendiz: " + aprendiz.nombre() + "\nSeleccione instructor",
                "Asignar sesión", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (seleccion == null) throw new FormularioCancelado();
        String instructor = seleccion.toString().split(" - ")[0];
        mostrar(java.util.List.of(servicio.asignar(codigo, cedula, especialidad, instructor, fecha)));
    }

    private void mostrar(java.util.List<?> registros) {
        StringBuilder texto = new StringBuilder();
        for (Object registro : registros) {
            if (registro instanceof Instructor i)
                texto.append(i.nombre()).append(" | C.C. ").append(i.cedula()).append(" | ").append(i.especialidad())
                        .append(" | Tel. ").append(i.telefono()).append(" | Sesiones: ").append(i.sesionesRealizadas());
            else if (registro instanceof Aprendiz a)
                texto.append(a.nombre()).append(" | C.C. ").append(a.cedula()).append(" | Tel. ").append(a.telefono())
                        .append(" | Sesiones por especialidad: ").append(a.sesionesPorEspecialidad());
            else if (registro instanceof Sesion s)
                texto.append(s.codigo()).append(" | ").append(s.fecha()).append(" | ").append(s.especialidad())
                        .append(" | Aprendiz: ").append(s.nombreAprendiz()).append(" (").append(s.cedulaAprendiz())
                        .append(") | Instructor: ").append(s.nombreInstructor()).append(" (").append(s.cedulaInstructor()).append(")");
            texto.append('\n');
        }
        salida.setText(registros.isEmpty() ? "No hay registros para esta consulta." : texto.toString());
        salida.setCaretPosition(0);
    }

    private static final class FormularioCancelado extends RuntimeException { }
}
