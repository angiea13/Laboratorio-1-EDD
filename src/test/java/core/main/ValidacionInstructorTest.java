package core.main;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ValidacionInstructorTest {
    @TempDir Path temporal;

    @Test void mensajesIdentificanCampoYRechazanDatosAntesDeInsertarOActualizar() throws Exception {
        Instructor[] invalidos = {
            new Instructor(" ", "12345678", "Música", "3001234567", 0),
            new Instructor("Ana", "", "Música", "3001234567", 0),
            new Instructor("Ana", "12A45678", "Música", "3001234567", 0),
            new Instructor("Ana", "12345678", " ", "3001234567", 0),
            new Instructor("Ana", "12345678", "Música", " ", 0),
            new Instructor("Ana", "12345678", "Música", "teléfono", 0),
            new Instructor("Ana", "12345678", "Música", "123", 0),
            new Instructor("Ana", "12345678", "Música", "0000000000", 0),
            new Instructor("Ana", "12345678", "Música", "3001234567", 16),
            new Instructor("x".repeat(81), "12345678", "Música", "3001234567", 0)
        };
        String[] campos = {"nombre", "cédula", "cédula", "especialidad", "teléfono", "teléfono", "teléfono", "teléfono", "sesiones", "nombre"};
        Path ruta = temporal.resolve("anterior.dat");
        try (ArchivoInstructores archivo = new ArchivoInstructores(ruta)) {
            archivo.agregar(new Instructor("Ana", "12345678", "Música", "3001234567", 0));
            byte[] antes = Files.readAllBytes(ruta);
            for (int i = 0; i < invalidos.length; i++) {
                Instructor invalido = invalidos[i];
                ValidacionException error = assertThrows(ValidacionException.class, () -> archivo.agregar(invalido));
                assertTrue(error.getMessage().contains(campos[i]));
                assertThrows(ValidacionException.class, () -> archivo.actualizar(invalido));
                assertArrayEquals(antes, Files.readAllBytes(ruta));
            }
        }
    }

    @Test void crudNuevoValidaTambienAlModificarSinCambiarDatosOIndice() throws Exception {
        Path datos = temporal.resolve("nuevo.dat");
        Path indice = temporal.resolve("nuevo.idx");
        ArchivoInstructoresIndexado archivo = new ArchivoInstructoresIndexado(datos.toString(), indice.toString());
        assertTrue(archivo.guardar("12345678", "Ana", "Música", "3001234567", (short) 0, false));
        byte[] antes = Files.readAllBytes(datos);
        byte[] indiceAntes = Files.readAllBytes(indice);
        for (boolean modificar : new boolean[] {false, true}) {
            assertFalse(archivo.guardar("12345678", "Ana", "Música", "abc", (short) 0, modificar));
            assertFalse(archivo.guardar("12345678", "Ana", "Música", "123", (short) 0, modificar));
            assertFalse(archivo.guardar("12345678", "x".repeat(51), "Música", "3001234567", (short) 0, modificar));
            assertFalse(archivo.guardar("12345678", "Ana", "x".repeat(21), "3001234567", (short) 0, modificar));
            assertFalse(archivo.guardar("12345678", "Ana", "Música", "3001234567", (short) -1, modificar));
            assertFalse(archivo.guardar("999999999999", "Ana", "Música", "3001234567", (short) 0, modificar));
            assertArrayEquals(antes, Files.readAllBytes(datos));
            assertArrayEquals(indiceAntes, Files.readAllBytes(indice));
        }
        assertEquals(1, archivo.disponibles("Música").length);
    }
}
