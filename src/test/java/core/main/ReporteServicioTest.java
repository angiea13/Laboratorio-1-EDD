package core.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ReporteServicioTest {
    @TempDir Path temporal;

    @Test void soloMuestraInstructoresConCupoDeLaEspecialidadSolicitada() throws Exception {
        try (ArchivoInstructores instructores = new ArchivoInstructores(temporal.resolve("i.dat"));
             ArchivoSesiones sesiones = new ArchivoSesiones(temporal.resolve("s.dat"))) {
            InstructorServicio servicio = new InstructorServicio(instructores);
            servicio.crear(new Instructor("Ana", "12345678", "Música", "3001234567", 14));
            servicio.crear(new Instructor("Luis", "87654321", "Música", "3011234567", 15));
            servicio.crear(new Instructor("Eva", "11223344", "Pintura", "3021234567", 0));
            var resultado = new ReporteServicio(instructores, sesiones).instructoresDisponibles("música");
            assertEquals(1, resultado.size());
            assertEquals("Ana", resultado.get(0).nombre());
        }
    }

    @Test void filtraSesionesPorFechaOPorInstructor() throws Exception {
        LocalDate hoy = LocalDate.of(2026, 9, 3);
        try (ArchivoInstructores instructores = new ArchivoInstructores(temporal.resolve("i2.dat"));
             ArchivoSesiones sesiones = new ArchivoSesiones(temporal.resolve("s2.dat"))) {
            sesiones.agregar(sesion("S1", "12345678", hoy));
            sesiones.agregar(sesion("S2", "87654321", hoy.plusDays(1)));
            ReporteServicio reportes = new ReporteServicio(instructores, sesiones);
            assertEquals("S1", reportes.sesionesPorFecha(hoy).get(0).codigo());
            assertEquals("S2", reportes.sesionesPorInstructor("87654321").get(0).codigo());
            assertTrue(reportes.sesionesPorInstructor("99999999").isEmpty());
        }
    }

    private Sesion sesion(String codigo, String instructor, LocalDate fecha) {
        return new Sesion(codigo, "Aprendiz", "11111111", "Música", "Instructor", instructor, fecha);
    }
}
