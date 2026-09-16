package core.main;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MenuInstructorTest {
    @TempDir Path carpeta;

    private String ejecutar(String entrada) throws Exception {
        InputStream originalEntrada = System.in;
        PrintStream originalSalida = System.out;
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(entrada.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(salida, true, StandardCharsets.UTF_8));
            Main.main(new String[] {carpeta.toString()});
        } finally {
            System.setIn(originalEntrada);
            System.setOut(originalSalida);
        }
        return salida.toString(StandardCharsets.UTF_8);
    }

    @Test void crudDesdeMenuPersisteYActualizaReportesSinCambiarOriginal() throws Exception {
        Path original = carpeta.resolve("instructores.dat");
        try (ArchivoInstructores archivo = new ArchivoInstructores(original)) {
            archivo.agregar(new Instructor("Anterior", "12345678", "Música", "3001234567", 0));
        }
        byte[] antes = Files.readAllBytes(original);
        String salida = ejecutar(String.join("\n",
                "1", "Ana", "87654321", "Música", "3001234567", "0",
                "2", "87654321", "3", "Ana Ruiz", "87654321", "Pintura", "3011234567", "14",
                "5", "6", "pintura", "4", "12345678", "0", ""));
        assertTrue(salida.contains("Ana Ruiz"));
        assertTrue(salida.contains("Instructor modificado correctamente"));
        assertArrayEquals(antes, Files.readAllBytes(original));
        ArchivoInstructoresIndexado nuevo = ArchivoInstructoresIndexado.abrirParaMenu(carpeta);
        assertNull(nuevo.consultar(12345678));
        assertEquals("Ana Ruiz", nuevo.consultar(87654321)[1]);
        try (ArchivoSesiones sesiones = new ArchivoSesiones(carpeta.resolve("sesiones.dat"))) {
            ReporteServicio reportes = new ReporteServicio(nuevo, sesiones);
            assertEquals(1, reportes.instructoresDisponibles("PINTURA").size());
            assertTrue(reportes.instructoresDisponibles("música").isEmpty());
            sesiones.agregar(new Sesion("S1", "Aprendiz", "12345678", "Pintura", "Ana", "101",
                    java.time.LocalDate.of(2026, 9, 14)));
            assertEquals(1, reportes.sesionesPorInstructor("101").size());
        }
        // No vuelve a importar al instructor eliminado en el segundo arranque.
        String segunda = ejecutar("2\n12345678\n4\n87654321\n6\npintura\n0\n");
        assertTrue(segunda.contains("No hay instructores disponibles"));
        assertEquals(0, ArchivoInstructoresIndexado.abrirParaMenu(carpeta).listar().length);
    }

    @Test void rechazaErroresDesdeMenuSinConfirmarOperacionesFallidas() throws Exception {
        String salida = ejecutar(String.join("\n",
                "1", "Ana", "12345678", "Música", "3001234567", "65536",
                "1", "Ana", "abc", "Música", "3001234567", "0",
                "3", "Ana", "12345678", "Música", "3001234567", "0",
                "4", "abc", "2", "999999999999", "0", ""));
        assertTrue(salida.contains("entre 0 y 15"));
        assertTrue(salida.contains("solo dígitos"));
        assertTrue(salida.contains("2147483647"));
        assertFalse(salida.contains("modificado correctamente"));
        assertFalse(salida.contains("eliminado correctamente"));
        assertEquals(0, ArchivoInstructoresIndexado.abrirParaMenu(carpeta).listar().length);
    }

    @Test void importacionIncompatibleNoDejaCopiaParcialNiModificaOriginal() throws Exception {
        Path original = carpeta.resolve("instructores.dat");
        try (ArchivoInstructores archivo = new ArchivoInstructores(original)) {
            archivo.agregar(new Instructor("Ana", "12345678", "Música", "3001234567", 0));
            archivo.agregar(new Instructor("Luis", "00123456", "Música", "3001234567", 0));
        }
        byte[] antes = Files.readAllBytes(original);
        assertThrows(IOException.class, () -> ArchivoInstructoresIndexado.abrirParaMenu(carpeta));
        assertFalse(Files.exists(carpeta.resolve("instructores-indexados.dat")));
        assertArrayEquals(antes, Files.readAllBytes(original));
    }

    @Test void gestionaAprendicesDesdeMenuConContadoresIndependientes() throws Exception {
        String salida = ejecutar(String.join("\n",
                "9", "Ana", "12345678", "3001234567", "2", "musica", "4", "Pintura", "2",
                "10", "12345678", "11", "Ana Ruiz", "12345678", "3011234567", "1", "Baile", "3",
                "13", "8", "0", ""));
        assertTrue(salida.contains("Aprendiz registrado correctamente"));
        assertTrue(salida.contains("Aprendiz modificado correctamente"));
        assertTrue(salida.contains("Música: 4 sesiones"));
        assertTrue(salida.contains("Pintura: 2 sesiones"));
        try (ArchivoAprendices archivo = new ArchivoAprendices(carpeta.resolve("aprendices.dat"))) {
            assertEquals("Ana Ruiz", archivo.buscar("12345678").nombre());
            assertEquals(0, archivo.buscar("12345678").sesionesPorEspecialidad().get("Baile"));
        }
        String segunda = ejecutar("12\n12345678\n12\n12345678\n10\n12345678\n13\n0\n");
        assertTrue(segunda.contains("Aprendiz eliminado correctamente"));
        assertTrue(segunda.contains("No existe un aprendiz"));
        assertTrue(segunda.contains("No hay aprendices registrados"));
        try (ArchivoAprendices archivo = new ArchivoAprendices(carpeta.resolve("aprendices.dat"))) {
            assertTrue(archivo.listar().isEmpty());
        }
    }

    @Test void formularioIncompletoNoGuardaAprendiz() throws Exception {
        String salida = ejecutar("9\nAna\n12345678\n");
        assertTrue(salida.contains("formulario incompleto"));
        try (ArchivoAprendices archivo = new ArchivoAprendices(carpeta.resolve("aprendices.dat"))) {
            assertTrue(archivo.listar().isEmpty());
        }
    }
}
