package core.main;

import core.file.Management;
import java.io.*;
import java.util.HashMap;

/** Operaciones de instructores para conectar después con el menú gráfico. */
public class ArchivoInstructoresIndexado {
    /** Importa una sola vez sin modificar ni sobrescribir el archivo anterior. */
    public static ArchivoInstructoresIndexado abrirParaMenu(java.nio.file.Path carpeta) throws IOException {
        File directorio = carpeta.toFile();
        if (!directorio.isDirectory() && !directorio.mkdirs()) throw new IOException("No se pudo crear la carpeta de datos.");
        File datos = new File(directorio, "instructores-indexados.dat");
        File indice = new File(directorio, "instructores-indexados.idx");
        File anterior = new File(directorio, "instructores.dat");
        if (!datos.exists()) {
            if (indice.exists() && indice.length() > 0) throw new IOException("Falta el archivo de instructores indexados.");
            // Los temporales evitan dejar una importación parcial como archivo definitivo.
            File temporal = File.createTempFile("instructores-", ".dat", directorio);
            File temporalIndice = File.createTempFile("instructores-", ".idx", directorio);
            try {
                ArchivoInstructoresIndexado destino = new ArchivoInstructoresIndexado(temporal.getPath(), temporalIndice.getPath());
                if (anterior.exists()) {
                    try (ArchivoInstructores origen = new ArchivoInstructores(anterior.toPath())) {
                        for (Instructor instructor : origen.listar()) {
                            // No convertir identificadores o teléfonos perdiendo ceros ni truncar campos.
                            try {
                                int id = leerCedula(instructor.cedula());
                                long telefono = Long.parseLong(instructor.telefono());
                                if (!instructor.cedula().equals("" + id) || !instructor.telefono().equals("" + telefono))
                                    throw new NumberFormatException();
                            } catch (ValidacionException | NumberFormatException e) {
                                throw new IOException("El instructor " + instructor.cedula() + " no es compatible con el formato numérico nuevo. El original se conserva.");
                            }
                            int sesiones = instructor.sesionesRealizadas();
                            if (sesiones < 0 || sesiones > 15 || !destino.guardar(instructor.cedula(), instructor.nombre(),
                                    instructor.especialidad(), instructor.telefono(), (short) sesiones, false))
                                throw new IOException("No se pudo importar el instructor " + instructor.cedula() + ". El original se conserva.");
                        }
                    }
                }
                if (!temporal.renameTo(datos)) throw new IOException("No se pudo guardar la copia de instructores.");
            } finally {
                if (temporal.exists()) temporal.delete();
                temporalIndice.delete();
            }
        }
        return new ArchivoInstructoresIndexado(datos.getPath(), indice.getPath());
    }

    private String dataFile;
    private String indexFile;
    private HashMap<Integer, Long> index = new HashMap<>();
    private Management management = new Management();
    // Segundo índice: especialidad -> cédulas y posiciones de sus instructores.
    private HashMap<String, HashMap<Integer, Long>> especialidades = new HashMap<>();

    public ArchivoInstructoresIndexado(String dataFile, String indexFile) throws IOException {
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
        for (Integer id : index.keySet()) agregarEspecialidad(consultar(id));
    }

    private void saveIndex() throws IOException {
        try (DataOutputStream file = new DataOutputStream(new FileOutputStream(indexFile))) {
            for (Integer id : index.keySet()) {
                file.writeInt(id);
                file.writeLong(index.get(id));
            }
        }
    }

