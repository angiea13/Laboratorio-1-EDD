package core.main;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/** Aplica las reglas del CRUD antes de modificar el archivo. */
public final class InstructorServicio {
    private final ArchivoInstructores archivo;
    public InstructorServicio(ArchivoInstructores archivo) { this.archivo = archivo; }

    public void crear(Instructor instructor) throws IOException, ValidacionException {
        Instructor valido = InstructorValidador.validar(instructor);
        if (archivo.existe(valido.cedula()))
            throw new ValidacionException("Ya existe un instructor con la cédula " + valido.cedula() + ".");
        archivo.agregar(valido);
    }

    public Optional<Instructor> consultar(String cedula) throws IOException, ValidacionException {
        return archivo.buscar(InstructorValidador.validarCedula(cedula));
    }

    public void modificar(Instructor instructor) throws IOException, ValidacionException {
        Instructor valido = InstructorValidador.validar(instructor);
        if (!archivo.existe(valido.cedula()))
            throw new ValidacionException("No existe un instructor con la cédula " + valido.cedula() + ".");
        archivo.actualizar(valido);
    }

    public void eliminar(String cedula) throws IOException, ValidacionException {
        String valida = InstructorValidador.validarCedula(cedula);
        if (!archivo.eliminar(valida))
            throw new ValidacionException("No existe un instructor con la cédula " + valida + ".");
    }

    public List<Instructor> listar() throws IOException { return archivo.listar(); }
}
