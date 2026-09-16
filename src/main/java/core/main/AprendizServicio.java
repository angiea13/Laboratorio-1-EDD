package core.main;

import java.io.IOException;

/** Operaciones de negocio sobre el archivo de aprendices. */
public final class AprendizServicio {
    private final ArchivoAprendices archivo;

    public AprendizServicio(ArchivoAprendices archivo) {
        this.archivo = archivo;
    }

    public void crear(Aprendiz aprendiz) throws IOException, ValidacionException {
        archivo.guardar(aprendiz);
    }

    public Aprendiz consultar(String cedula) throws IOException, ValidacionException {
        return archivo.buscar(cedula);
    }

    public void modificar(Aprendiz aprendiz) throws IOException, ValidacionException {
        archivo.actualizar(aprendiz);
    }

    public void eliminar(String cedula) throws IOException, ValidacionException {
        archivo.eliminar(cedula);
    }

    public java.util.List<Aprendiz> listar() throws IOException {
        return archivo.listar();
    }

    public int reiniciarContadoresMensuales() throws IOException {
        return archivo.reiniciarContadoresPorEspecialidad();
    }
}