    public boolean guardar(String cedula, String nombre, String especialidad, String telefono,
                        short sesiones, boolean modificar) throws IOException {
        try {
            Instructor valido = InstructorValidador.validarIndexado(
                    new Instructor(nombre, cedula, especialidad, telefono, sesiones));
            int id = Integer.parseInt(valido.cedula());
            long phone = Long.parseLong(valido.telefono());
            nombre = valido.nombre();
            especialidad = valido.especialidad();
            boolean guardado;
            String[] anterior = null;
            if (index.containsKey(id)) anterior = consultar(id);
            if (modificar) guardado = management.updateInstructor(id, nombre, especialidad, phone, sesiones, index, dataFile);
            else guardado = management.addInstructor(id, nombre, especialidad, phone, sesiones, index, dataFile);
            if (!guardado) return false;
            quitarEspecialidad(anterior);
            agregarEspecialidad(consultar(id));
            saveIndex();
            return true;
        } catch (ValidacionException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public String[] consultar(int cedula) {
        return management.getInstructor(cedula, index, dataFile);
    }

    public boolean eliminar(int cedula) throws IOException {
        String[] anterior = null;
        if (index.containsKey(cedula)) anterior = consultar(cedula);
        if (!management.deleteInstructor(cedula, index, dataFile)) return false;
        quitarEspecialidad(anterior);
        saveIndex();
        return true;
    }

    public static int leerCedula(String texto) throws ValidacionException {
        if (texto == null || texto.trim().isEmpty()) throw new ValidacionException("La cédula es obligatoria.");
        texto = texto.trim();
        for (int i = 0; i < texto.length(); i++) {
            if (texto.charAt(i) < '0' || texto.charAt(i) > '9')
                throw new ValidacionException("La cédula debe contener solo dígitos.");
        }
        try {
            int cedula = Integer.parseInt(texto);
            if (cedula <= 0) throw new NumberFormatException();
            return cedula;
        } catch (NumberFormatException e) {
            throw new ValidacionException("La cédula debe estar entre 1 y 2147483647.");
        }
    }

    private String clave(String especialidad) {
        return especialidad.trim().toLowerCase(java.util.Locale.ROOT);
    }

    private void agregarEspecialidad(String[] registro) throws IOException {
        if (registro == null) throw new IOException("No se pudo leer el instructor del índice.");
        int id = Integer.parseInt(registro[0]);
        String especialidad = clave(registro[2]);
        if (!especialidades.containsKey(especialidad)) especialidades.put(especialidad, new HashMap<>());
        especialidades.get(especialidad).put(id, index.get(id));
    }

    private void quitarEspecialidad(String[] registro) {
        if (registro == null) return;
        String especialidad = clave(registro[2]);
        especialidades.get(especialidad).remove(Integer.parseInt(registro[0]));
        if (especialidades.get(especialidad).isEmpty()) especialidades.remove(especialidad);
    }

    public Instructor[] listar() throws IOException {
        Instructor[] resultado = new Instructor[index.size()];
        int i = 0;
        for (Integer id : index.keySet()) resultado[i++] = leerInstructor(id);
        return resultado;
    }

    /** Modifica únicamente los dos bytes del contador, mediante el índice. */
    public void actualizarContador(int cedula, int cantidad) throws IOException {
        if (cantidad < 0 || cantidad > 15) throw new IOException("Contador fuera del rango 0-15.");
        Long posicion = index.get(cedula);
        if (posicion == null) throw new IOException("El instructor no existe.");
        try (RandomAccessFile archivo = new RandomAccessFile(dataFile, "rw")) {
            archivo.seek(posicion + 152);
            archivo.writeShort(cantidad);
            archivo.getFD().sync();
        }
    }

    public int reiniciarContadoresMensuales() throws IOException {
        for (Integer cedula : index.keySet()) actualizarContador(cedula, 0);
        return index.size();
    }

    private Instructor leerInstructor(int id) throws IOException {
        String[] r = consultar(id);
        if (r == null) throw new IOException("No se pudo consultar el instructor " + id + ".");
        return new Instructor(r[1], r[0], r[2], r[3], Integer.parseInt(r[4]));
    }

    public Instructor[] disponibles(String especialidad) throws IOException {
        HashMap<Integer, Long> seleccion = especialidades.get(clave(especialidad));
        if (seleccion == null) return new Instructor[0];
        Instructor[] temporal = new Instructor[seleccion.size()];
        int cantidad = 0;
        for (Integer id : seleccion.keySet()) {
            Instructor instructor = leerInstructor(id);
            if (instructor.sesionesRealizadas() < 15) temporal[cantidad++] = instructor;
        }
        Instructor[] resultado = new Instructor[cantidad];
        for (int i = 0; i < cantidad; i++) resultado[i] = temporal[i];
        return resultado;
    }
}
