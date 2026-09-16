package core.main;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AprendizCrudTest {
    @TempDir Path temporal;

    private Aprendiz aprendiz(String cedula, Map<String, Integer> especialidades) {
        return new Aprendiz("Ana", cedula, "3001234567", especialidades);
    }

    @Test void crudConVariasEspecialidadesPersisteYNoAfectaVecinos() throws Exception {
        Path ruta = temporal.resolve("aprendices.dat");
        try (ArchivoAprendices archivo = new ArchivoAprendices(ruta)) {
            AprendizServicio servicio = new AprendizServicio(archivo);
            servicio.crear(aprendiz("12345678", Map.of("musica", 4, "Pintura", 2)));
            servicio.crear(aprendiz("87654321", Map.of("Baile", 1)));
            assertEquals(Map.of("Música", 4, "Pintura", 2), servicio.consultar("12345678").sesionesPorEspecialidad());
            long longitud = Files.size(ruta);
            servicio.modificar(new Aprendiz("Ana María", "12345678", "3011234567", Map.of("Escritura", 3)));
            assertEquals(longitud, Files.size(ruta));
            assertEquals("Ana", servicio.consultar("87654321").nombre());
            servicio.eliminar("12345678");
            assertNull(servicio.consultar("12345678"));
            assertEquals(longitud, Files.size(ruta));
            assertEquals(1, servicio.reiniciarContadoresMensuales());
        }
        try (ArchivoAprendices archivo = new ArchivoAprendices(ruta)) {
            assertNull(archivo.buscar("12345678"));
            assertEquals(0, archivo.buscar("87654321").sesionesPorEspecialidad().get("Baile"));
            archivo.guardar(aprendiz("12345678", Map.of("Música", 1)));
            assertEquals(2, archivo.listar().size());
        }
        try (ArchivoAprendices archivo = new ArchivoAprendices(ruta)) {
            assertEquals(1, archivo.buscar("12345678").sesionesPorEspecialidad().get("Música"));
        }
    }

    @Test void rechazaDuplicadosInexistentesYModificacionInvalidaSinEscribir() throws Exception {
        Path ruta = temporal.resolve("aprendices.dat");
        try (ArchivoAprendices archivo = new ArchivoAprendices(ruta)) {
            archivo.guardar(aprendiz("12345678", Map.of("Música", 1)));
            byte[] antes = Files.readAllBytes(ruta);
            assertThrows(ValidacionException.class, () -> archivo.guardar(aprendiz(" 12345678 ", Map.of("Baile", 0))));
            assertThrows(ValidacionException.class, () -> archivo.eliminar("99999999"));
            assertThrows(ValidacionException.class, () -> archivo.actualizar(aprendiz("99999999", Map.of("Baile", 0))));
            assertThrows(ValidacionException.class, () -> archivo.actualizar(aprendiz("12345678", Map.of("Desconocida", 0))));
            assertThrows(ValidacionException.class, () -> archivo.actualizar(aprendiz("12345678", Map.of("Música", 5))));
            assertArrayEquals(antes, Files.readAllBytes(ruta));
        }
    }

    @Test void validaCamposCatalogoRepeticionesYContadores() throws Exception {
        List<Aprendiz> invalidos = new ArrayList<>();
        invalidos.add(null);
        invalidos.add(new Aprendiz(" ", "12345678", "3001234567", Map.of("Música", 1)));
        invalidos.add(aprendiz("", Map.of("Música", 1)));
        invalidos.add(aprendiz("123A5678", Map.of("Música", 1)));
        invalidos.add(aprendiz("00000000", Map.of("Música", 1)));
        invalidos.add(new Aprendiz("Ana", "12345678", "teléfono", Map.of("Música", 1)));
        invalidos.add(aprendiz("12345678", Map.of()));
        invalidos.add(aprendiz("12345678", Map.of("Fútbol", 1)));
        invalidos.add(aprendiz("12345678", Map.of("Música", -1)));
        invalidos.add(aprendiz("12345678", Map.of("Música", 5)));
        invalidos.add(aprendiz("12345678", Map.of("musica", 1, " MÚSICA ", 2)));
        invalidos.add(new Aprendiz("x".repeat(81), "12345678", "3001234567", Map.of("Música", 1)));
        Map<String, Integer> nulo = new HashMap<>();
        nulo.put("Música", null);
        invalidos.add(aprendiz("12345678", nulo));
        Map<String, Integer> claveNula = new HashMap<>();
        claveNula.put(null, 0);
        invalidos.add(aprendiz("12345678", claveNula));
        Path ruta = temporal.resolve("aprendices.dat");
        try (ArchivoAprendices archivo = new ArchivoAprendices(ruta)) {
            for (Aprendiz invalido : invalidos) {
                ValidacionException error = assertThrows(ValidacionException.class, () -> archivo.guardar(invalido));
                assertFalse(error.getMessage().isBlank());
                assertEquals(0, Files.size(ruta));
            }
        }
    }

    @Test void rechazaArchivoIncompletoYCantidadDeEspecialidadesCorrupta() throws Exception {
        Path ruta = temporal.resolve("aprendices.dat");
        try (ArchivoAprendices archivo = new ArchivoAprendices(ruta)) {
            archivo.guardar(aprendiz("12345678", Map.of("Música", 0)));
        }
        try (RandomAccessFile archivo = new RandomAccessFile(ruta.toFile(), "rw")) {
            archivo.seek((80 + 24 + 20) * 2);
            archivo.writeInt(11);
        }
        assertThrows(IOException.class, () -> new ArchivoAprendices(ruta));
        // También prueba que el constructor fallido cerró el archivo en Windows.
        Files.write(ruta, new byte[] {1});
        assertThrows(IOException.class, () -> new ArchivoAprendices(ruta));
    }
}
