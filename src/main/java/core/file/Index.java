package core.file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Index {

    public Index() {
    }

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

}
