package core.main;

import java.util.LinkedHashMap;
import java.util.Map;

/** Valida el registro completo antes de insertar o sobrescribir un aprendiz. */
public final class AprendizValidador {
    private AprendizValidador() { }

    public static Aprendiz validar(Aprendiz aprendiz) throws ValidacionException {
        if (aprendiz == null) throw new ValidacionException("El aprendiz es obligatorio.");
        String nombre = ValidacionDatos.texto(aprendiz.nombre(), "nombre", 80);
        String cedula = InstructorValidador.validarCedula(aprendiz.cedula());
        String telefono = ValidacionDatos.digitos(aprendiz.telefono(), "teléfono", 7, 10);
        Map<String, Integer> datos = aprendiz.sesionesPorEspecialidad();
        if (datos.isEmpty()) throw new ValidacionException("Debe registrar al menos una especialidad.");
        if (datos.size() > 10) throw new ValidacionException("Se permiten máximo 10 especialidades.");
        Map<String, Integer> normalizadas = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entrada : datos.entrySet()) {
            String especialidad = CatalogoEspecialidades.validar(entrada.getKey());
            if (normalizadas.containsKey(especialidad))
                throw new ValidacionException("La especialidad " + especialidad + " está repetida.");
            Integer sesiones = entrada.getValue();
            if (sesiones == null || sesiones < 0 || sesiones > 4)
                throw new ValidacionException("Las sesiones de " + especialidad + " deben estar entre 0 y 4.");
            normalizadas.put(especialidad, sesiones);
        }
        return new Aprendiz(nombre, cedula, telefono, normalizadas);
    }
}
