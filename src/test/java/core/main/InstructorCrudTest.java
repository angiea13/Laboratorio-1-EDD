package core.main;

import java.io.*;

/** Prueba ejecutable con Java, sin librerías adicionales. Usa únicamente target. */
public class InstructorCrudTest {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void main(String[] args) throws IOException {
        File directory = new File("target/req003-test-" + System.nanoTime());
        if (!directory.mkdirs()) throw new IOException("No se pudo crear carpeta de prueba.");
        String data = new File(directory, "instructor.dat").getPath();
        String index = new File(directory, "instructor.idx").getPath();
        ArchivoInstructoresIndexado file = new ArchivoInstructoresIndexado(data, index);
        file.guardar("101", "Ana", "Música", "3001234567", (short) 0, false);
        file.guardar("102", "Luis", "Pintura", "3007654321", (short) 15, false);
        check(new File(data).length() == 308, "Dos registros deben ocupar 308 bytes.");
        check(file.consultar(101)[2].equals("Música"), "Lectura de especialidad.");
        file.guardar("101", "Duplicado", "Baile", "3001234567", (short) 0, false);
        file.guardar("103", " ", "Baile", "3001234567", (short) 0, false);
        file.guardar("104", "Eva", "Baile", "abc", (short) 0, false);
        file.guardar("105", "Eva", "Baile", "3001234567", (short) 16, false);
        file.guardar("999999999999", "Eva", "Baile", "3001234567", (short) 0, false);
        check(new File(data).length() == 308, "No guardar entradas inválidas o duplicadas.");
        file.guardar("101", "Ana María Gómez", "Escritura", "3001112222", (short) 5, true);
        check(file.consultar(101)[1].equals("Ana María Gómez"), "Modificación del nombre.");
        check(file.consultar(102)[1].equals("Luis"), "Modificar no debe dañar al vecino.");
        file.eliminar(101);
        file.eliminar(999);
        check(file.consultar(101) == null, "Baja lógica.");
        check(new File(data).length() == 308, "Baja no desplaza registros.");
        file = new ArchivoInstructoresIndexado(data, index);
        check(file.consultar(101) == null, "La baja persiste al reabrir.");
        check(file.consultar(102)[4].equals("15"), "El otro registro persiste.");
        file.guardar("101", "Ana", "Baile", "3001234567", (short) 0, false);
        check(new File(data).length() == 462, "Reingreso al final del archivo.");
        try (DataInputStream input = new DataInputStream(new FileInputStream(index))) {
            int count = 0;
            while (input.available() > 0) {
                int id = input.readInt();
                long position = input.readLong();
                check((id == 102 && position == 154) || (id == 101 && position == 308), "Índice persistente incorrecto.");
                count++;
            }
            check(count == 2, "Índice con dos instructores activos.");
        }
        try (RandomAccessFile corrupt = new RandomAccessFile(data, "rw")) {
            corrupt.seek(corrupt.length());
            corrupt.writeByte(1);
        }
        boolean rejected = false;
        try { new ArchivoInstructoresIndexado(data, index); }
        catch (IOException e) { rejected = true; }
        check(rejected, "Rechazar archivo incompleto.");
        System.out.println("Pruebas CRUD de instructores: OK.");
    }
}
