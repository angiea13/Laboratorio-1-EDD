package core.main;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/** Coordina los tres archivos. Valida todo antes de modificar sus registros. */
public final class SesionServicio {
    private final ArchivoSesiones sesiones;
    private final ArchivoAprendices aprendices;
    private final ArchivoInstructoresIndexado instructores;
    private final java.time.Clock reloj;

    public SesionServicio(ArchivoSesiones sesiones, ArchivoAprendices aprendices,
                         ArchivoInstructoresIndexado instructores) {
        this(sesiones, aprendices, instructores, java.time.Clock.systemDefaultZone());
    }

    SesionServicio(ArchivoSesiones sesiones, ArchivoAprendices aprendices,
                   ArchivoInstructoresIndexado instructores, java.time.Clock reloj) {
        this.sesiones = sesiones;
        this.aprendices = aprendices;
        this.instructores = instructores;
        this.reloj = reloj;
    }

    private LocalDate hoy() { return LocalDate.now(reloj); }

    private boolean mismoMes(LocalDate a, LocalDate b) {
        return YearMonth.from(a).equals(YearMonth.from(b));
    }

    private int cantidadInstructor(Instructor instructor, LocalDate fecha) throws IOException {
        int cantidad = 0;
        for (Sesion s : sesiones.buscarPorInstructor(instructor.cedula()))
            if (mismoMes(s.fecha(), fecha)) cantidad++;
        // Los contadores importados pertenecen al mes actual; el historial permite reservar otros meses.
        if (mismoMes(fecha, hoy())) cantidad = Math.max(cantidad, instructor.sesionesRealizadas());
        return cantidad;
    }

    private int cantidadAprendiz(Aprendiz aprendiz, String especialidad, LocalDate fecha) throws IOException {
        int cantidad = 0;
        for (Sesion s : sesiones.buscarPorAprendiz(aprendiz.cedula()))
            if (s.cedulaAprendiz().equals(aprendiz.cedula()) && s.especialidad().equals(especialidad)
                    && mismoMes(s.fecha(), fecha)) cantidad++;
        if (mismoMes(fecha, hoy()))
            cantidad = Math.max(cantidad, aprendiz.sesionesPorEspecialidad().get(especialidad));
        return cantidad;
    }

    public List<Instructor> disponibles(String especialidad, LocalDate fecha) throws IOException, ValidacionException {
        especialidad = CatalogoEspecialidades.validar(especialidad);
        validarFecha(fecha);
        List<Instructor> resultado = new ArrayList<>();
        for (Instructor i : instructores.listar()) {
            if (!i.especialidad().equals(especialidad) || cantidadInstructor(i, fecha) >= 15) continue;
            boolean ocupado = false;
            for (Sesion s : sesiones.buscarPorFecha(fecha))
                if (s.cedulaInstructor().equals(i.cedula())) ocupado = true;
            if (!ocupado) resultado.add(i);
        }
        return resultado;
    }

    private void validarFecha(LocalDate fecha) throws ValidacionException {
        if (fecha == null || fecha.isBefore(hoy()))
            throw new ValidacionException("La fecha debe ser hoy o una fecha futura.");
    }

