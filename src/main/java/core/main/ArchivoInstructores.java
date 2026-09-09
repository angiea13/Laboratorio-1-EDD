package core.main;

import core.file.Management;
import java.io.*;
import java.util.HashMap;

/** Operaciones de instructores para conectar después con el menú gráfico. */
public class ArchivoInstructores {
    private String dataFile;
    private String indexFile;
    private HashMap<Integer, Long> index = new HashMap<>();
    private Management management = new Management();

    public ArchivoInstructores(String dataFile, String indexFile) throws IOException {
        if (new File(dataFile).getCanonicalPath().equals(new File(indexFile).getCanonicalPath())) {
            throw new IOException("El archivo de datos y el índice deben ser diferentes.");
        }
        this.dataFile = dataFile;
        this.indexFile = indexFile;
        // Reconstruye al abrir para recuperar también un índice perdido o desactualizado.
        // Después, cada consulta usa el HashMap y seek(), sin recorrer todo el archivo.
        if (new File(dataFile).exists()) {
            try (RandomAccessFile file = new RandomAccessFile(dataFile, "r")) {
                if (file.length() % 154 != 0) throw new IOException("Archivo de instructores con registros errados.");
                while (file.getFilePointer() < file.length()) {
                    long position = file.getFilePointer();
                    int id = file.readInt();
                    if (id == 0 || index.containsKey(id)) throw new IOException("Cédula incorrecta o duplicada en archivo.");
                    if (id > 0) index.put(id, position);
                    file.seek(position + 154);
                }
            }
        } else if (new File(indexFile).exists() && new File(indexFile).length() > 0) {
            throw new IOException("Archivo de instructores no encontrado; existe un índice con datos.");
        }
        saveIndex();
    }

    private void saveIndex() throws IOException {
        try (DataOutputStream file = new DataOutputStream(new FileOutputStream(indexFile))) {
            for (Integer id : index.keySet()) {
                file.writeInt(id);
                file.writeLong(index.get(id));
            }
        }
    }

    // Solo acepta dígitos. La cédula mantiene el tipo int del proyecto original.
    private boolean numeric(String value) {
        if (value == null || value.trim().isEmpty()) return false;
        value = value.trim();
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) < '0' || value.charAt(i) > '9') return false;
        }
        return true;
    }

    public void guardar(String cedula, String nombre, String especialidad, String telefono,
                        short sesiones, boolean modificar) throws IOException {
        if (!numeric(cedula) || !numeric(telefono)) {
            System.out.println("Error: cédula y teléfono son obligatorios y deben contener solo dígitos.");
            return;
        }
        try {
            int id = Integer.parseInt(cedula.trim());
            long phone = Long.parseLong(telefono.trim());
            if (modificar) management.updateInstructor(id, nombre, especialidad, phone, sesiones, index, dataFile);
            else management.addInstructor(id, nombre, especialidad, phone, sesiones, index, dataFile);
            saveIndex();
        } catch (NumberFormatException e) {
            System.out.println("Error: cédula o teléfono fuera del rango numérico admitido.");
        }
    }

    public String[] consultar(int cedula) {
        return management.getInstructor(cedula, index, dataFile);
    }

    public void eliminar(int cedula) throws IOException {
        management.deleteInstructor(cedula, index, dataFile);
        saveIndex();
    }
}
