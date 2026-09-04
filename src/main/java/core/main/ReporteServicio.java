package core.main;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/** Genera reportes mediante los índices mantenidos por los archivos. */
public final class ReporteServicio {
    private final ArchivoInstructores instructores;
    private final ArchivoSesiones sesiones;

    public ReporteServicio(ArchivoInstructores instructores, ArchivoSesiones sesiones) {
        this.instructores = instructores;
        this.sesiones = sesiones;
    }

    public List<Instructor> instructoresDisponibles(String especialidad) throws IOException, ValidacionException {
        if (especialidad == null || especialidad.isBlank())
            throw new ValidacionException("La especialidad es obligatoria.");
        return instructores.buscarDisponiblesPorEspecialidad(especialidad);
    }

    public List<Sesion> sesionesPorFecha(LocalDate fecha) throws IOException, ValidacionException {
        if (fecha == null) throw new ValidacionException("La fecha es obligatoria.");
        return sesiones.buscarPorFecha(fecha);
    }

    public List<Sesion> sesionesPorInstructor(String cedula) throws IOException, ValidacionException {
        return sesiones.buscarPorInstructor(InstructorValidador.validarCedula(cedula));
    }
}
