package core.main;

import java.util.LinkedHashMap;
import java.util.Map;

/** Datos de un aprendiz y sesiones realizadas por cada especialidad. */
public record Aprendiz(String nombre, String cedula, String telefono,
                       Map<String, Integer> sesionesPorEspecialidad) {
    public Aprendiz {
        sesionesPorEspecialidad = sesionesPorEspecialidad == null
                ? Map.of()
                : java.util.Collections.unmodifiableMap(new LinkedHashMap<>(sesionesPorEspecialidad));
    }
}
