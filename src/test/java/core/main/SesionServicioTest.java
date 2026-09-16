package core.main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.time.*;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class SesionServicioTest {
    @TempDir Path carpeta;
    private final Clock reloj = Clock.fixed(Instant.parse("2026-09-01T12:00:00Z"), ZoneOffset.UTC);
    private final LocalDate fecha = LocalDate.of(2026, 9, 10);

    private ArchivoInstructoresIndexado instructores(int sesiones) throws Exception {
        ArchivoInstructoresIndexado archivo = ArchivoInstructoresIndexado.abrirParaMenu(carpeta);
        assertTrue(archivo.guardar("123456", "Ana", "Música", "3001234567", (short) sesiones, false));
        return archivo;
    }

    private void aprendiz(ArchivoAprendices archivo, String id, int sesiones) throws Exception {
        archivo.guardar(new Aprendiz("Valerie", id, "3001234567", Map.of("Música", sesiones, "Baile", 0)));
    }

    @Test void asignaCancelaYPersisteSinAfectarOtrosRegistros() throws Exception {
        var i = instructores(0);
        try (var a = new ArchivoAprendices(carpeta.resolve("aprendices.dat"));
             var s = new ArchivoSesiones(carpeta.resolve("sesiones.dat"))) {
            aprendiz(a, "654321", 0);
            var servicio = new SesionServicio(s, a, i, reloj);
            servicio.asignar(" S1 ", "654321", "musica", "123456", fecha);
            servicio.asignar("S2", "654321", "Música", "123456", fecha.plusDays(1));
            assertEquals(2, a.buscar("654321").sesionesPorEspecialidad().get("Música"));
            assertEquals("2", i.consultar(123456)[4]);
            servicio.cancelar("S1");
            assertNull(s.buscar("S1"));
            assertTrue(s.buscarPorFecha(fecha).isEmpty());
            assertEquals(1, s.buscarPorInstructor("123456").size());
            assertEquals(1, a.buscar("654321").sesionesPorEspecialidad().get("Música"));
            assertEquals(0, a.buscar("654321").sesionesPorEspecialidad().get("Baile"));
            assertEquals("1", i.consultar(123456)[4]);
        }
        try (var s = new ArchivoSesiones(carpeta.resolve("sesiones.dat"))) {
            assertNull(s.buscar("S1"));
            assertNotNull(s.buscar("S2"));
            assertEquals(1, s.listar().size());
        }
        assertEquals("1", ArchivoInstructoresIndexado.abrirParaMenu(carpeta).consultar(123456)[4]);
    }

    @Test void rechazaDuplicadosYDatosInvalidosSinCambiarContadores() throws Exception {
        var i = instructores(0);
        try (var a = new ArchivoAprendices(carpeta.resolve("aprendices.dat")); var s = new ArchivoSesiones(carpeta.resolve("sesiones.dat"))) {
            aprendiz(a, "654321", 0); aprendiz(a, "654322", 0);
            assertTrue(i.guardar("123457", "Luis", "Música", "3001234567", (short) 0, false));
            var servicio = new SesionServicio(s, a, i, reloj);
            servicio.asignar("S1", "654321", "Música", "123456", fecha);
            assertThrows(ValidacionException.class, () -> servicio.asignar("S1", "654322", "Música", "123457", fecha.plusDays(1)));
            assertThrows(ValidacionException.class, () -> servicio.asignar("S2", "654322", "Música", "123456", fecha));
            assertThrows(ValidacionException.class, () -> servicio.asignar("S2", "654321", "Música", "123457", fecha));
            assertThrows(ValidacionException.class, () -> servicio.asignar("S2", "999999", "Música", "123457", fecha));
            assertThrows(ValidacionException.class, () -> servicio.asignar("S2", "654321", "Pintura", "123457", fecha));
            assertThrows(ValidacionException.class, () -> servicio.asignar("S2", "654321", "Música", "123457", fecha.minusMonths(1)));
            assertEquals(1, s.listar().size());
            assertEquals("1", i.consultar(123456)[4]);
            assertEquals("0", i.consultar(123457)[4]);
            assertEquals(0, a.buscar("654322").sesionesPorEspecialidad().get("Música"));
        }
    }

    @Test void respetaLimitesImportadosYReinicioDeInstructores() throws Exception {
        var i = instructores(15);
        try (var a = new ArchivoAprendices(carpeta.resolve("aprendices.dat")); var s = new ArchivoSesiones(carpeta.resolve("sesiones.dat"))) {
            aprendiz(a, "654321", 4);
            var servicio = new SesionServicio(s, a, i, reloj);
            assertTrue(servicio.disponibles("Música", fecha).isEmpty());
            assertEquals(1, i.reiniciarContadoresMensuales());
            assertEquals("0", i.consultar(123456)[4]);
            assertEquals("Ana", i.consultar(123456)[1]);
            assertThrows(ValidacionException.class, () -> servicio.asignar("S1", "654321", "Música", "123456", fecha));
            a.reiniciarContadoresPorEspecialidad();
            servicio.asignar("S1", "654321", "Música", "123456", fecha);
            assertEquals("1", i.consultar(123456)[4]);
        }
    }

    @Test void soloCancelaFechasFuturas() throws Exception {
        var i = instructores(0);
        try (var a = new ArchivoAprendices(carpeta.resolve("aprendices.dat")); var s = new ArchivoSesiones(carpeta.resolve("sesiones.dat"))) {
            aprendiz(a, "654321", 0);
            s.agregar(new Sesion("ayer", "Valerie", "654321", "Música", "Ana", "123456", LocalDate.of(2026, 8, 31)));
            var servicio = new SesionServicio(s, a, i, reloj);
            servicio.asignar("hoy", "654321", "Música", "123456", LocalDate.of(2026, 9, 1));
            assertThrows(ValidacionException.class, () -> servicio.cancelar("ayer"));
            assertThrows(ValidacionException.class, () -> servicio.cancelar("hoy"));
            assertThrows(ValidacionException.class, () -> servicio.cancelar("inexistente"));
            assertEquals(2, s.listar().size());
        }
    }

    @Test void limitaPorMesYEspecialidadSinMezclarReservasFuturas() throws Exception {
        var i = instructores(0);
        try (var a = new ArchivoAprendices(carpeta.resolve("aprendices.dat")); var s = new ArchivoSesiones(carpeta.resolve("sesiones.dat"))) {
            aprendiz(a, "654321", 0);
            var servicio = new SesionServicio(s, a, i, reloj);
            for (int n = 0; n < 4; n++) servicio.asignar("S" + n, "654321", "Música", "123456", fecha.plusDays(n));
            assertThrows(ValidacionException.class, () -> servicio.asignar("S4", "654321", "Música", "123456", fecha.plusDays(4)));
            servicio.asignar("octubre", "654321", "Música", "123456", fecha.plusMonths(1));
            assertEquals("4", i.consultar(123456)[4]);
            servicio.cancelar("octubre");
            assertEquals(4, a.buscar("654321").sesionesPorEspecialidad().get("Música"));
            servicio.cancelar("S0");
            servicio.asignar("reemplazo", "654321", "Música", "123456", fecha.plusDays(4));
            assertEquals(4, s.listar().size());
        }
    }

    @Test void quinceSesionesCierranLaDisponibilidadDelMesFuturo() throws Exception {
        var i = instructores(0);
        try (var a = new ArchivoAprendices(carpeta.resolve("aprendices.dat")); var s = new ArchivoSesiones(carpeta.resolve("sesiones.dat"))) {
            var servicio = new SesionServicio(s, a, i, reloj);
            for (int n = 0; n < 16; n++) aprendiz(a, "" + (654321 + n), 0);
            LocalDate octubre = LocalDate.of(2026, 10, 1);
            for (int n = 0; n < 15; n++) servicio.asignar("S" + n, "" + (654321 + n), "Música", "123456", octubre.plusDays(n));
            assertTrue(servicio.disponibles("Música", octubre.plusDays(15)).isEmpty());
            assertThrows(ValidacionException.class, () -> servicio.asignar("S15", "654336", "Música", "123456", octubre.plusDays(15)));
            servicio.cancelar("S0");
            servicio.asignar("S15", "654336", "Música", "123456", octubre.plusDays(15));
            assertEquals(15, s.listar().size());
            assertEquals("0", i.consultar(123456)[4]);
        }
    }
}
