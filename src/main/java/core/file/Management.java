package core.file;

import java.io.*;
import java.util.HashMap;

public class Management {

    /*
    Campos INSTRUCTOR:
        Nombre -> 50 chars (100 bytes)
        Cédula -> int (4 bytes)
        Especialidad -> 20 chars (40 bytes)
        Teléfono -> int (4 bytes)
        # Sesiones -> short (2 bytes)
        Total: 150 bytes
    */
    protected static final int INSTRUCTOR_RECORD_SIZE = 150;

    /*
    Campos APPRENTICE:
        Nombre -> 50 chars (100 bytes)
        Cédula -> int (4 bytes)
        # Especialidades Practicadas -> short (2 bytes)
        Teléfono -> int (4 bytes)
        # Sesiones -> short (2 bytes)
        Total: 112 bytes
    */
    protected static final int APPRENTICE_RECORD_SIZE = 112;

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
    protected static final int SESSION_RECORD_SIZE = 280;

    public Management() {
    }

    public void addApprentice(int id, String name, short nOfSpecialties, long phone, short nOfSessions, HashMap<Integer, Long> INDEX_FILE, String DATA_FILE) {
        if (INDEX_FILE.containsKey(id)) {
            System.out.println("Error: la cédula ya existe.");
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

    public void addInstructor(int id, String name, String specialty, long phone, short nOfSessions, HashMap<Integer, Long> INDEX_FILE, String DATA_FILE) {
        if (INDEX_FILE.containsKey(id)) {
            System.out.println("Error: la cédula ya existe.");
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