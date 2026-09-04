package core.main;

/** Datos persistidos de un instructor de la academia. */
public record Instructor(String nombre, String cedula, String especialidad,
                         String telefono, int sesionesRealizadas) {
}
