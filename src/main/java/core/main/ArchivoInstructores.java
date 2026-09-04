package core.main;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Archivo indexado binario con registros de longitud fija y borrado lógico. */
public final class ArchivoInstructores implements Closeable {
    private static final int NOMBRE = 80, CEDULA = 24, ESPECIALIDAD = 60, TELEFONO = 20;
    private static final int TAM_REGISTRO = 1 + (NOMBRE + CEDULA + ESPECIALIDAD + TELEFONO) * 2 + 4;
    private final RandomAccessFile archivo;
    private final Map<String, Long> indice = new LinkedHashMap<>();

    public ArchivoInstructores(Path ruta) throws IOException {
        Path padre = ruta.toAbsolutePath().getParent();
        if (padre != null) Files.createDirectories(padre);
        archivo = new RandomAccessFile(ruta.toFile(), "rw");
        cargarIndice();
    }

    public synchronized boolean existe(String cedula) { return indice.containsKey(cedula); }

    public synchronized void agregar(Instructor instructor) throws IOException {
        long posicion = archivo.length();
        archivo.seek(posicion);
        escribirRegistro(instructor, true);
        indice.put(instructor.cedula(), posicion);
    }

    public synchronized Optional<Instructor> buscar(String cedula) throws IOException {
        Long posicion = indice.get(cedula);
        if (posicion == null) return Optional.empty();
        archivo.seek(posicion);
        return Optional.of(leerRegistro());
    }

    public synchronized void actualizar(Instructor instructor) throws IOException {
        archivo.seek(indice.get(instructor.cedula()));
        escribirRegistro(instructor, true);
    }

    public synchronized boolean eliminar(String cedula) throws IOException {
        Long posicion = indice.remove(cedula);
        if (posicion == null) return false;
        archivo.seek(posicion);
        archivo.writeBoolean(false);
        archivo.getFD().sync();
        return true;
    }

    public synchronized List<Instructor> listar() throws IOException {
        List<Instructor> resultado = new ArrayList<>();
        for (String cedula : indice.keySet()) resultado.add(buscar(cedula).orElseThrow());
        return resultado;
    }

    private void cargarIndice() throws IOException {
        if (archivo.length() % TAM_REGISTRO != 0)
            throw new IOException("El archivo de instructores contiene registros incompletos.");
        for (long posicion = 0; posicion < archivo.length(); posicion += TAM_REGISTRO) {
            archivo.seek(posicion);
            boolean activo = archivo.readBoolean();
            Instructor instructor = leerCampos();
            if (activo) indice.put(instructor.cedula(), posicion);
        }
    }

    private Instructor leerRegistro() throws IOException {
        if (!archivo.readBoolean()) throw new EOFException("El registro fue eliminado.");
        return leerCampos();
    }

    private Instructor leerCampos() throws IOException {
        return new Instructor(leerTexto(NOMBRE), leerTexto(CEDULA), leerTexto(ESPECIALIDAD), leerTexto(TELEFONO), archivo.readInt());
    }

    private void escribirRegistro(Instructor instructor, boolean activo) throws IOException {
        archivo.writeBoolean(activo);
        escribirTexto(instructor.nombre(), NOMBRE); escribirTexto(instructor.cedula(), CEDULA);
        escribirTexto(instructor.especialidad(), ESPECIALIDAD); escribirTexto(instructor.telefono(), TELEFONO);
        archivo.writeInt(instructor.sesionesRealizadas());
        archivo.getFD().sync();
    }

    private void escribirTexto(String texto, int longitud) throws IOException {
        if (texto.length() > longitud) texto = texto.substring(0, longitud);
        archivo.writeChars(texto);
        for (int i = texto.length(); i < longitud; i++) archivo.writeChar('\0');
    }

    private String leerTexto(int longitud) throws IOException {
        StringBuilder texto = new StringBuilder(longitud);
        for (int i = 0; i < longitud; i++) {
            char caracter = archivo.readChar();
            if (caracter != '\0') texto.append(caracter);
        }
        return texto.toString();
    }

    @Override public void close() throws IOException { archivo.close(); }
}
