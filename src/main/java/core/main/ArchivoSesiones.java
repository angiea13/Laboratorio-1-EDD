package core.main;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/** Archivo de sesiones con índices secundarios por fecha y por instructor. */
public final class ArchivoSesiones implements Closeable {
    private static final int CODIGO = 30, NOMBRE = 80, CEDULA = 24, ESPECIALIDAD = 60;
    private static final int TAM_REGISTRO = (CODIGO + NOMBRE + CEDULA + ESPECIALIDAD + NOMBRE + CEDULA) * 2 + 8;
    private final RandomAccessFile archivo;
    private final Map<String, Long> indiceCodigo = new LinkedHashMap<>();
    private final Map<LocalDate, Set<Long>> indiceFecha = new LinkedHashMap<>();
    private final Map<String, Set<Long>> indiceInstructor = new LinkedHashMap<>();

    public ArchivoSesiones(Path ruta) throws IOException {
        Path padre = ruta.toAbsolutePath().getParent();
        if (padre != null) Files.createDirectories(padre);
        archivo = new RandomAccessFile(ruta.toFile(), "rw");
        cargarIndices();
    }

    public synchronized void agregar(Sesion sesion) throws IOException, ValidacionException {
        validar(sesion);
        if (indiceCodigo.containsKey(sesion.codigo().trim()))
            throw new ValidacionException("Ya existe una sesión con el código " + sesion.codigo().trim() + ".");
        long posicion = archivo.length();
        archivo.seek(posicion);
        escribir(sesion);
        indexar(sesion, posicion);
    }

    /** Obtiene las sesiones usando exclusivamente el índice de fecha. */
    public synchronized List<Sesion> buscarPorFecha(LocalDate fecha) throws IOException {
        return leerPosiciones(indiceFecha.getOrDefault(fecha, Set.of()));
    }

    /** Obtiene las sesiones usando exclusivamente el índice de instructor. */
    public synchronized List<Sesion> buscarPorInstructor(String cedulaInstructor) throws IOException {
        return leerPosiciones(indiceInstructor.getOrDefault(normalizar(cedulaInstructor), Set.of()));
    }

    private List<Sesion> leerPosiciones(Set<Long> posiciones) throws IOException {
        List<Sesion> resultado = new ArrayList<>();
        for (long posicion : posiciones) {
            archivo.seek(posicion);
            resultado.add(leer());
        }
        return resultado;
    }

    private void cargarIndices() throws IOException {
        if (archivo.length() % TAM_REGISTRO != 0)
            throw new IOException("El archivo de sesiones contiene registros incompletos.");
        for (long posicion = 0; posicion < archivo.length(); posicion += TAM_REGISTRO) {
            archivo.seek(posicion);
            indexar(leer(), posicion);
        }
    }

    private void indexar(Sesion sesion, long posicion) {
        indiceCodigo.put(sesion.codigo(), posicion);
        indiceFecha.computeIfAbsent(sesion.fecha(), clave -> new LinkedHashSet<>()).add(posicion);
        indiceInstructor.computeIfAbsent(normalizar(sesion.cedulaInstructor()), clave -> new LinkedHashSet<>()).add(posicion);
    }

    private void validar(Sesion sesion) throws ValidacionException {
        if (sesion == null || vacio(sesion.codigo()) || vacio(sesion.nombreAprendiz())
                || vacio(sesion.cedulaAprendiz()) || vacio(sesion.especialidad())
                || vacio(sesion.nombreInstructor()) || vacio(sesion.cedulaInstructor()) || sesion.fecha() == null)
            throw new ValidacionException("Todos los campos de la sesión son obligatorios.");
    }

    private void escribir(Sesion s) throws IOException {
        escribirTexto(s.codigo(), CODIGO); escribirTexto(s.nombreAprendiz(), NOMBRE);
        escribirTexto(s.cedulaAprendiz(), CEDULA); escribirTexto(s.especialidad(), ESPECIALIDAD);
        escribirTexto(s.nombreInstructor(), NOMBRE); escribirTexto(s.cedulaInstructor(), CEDULA);
        archivo.writeLong(s.fecha().toEpochDay());
        archivo.getFD().sync();
    }

    private Sesion leer() throws IOException {
        return new Sesion(leerTexto(CODIGO), leerTexto(NOMBRE), leerTexto(CEDULA), leerTexto(ESPECIALIDAD),
                leerTexto(NOMBRE), leerTexto(CEDULA), LocalDate.ofEpochDay(archivo.readLong()));
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
            char c = archivo.readChar();
            if (c != '\0') texto.append(c);
        }
        return texto.toString();
    }

    private boolean vacio(String valor) { return valor == null || valor.isBlank(); }
    private String normalizar(String valor) { return valor.trim().toLowerCase(Locale.ROOT); }
    @Override public void close() throws IOException { archivo.close(); }
}
