package core.main;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Archivo binario de aprendices con registros de longitud fija. */
public final class ArchivoAprendices implements Closeable {
    private static final int NOMBRE = 100;
    private static final int CEDULA = 4;
    private static final int TELEFONO = 8;
    private static final int ESPECIALIDAD = 40;
    private static final int MAX_ESPECIALIDADES = 10;
    private static final int TAM_REGISTRO = (NOMBRE + CEDULA + TELEFONO) * 2 + 4
            + MAX_ESPECIALIDADES * (ESPECIALIDAD * 2 + 4);

    private final RandomAccessFile archivo;
    private final Map<String, Long> indice = new LinkedHashMap<>();

    public ArchivoAprendices(Path ruta) throws IOException {
        Path padre = ruta.toAbsolutePath().getParent();
        if (padre != null) Files.createDirectories(padre);
        archivo = new RandomAccessFile(ruta.toFile(), "rw");
        cargarIndice();
    }

    public synchronized void guardar(Aprendiz aprendiz) throws IOException, ValidacionException {
        validar(aprendiz);
        if (indice.containsKey(aprendiz.cedula()))
            throw new ValidacionException("Ya existe un aprendiz con la cédula " + aprendiz.cedula() + ".");
        long posicion = archivo.length();
        archivo.seek(posicion);
        escribir(aprendiz);
        indice.put(aprendiz.cedula(), posicion);
    }

    public synchronized List<Aprendiz> listar() throws IOException {
        List<Aprendiz> resultado = new ArrayList<>();
        for (long posicion : indice.values()) {
            archivo.seek(posicion);
            resultado.add(leer());
        }
        return resultado;
    }

    /**
     * Recorre todos los registros y sobrescribe únicamente el entero asociado a
     * cada especialidad. Nombre, cédula, teléfono y especialidades no cambian.
     *
     * @return cantidad total de contadores reiniciados
     */
    public synchronized int reiniciarContadoresPorEspecialidad() throws IOException {
        int reiniciados = 0;
        for (long posicion = 0; posicion < archivo.length(); posicion += TAM_REGISTRO) {
            long inicioEspecialidades = posicion + (NOMBRE + CEDULA + TELEFONO) * 2;
            archivo.seek(inicioEspecialidades);
            int cantidad = archivo.readInt();
            for (int i = 0; i < MAX_ESPECIALIDADES; i++) {
                archivo.skipBytes(ESPECIALIDAD * 2);
                if (i < cantidad) {
                    archivo.writeInt(0);
                    reiniciados++;
                } else {
                    archivo.skipBytes(4);
                }
            }
        }
        archivo.getFD().sync();
        return reiniciados;
    }

    private void cargarIndice() throws IOException {
        if (archivo.length() % TAM_REGISTRO != 0)
            throw new IOException("El archivo de aprendices contiene registros incompletos.");
        for (long posicion = 0; posicion < archivo.length(); posicion += TAM_REGISTRO) {
            archivo.seek(posicion + NOMBRE * 2L);
            indice.put(leerTexto(CEDULA), posicion);
        }
    }

    private void validar(Aprendiz aprendiz) throws ValidacionException {
        if (aprendiz == null || aprendiz.nombre() == null || aprendiz.nombre().isBlank()
                || aprendiz.cedula() == null || aprendiz.cedula().isBlank()
                || aprendiz.telefono() == null || aprendiz.telefono().isBlank()
                || aprendiz.sesionesPorEspecialidad().isEmpty())
            throw new ValidacionException("Todos los datos del aprendiz y sus especialidades son obligatorios.");
        InstructorValidador.validarCedula(aprendiz.cedula());
        if (aprendiz.sesionesPorEspecialidad().size() > MAX_ESPECIALIDADES)
            throw new ValidacionException("Un aprendiz puede registrar máximo 10 especialidades.");
        for (var entrada : aprendiz.sesionesPorEspecialidad().entrySet()) {
            if (entrada.getKey() == null || entrada.getKey().isBlank() || entrada.getValue() == null
                    || entrada.getValue() < 0 || entrada.getValue() > 4)
                throw new ValidacionException("Cada especialidad debe tener un nombre y entre 0 y 4 sesiones.");
        }
    }

    private void escribir(Aprendiz aprendiz) throws IOException {
        escribirTexto(aprendiz.nombre(), NOMBRE);
        escribirTexto(aprendiz.cedula(), CEDULA);
        escribirTexto(aprendiz.telefono(), TELEFONO);
        archivo.writeInt(aprendiz.sesionesPorEspecialidad().size());
        int escritas = 0;
        for (var entrada : aprendiz.sesionesPorEspecialidad().entrySet()) {
            escribirTexto(entrada.getKey(), ESPECIALIDAD);
            archivo.writeInt(entrada.getValue());
            escritas++;
        }
        while (escritas++ < MAX_ESPECIALIDADES) {
            escribirTexto("", ESPECIALIDAD);
            archivo.writeInt(0);
        }
        archivo.getFD().sync();
    }

    private Aprendiz leer() throws IOException {
        String nombre = leerTexto(NOMBRE);
        String cedula = leerTexto(CEDULA);
        String telefono = leerTexto(TELEFONO);
        int cantidad = archivo.readInt();
        Map<String, Integer> especialidades = new LinkedHashMap<>();
        for (int i = 0; i < MAX_ESPECIALIDADES; i++) {
            String especialidad = leerTexto(ESPECIALIDAD);
            int sesiones = archivo.readInt();
            if (i < cantidad) especialidades.put(especialidad, sesiones);
        }
        return new Aprendiz(nombre, cedula, telefono, especialidades);
    }

    private void escribirTexto(String valor, int longitud) throws IOException {
        String texto = valor.trim();
        if (texto.length() > longitud) texto = texto.substring(0, longitud);
        archivo.writeChars(texto);
        for (int i = texto.length(); i < longitud; i++) archivo.writeChar('\0');
    }

    private String leerTexto(int longitud) throws IOException {
        StringBuilder texto = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            char caracter = archivo.readChar();
            if (caracter != '\0') texto.append(caracter);
        }
        return texto.toString();
    }

    @Override public void close() throws IOException { archivo.close(); }
}
