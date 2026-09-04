package core.main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AprendizServicioTest {
    @TempDir Path temporal;

    @Test void reiniciaTodasLasEspecialidadesSinAlterarLosDemasDatos() throws Exception {
        Path datos = temporal.resolve("aprendices.dat");
        Map<String, Integer> artes = new LinkedHashMap<>();
        artes.put("Música", 4);
        artes.put("Pintura", 2);
        try (ArchivoAprendices archivo = new ArchivoAprendices(datos)) {
            archivo.guardar(new Aprendiz("Ana Pérez", "12345678", "3001234567", artes));
            archivo.guardar(new Aprendiz("Luis Ruiz", "87654321", "3011234567", Map.of("Baile", 3)));
            assertEquals(3, new AprendizServicio(archivo).reiniciarContadoresMensuales());
            var resultado = archivo.listar();
            assertEquals("Ana Pérez", resultado.get(0).nombre());
            assertEquals("12345678", resultado.get(0).cedula());
            assertEquals("3001234567", resultado.get(0).telefono());
            assertEquals(java.util.Set.of("Música", "Pintura"), resultado.get(0).sesionesPorEspecialidad().keySet());
            resultado.forEach(a -> a.sesionesPorEspecialidad().values().forEach(s -> assertEquals(0, s)));
        }
        try (ArchivoAprendices reabierto = new ArchivoAprendices(datos)) {
            reabierto.listar().forEach(a -> a.sesionesPorEspecialidad().values().forEach(s -> assertEquals(0, s)));
        }
    }
}
