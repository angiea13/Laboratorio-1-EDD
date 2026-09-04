package core.main;

import java.io.IOException;

/** Operaciones de negocio sobre el archivo de aprendices. */
public final class AprendizServicio {
    private final ArchivoAprendices archivo;

    public AprendizServicio(ArchivoAprendices archivo) {
        this.archivo = archivo;
    }

    public int reiniciarContadoresMensuales() throws IOException {
        return archivo.reiniciarContadoresPorEspecialidad();
    }
}
