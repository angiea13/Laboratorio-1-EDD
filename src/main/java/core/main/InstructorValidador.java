package core.main;

/** Valida antes de guardar y comunica el campo que falló. */
public final class InstructorValidador {
    private InstructorValidador() { }

    public static Instructor validar(Instructor instructor) throws ValidacionException {
        if (instructor == null) throw new ValidacionException("El instructor es obligatorio.");
        String nombre = ValidacionDatos.texto(instructor.nombre(), "nombre", 80);
        String cedula = validarCedula(instructor.cedula());
        String especialidad = ValidacionDatos.texto(instructor.especialidad(), "especialidad", 60);
        String telefono = ValidacionDatos.digitos(instructor.telefono(), "teléfono", 7, 10);
        validarSesiones(instructor.sesionesRealizadas());
        return new Instructor(nombre, cedula, especialidad, telefono, instructor.sesionesRealizadas());
    }

    public static void validarSesiones(int cantidad) throws ValidacionException {
        if (cantidad < 0 || cantidad > 15)
            throw new ValidacionException("Las sesiones del instructor deben estar entre 0 y 15.");
    }

    public static String validarCedula(String cedula) throws ValidacionException {
        return ValidacionDatos.digitos(cedula, "cédula", 6, 12);
    }

    // El CRUD nuevo guarda la cédula como int y usa campos de 50 y 20 caracteres.
    public static Instructor validarIndexado(Instructor instructor) throws ValidacionException {
        if (instructor == null) throw new ValidacionException("El instructor es obligatorio.");
        String nombre = ValidacionDatos.texto(instructor.nombre(), "nombre", 50);
        int cedula = ArchivoInstructoresIndexado.leerCedula(instructor.cedula());
        String especialidad = ValidacionDatos.texto(instructor.especialidad(), "especialidad", 20);
        String telefono = ValidacionDatos.digitos(instructor.telefono(), "teléfono", 7, 10);
        validarSesiones(instructor.sesionesRealizadas());
        return new Instructor(nombre, "" + cedula, especialidad, telefono, instructor.sesionesRealizadas());
    }
}
