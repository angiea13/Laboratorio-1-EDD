package core.main;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class InstructorServicioTest {
    @TempDir Path temporal;

    @Test void realizaCrudCompletoYPersisteLosCambios() throws Exception {
        Path datos = temporal.resolve("instructores.dat");
        try (ArchivoInstructores archivo = new ArchivoInstructores(datos)) {
            InstructorServicio servicio = new InstructorServicio(archivo);
            servicio.crear(instructor("Ana", "12345678", "Música", "3001234567"));
            assertEquals("Ana", servicio.consultar("12345678").orElseThrow().nombre());
            servicio.modificar(instructor("Ana Ruiz", "12345678", "Pintura", "3011234567"));
            assertEquals("Pintura", servicio.consultar("12345678").orElseThrow().especialidad());
            assertEquals(1, servicio.listar().size());
            servicio.eliminar("12345678");
            assertTrue(servicio.consultar("12345678").isEmpty());
        }
        try (ArchivoInstructores reabierto = new ArchivoInstructores(datos)) {
            assertFalse(reabierto.existe("12345678"));
        }
    }

    @Test void rechazaCedulaDuplicada() throws Exception {
        try (ArchivoInstructores archivo = new ArchivoInstructores(temporal.resolve("a.dat"))) {
            InstructorServicio servicio = new InstructorServicio(archivo);
            servicio.crear(instructor("Ana", "12345678", "Música", "3001234567"));
            ValidacionException error = assertThrows(ValidacionException.class,
                    () -> servicio.crear(instructor("Luis", "12345678", "Baile", "3011234567")));
            assertTrue(error.getMessage().contains("Ya existe"));
        }
    }

    @Test void rechazaEliminarInstructorInexistente() throws Exception {
        try (ArchivoInstructores archivo = new ArchivoInstructores(temporal.resolve("b.dat"))) {
            InstructorServicio servicio = new InstructorServicio(archivo);
            ValidacionException error = assertThrows(ValidacionException.class, () -> servicio.eliminar("99999999"));
            assertTrue(error.getMessage().contains("No existe"));
        }
    }

    @Test void rechazaCamposVaciosYCedulaOTelefonoMalFormados() throws Exception {
        try (ArchivoInstructores archivo = new ArchivoInstructores(temporal.resolve("c.dat"))) {
            InstructorServicio servicio = new InstructorServicio(archivo);
            assertThrows(ValidacionException.class, () -> servicio.crear(instructor("", "12345678", "Música", "3001234567")));
            assertThrows(ValidacionException.class, () -> servicio.crear(instructor("Ana", "12A", "Música", "3001234567")));
            assertThrows(ValidacionException.class, () -> servicio.crear(instructor("Ana", "12345678", "Música", "teléfono")));
            assertTrue(servicio.listar().isEmpty());
        }
    }

    private Instructor instructor(String nombre, String cedula, String especialidad, String telefono) {
        return new Instructor(nombre, cedula, especialidad, telefono, 0);
    }
}