    public synchronized Sesion asignar(String codigo, String cedulaAprendiz, String especialidad,
                                       String cedulaInstructor, LocalDate fecha)
            throws IOException, ValidacionException {
        codigo = ValidacionDatos.texto(codigo, "código", 30);
        especialidad = CatalogoEspecialidades.validar(especialidad);
        validarFecha(fecha);
        if (sesiones.buscar(codigo) != null) throw new ValidacionException("El código de sesión ya existe.");
        Aprendiz aprendiz = aprendices.buscar(cedulaAprendiz);
        if (aprendiz == null) throw new ValidacionException("El aprendiz no está registrado.");
        if (!aprendiz.sesionesPorEspecialidad().containsKey(especialidad))
            throw new ValidacionException("El aprendiz no practica esa especialidad.");
        int cantidadAprendiz = cantidadAprendiz(aprendiz, especialidad, fecha);
        if (cantidadAprendiz >= 4) throw new ValidacionException("El aprendiz ya tiene cuatro sesiones en ese mes y especialidad.");
        int id = ArchivoInstructoresIndexado.leerCedula(cedulaInstructor);
        Instructor instructor = null;
        for (Instructor i : disponibles(especialidad, fecha))
            if (Integer.parseInt(i.cedula()) == id) instructor = i;
        if (instructor == null) throw new ValidacionException("El instructor no existe o no está disponible para esa especialidad y fecha.");
        for (Sesion s : sesiones.buscarPorFecha(fecha))
            if (s.cedulaAprendiz().equals(aprendiz.cedula()))
                throw new ValidacionException("El aprendiz ya tiene una sesión en esa fecha.");
        Sesion nueva = new Sesion(codigo, aprendiz.nombre(), aprendiz.cedula(), especialidad,
                instructor.nombre(), instructor.cedula(), fecha);
        boolean actual = mismoMes(fecha, hoy());
        try {
            if (actual) {
                cambiarAprendiz(aprendiz, especialidad, cantidadAprendiz + 1);
                instructores.actualizarContador(id, cantidadInstructor(instructor, fecha) + 1);
            }
            sesiones.agregar(nueva);
        } catch (IOException | ValidacionException error) {
            // Compensación ante errores de escritura: conservar los contadores anteriores.
            if (actual) restaurar(aprendiz, instructor, error);
            throw error;
        }
        return nueva;
    }

    public synchronized void cancelar(String codigo) throws IOException, ValidacionException {
        codigo = ValidacionDatos.texto(codigo, "código", 30);
        Sesion sesion = sesiones.buscar(codigo);
        if (sesion == null) throw new ValidacionException("La sesión no existe.");
        if (!sesion.fecha().isAfter(hoy()))
            throw new ValidacionException("Solo se pueden cancelar sesiones con fecha futura.");
        Aprendiz aprendiz = aprendices.buscar(sesion.cedulaAprendiz());
        Instructor instructor = null;
        for (Instructor i : instructores.listar())
            if (i.cedula().equals(sesion.cedulaInstructor())) instructor = i;
        boolean actual = mismoMes(sesion.fecha(), hoy());
        try {
            if (actual) {
                if (aprendiz != null && aprendiz.sesionesPorEspecialidad().containsKey(sesion.especialidad()))
                    cambiarAprendiz(aprendiz, sesion.especialidad(), Math.max(0,
                            cantidadAprendiz(aprendiz, sesion.especialidad(), sesion.fecha()) - 1));
                if (instructor != null) instructores.actualizarContador(Integer.parseInt(instructor.cedula()),
                        Math.max(0, cantidadInstructor(instructor, sesion.fecha()) - 1));
            }
            sesiones.eliminar(codigo);
        } catch (IOException | ValidacionException error) {
            if (actual) restaurar(aprendiz, instructor, error);
            throw error;
        }
    }

    private void cambiarAprendiz(Aprendiz aprendiz, String especialidad, int cantidad)
            throws IOException, ValidacionException {
        LinkedHashMap<String, Integer> contadores = new LinkedHashMap<>(aprendiz.sesionesPorEspecialidad());
        contadores.put(especialidad, cantidad);
        aprendices.actualizar(new Aprendiz(aprendiz.nombre(), aprendiz.cedula(), aprendiz.telefono(), contadores));
    }

    private void restaurar(Aprendiz aprendiz, Instructor instructor, Exception original) {
        try { if (aprendiz != null) aprendices.actualizar(aprendiz); }
        catch (Exception error) { original.addSuppressed(error); }
        try { if (instructor != null) instructores.actualizarContador(Integer.parseInt(instructor.cedula()), instructor.sesionesRealizadas()); }
        catch (Exception error) { original.addSuppressed(error); }
    }
}
