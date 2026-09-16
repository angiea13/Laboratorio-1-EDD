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
        Total: 154 bytes
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
    public boolean addInstructor(int id, String name, String specialty, long phone, short nOfSessions, HashMap<Integer, Long> INDEX_FILE, String DATA_FILE) {
        if (!validInstructor(id, name, specialty, phone, nOfSessions)) return false;
        if (INDEX_FILE.containsKey(id)) {
            System.out.println("Error: la cédula del instructor ya existe en los registros.");
            return false;
        }
        try (RandomAccessFile instructorRAF = new RandomAccessFile(DATA_FILE, "rw")) {
            if (instructorRAF.length() % INSTRUCTOR_RECORD_SIZE != 0) {
                System.out.println("Error: archivo de instructores con registros errados.");
                return false;
            }
            long posicion = instructorRAF.length();
            instructorRAF.seek(posicion);

            writeInstructor(instructorRAF, id, name.trim(), specialty.trim(), phone, nOfSessions);

            INDEX_FILE.put(id, posicion);
            System.out.println("Instructor guardado en posición: " + posicion);
            return true;
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }

    // Valida todos los campos antes de escribir el registro.
    private boolean validInstructor(int id, String name, String specialty, long phone, short sessions) {
        if (id <= 0) {
            System.out.println("Error: la cédula debe ser un número positivo.");
            return false;
        }
        if (phone < 1000000L || phone > 9999999999L) {
            System.out.println("Error: el teléfono debe contener entre 7 y 10 dígitos.");
            return false;
        }
        if (name == null || name.trim().isEmpty() || name.trim().length() > 50) {
            System.out.println("Error: el nombre debe tener entre 1 y 50 caracteres.");
            return false;
        }
        if (specialty == null || specialty.trim().isEmpty() || specialty.trim().length() > 20) {
            System.out.println("Error: la especialidad debe tener entre 1 y 20 caracteres.");
            return false;
        }
        if (sessions < 0 || sessions > 15) {
            System.out.println("Error: las sesiones deben estar entre 0 y 15.");
            return false;
        }
        return true;
    }

    // Longitud fija: 4 + 100 + 40 + 8 + 2 = 154 bytes.
    private void writeInstructor(RandomAccessFile file, int id, String name, String specialty,
                                 long phone, short sessions) throws IOException {
        file.writeInt(id);
        for (int i = 0; i < 50; i++) {
            if (i < name.length()) file.writeChar(name.charAt(i));
            else file.writeChar(' ');
        }
        for (int i = 0; i < 20; i++) {
            if (i < specialty.length()) file.writeChar(specialty.charAt(i));
            else file.writeChar(' ');
        }
        file.writeLong(phone);
        file.writeShort(sessions);
    }

    private String readText(RandomAccessFile file, int length) throws IOException {
        String text = "";
        for (int i = 0; i < length; i++) text = text + file.readChar();
        return text.trim();
    }

    // El índice debe señalar un registro completo de la cédula solicitada.
    private void locateInstructor(RandomAccessFile file, int id, long position) throws IOException {
        if (file.length() % INSTRUCTOR_RECORD_SIZE != 0 || position < 0
                || position % INSTRUCTOR_RECORD_SIZE != 0
                || position > file.length() - INSTRUCTOR_RECORD_SIZE) {
            throw new IOException("Archivo o índice de instructores incorrecto.");
        }
        file.seek(position);
        if (file.readInt() != id) throw new IOException("El índice no corresponde al instructor.");
        file.seek(position);
    }

    // Orden del arreglo: cédula, nombre, especialidad, teléfono, sesiones.
    public String[] getInstructor(int id, HashMap<Integer, Long> index, String dataFile) {
        if (!index.containsKey(id)) {
            System.out.println("No existe un instructor con esa cédula.");
            return null;
        }
        try (RandomAccessFile file = new RandomAccessFile(dataFile, "r")) {
            locateInstructor(file, id, index.get(id));
            String[] record = new String[5];
            record[0] = "" + file.readInt();
            record[1] = readText(file, 50);
            record[2] = readText(file, 20);
            record[3] = "" + file.readLong();
            record[4] = "" + file.readShort();
            return record;
        } catch (IOException e) {
            System.out.println("Error al consultar instructor: " + e.getMessage());
            return null;
        }
    }

    // Conserva la cédula y la posición; reescribe el registro de longitud fija.
    public boolean updateInstructor(int id, String name, String specialty, long phone, short sessions,
                                 HashMap<Integer, Long> index, String dataFile) {
        if (!validInstructor(id, name, specialty, phone, sessions)) return false;
        if (!index.containsKey(id)) {
            System.out.println("No existe un instructor con esa cédula.");
            return false;
        }
        if (!new File(dataFile).exists()) {
            System.out.println("Error: archivo de instructores no encontrado.");
            return false;
        }
        try (RandomAccessFile file = new RandomAccessFile(dataFile, "rw")) {
            locateInstructor(file, id, index.get(id));
            writeInstructor(file, id, name.trim(), specialty.trim(), phone, sessions);
            System.out.println("Instructor modificado correctamente.");
            return true;
        } catch (IOException e) {
            System.out.println("Error al modificar instructor: " + e.getMessage());
            return false;
        }
    }

    // Baja lógica: una cédula negativa marca el registro eliminado, sin desplazar otros.
    public boolean deleteInstructor(int id, HashMap<Integer, Long> index, String dataFile) {
        if (!index.containsKey(id)) {
            System.out.println("No existe un instructor con esa cédula.");
            return false;
        }
        if (!new File(dataFile).exists()) {
            System.out.println("Error: archivo de instructores no encontrado.");
            return false;
        }
        try (RandomAccessFile file = new RandomAccessFile(dataFile, "rw")) {
            locateInstructor(file, id, index.get(id));
            file.writeInt(-id);
            index.remove(id);
            System.out.println("Instructor eliminado correctamente.");
            return true;
        } catch (IOException e) {
            System.out.println("Error al eliminar instructor: " + e.getMessage());
            return false;
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
