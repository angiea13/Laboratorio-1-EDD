package core.main;

import java.util.regex.Pattern;

/** Impide que datos inválidos lleguen al archivo. */
public final class InstructorValidador {
    private static final Pattern CEDULA = Pattern.compile("\\d{6,12}");
    private static final Pattern TELEFONO = Pattern.compile("\\d{7,10}");
    private InstructorValidador() { }

    public static Instructor validar(Instructor instructor) throws ValidacionException {
        if (instructor == null) throw new ValidacionException("El instructor es obligatorio.");
        String nombre = limpiar(instructor.nombre());
        String cedula = limpiar(instructor.cedula());
        String especialidad = limpiar(instructor.especialidad());
        String telefono = limpiar(instructor.telefono());
        if (nombre.isEmpty() || especialidad.isEmpty() || cedula.isEmpty() || telefono.isEmpty())
            throw new ValidacionException("Todos los campos son obligatorios.");
        if (!CEDULA.matcher(cedula).matches())
            throw new ValidacionException("La cédula debe contener entre 6 y 12 dígitos.");
        if (!TELEFONO.matcher(telefono).matches())
            throw new ValidacionException("El teléfono debe contener entre 7 y 10 dígitos.");
        if (instructor.sesionesRealizadas() < 0 || instructor.sesionesRealizadas() > 15)
            throw new ValidacionException("Las sesiones realizadas deben estar entre 0 y 15.");
        return new Instructor(nombre, cedula, especialidad, telefono, instructor.sesionesRealizadas());
    }

    public static String validarCedula(String cedula) throws ValidacionException {
        String limpia = limpiar(cedula);
        if (!CEDULA.matcher(limpia).matches())
            throw new ValidacionException("La cédula debe contener entre 6 y 12 dígitos.");
        return limpia;
    }

    private static String limpiar(String valor) { return valor == null ? "" : valor.trim(); }
}
