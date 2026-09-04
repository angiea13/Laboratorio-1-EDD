package core.file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class Index {

    public Index() {
    }

    /*
    Este método carga los índices a partir del archivo de índices del disco.
    Tiene como parámetros el nombre del archivo de índices (FILE_NAME), 
    y el HashMap en el que se guardarán en memoria (INDEX_FILE).

    Solo es realmente útil si ya existe el archivo de índices.
    */
    public void loadIndex(String FILE_NAME, HashMap<Integer, Long> INDEX_FILE) {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (DataInputStream inx = new DataInputStream(new FileInputStream(file))) {
            while (inx.available() > 0) {
                int id = inx.readInt();
                long pos = inx.readLong();
                INDEX_FILE.put(id, pos);
            }
            System.out.println("Índice cargado en memoria desde el disco.");
        } catch (IOException e) {
            System.err.println("Error al cargar el índice: " + e.getMessage());
        }
    }

    /*
    Este método guarda los índices a partir del HashMap en memoria.
    Tiene como parámetros el nombre del archivo de índices (FILE_NAME), 
    y el HashMap en donde los índices están guardados (INDEX_FILE).
    */
    public void saveIndex(String FILE_NAME, HashMap<Integer, Long> INDEX_FILE) {
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(FILE_NAME))) {
            for (Map.Entry<Integer, Long> entry : INDEX_FILE.entrySet()) {
                out.writeInt(entry.getKey());
                out.writeLong(entry.getValue());
            }
            System.out.println("Índice guardado en disco exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al guardar el índice: " + e.getMessage());
        }
    }

    public String[] getRecordApprentice(HashMap<Integer, Long> INDEX_FILE, int id, String DATA_FILE) {
        try (RandomAccessFile df = new RandomAccessFile(DATA_FILE, "r")) {
            String[] record = new String[5];
            long position = INDEX_FILE.get(id);
            df.seek(position); // Saltamos directo a la posición obtenida por el índice
            
            record[0] = "" + df.readInt();
            
            // Leer los 50 caracteres del nombre
            StringBuilder tempName = new StringBuilder();
            for (int i = 0; i < 49; i++) {
                tempName.append(df.readChar());
            }

            record[1] = tempName.toString().trim();
            record[2] = "" + df.readShort();
            record[3] = "" + df.readLong();
            record[4] = "" + df.readShort();

            return record;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
