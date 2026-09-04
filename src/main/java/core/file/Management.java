package core.file;

import java.io.*;
import java.util.HashMap;

public class Management {

    /*
    Campos INSTRUCTOR:
        Nombre -> 50 chars (100 bytes)
        Cédula -> int (4 bytes)
        Especialidad -> 20 chars (40 bytes)
        Teléfono -> long (8 bytes)
        # Sesiones -> short (2 bytes)
        Total: 150 bytes
    */
    protected final int INSTRUCTOR_RECORD_SIZE = 154;

    /*
    Campos APPRENTICE:
        Nombre -> 50 chars (100 bytes)
        Cédula -> int (4 bytes)
        # Especialidades Practicadas -> short (2 bytes)
        Teléfono -> long (8 bytes)
        # Sesiones -> short (2 bytes)
        Total: 112 bytes
    */
    protected final int APPRENTICE_RECORD_SIZE = 116;

    /*
    Campos SESSION:
        Código -> int (4 bytes)    
        Nombre aprendiz -> 50 chars (100 bytes)
        Cédula aprendiz -> int (4 bytes)
        Especialidad -> 20 chars (40 bytes)
        Instructor -> 50 chars (100 bytes)
        Fecha -> 16 chars (32 bytes)
        Total: 280 bytes
    */
    protected final int SESSION_RECORD_SIZE = 280;

    public Management() {
    }

    /*
    Esta subrutina añade un registro al archivo de aprendiz.
    Toma como parámetros los campos: la cédula, el nombre, el número de especialidades que practican,
    su número de teléfono, y el número de sesiones que llevan ese mes.
    Los otros dos parámetros son el HashMap que tiene cargados los índices de los aprendices (INDEX_FILE),
    y el nombre del archivo de aprendices .dat en el disco (DATA_FILE).
    */
    public void addApprentice(int id, String name, short nOfSpecialties, long phone, short nOfSessions, HashMap<Integer, Long> INDEX_FILE, String DATA_FILE) {
        if (INDEX_FILE.containsKey(id)) {
            System.out.println("Error: la cédula del aprendiz ya existe en los registros.");
            return;
        }
        try (RandomAccessFile apprenticeRAF = new RandomAccessFile(DATA_FILE, "rw")) {
            long posicion = apprenticeRAF.length();
            apprenticeRAF.seek(posicion);

            apprenticeRAF.writeInt(id);
            apprenticeRAF.writeChars(name + " ".repeat(50 - name.length()));
            apprenticeRAF.writeShort(nOfSpecialties);
            apprenticeRAF.writeLong(phone);
            apprenticeRAF.writeShort(nOfSessions);

            INDEX_FILE.put(id, posicion);
            System.out.println("Aprendiz guardado en posición: " + posicion);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /*
    Esta subrutina añade un registro al archivo de instructor.
    Toma como parámetros los campos: la cédula, el nombre, la especialidad que dicta,
    su número de teléfono, y el número de sesiones que tiene agendadas ese mes.
    Los otros dos parámetros son el HashMap que tiene cargados los índices de los instructores (INDEX_FILE),
    y el nombre del archivo de instructores .dat en el disco (DATA_FILE).
    */
    public void addInstructor(int id, String name, String specialty, long phone, short nOfSessions, HashMap<Integer, Long> INDEX_FILE, String DATA_FILE) {
        if (INDEX_FILE.containsKey(id)) {
            System.out.println("Error: la cédula del instructor ya existe en los registros.");
            return;
        }
        try (RandomAccessFile instructorRAF = new RandomAccessFile(DATA_FILE, "rw")) {
            long posicion = instructorRAF.length();
            instructorRAF.seek(posicion);

            instructorRAF.writeInt(id);
            instructorRAF.writeChars(name + " ".repeat(50 - name.length()));
            instructorRAF.writeChars(specialty + " ".repeat(40 - name.length()));
            instructorRAF.writeLong(phone);
            instructorRAF.writeShort(nOfSessions);

            INDEX_FILE.put(id, posicion);
            System.out.println("Instructor guardado en posición: " + posicion);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /*
    Esta subrutina añade un registro al archivo de sesiones.
    Toma como parámetros los campos: el código de la sesión, el nombre del aprendiz que asistirá,
    la cédula del aprendiz que asistirá, la especialidad que se abordará, el nombre del instructor,
    y la fecha de la sesión.
    Los otros dos parámetros son el HashMap que tiene cargados los índices de las sesiones (INDEX_FILE),
    y el nombre del archivo de instructores .dat en el disco (DATA_FILE).

    NOTA: la clave en este caso es el código de la sesión, no la cédula, como en los anteriores métodos.
    */
    public void addSession(int code, String apprenticeName, int apprenticeId, String specialty, String instructorName, String date, HashMap<Integer, Long> INDEX_FILE, String DATA_FILE) {
        if (INDEX_FILE.containsKey(code)) {
            System.out.println("Error: el código ya existe.");
            return;
        }
        try (RandomAccessFile instructorRAF = new RandomAccessFile(DATA_FILE, "rw")) {
            long posicion = instructorRAF.length();
            instructorRAF.seek(posicion);

            instructorRAF.writeInt(code);
            instructorRAF.writeChars(apprenticeName + " ".repeat(50 - apprenticeName.length()));
            instructorRAF.writeInt(apprenticeId);
            instructorRAF.writeChars(specialty + " ".repeat(40 - specialty.length()));
            instructorRAF.writeChars(instructorName + " ".repeat(50 - instructorName.length()));
            instructorRAF.writeChars(date + " ".repeat(32 - date.length()));

            INDEX_FILE.put(code, posicion);
            System.out.println("Instructor guardado en posición: " + posicion);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

}