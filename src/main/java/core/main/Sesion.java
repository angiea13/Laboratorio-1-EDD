package core.main;

import java.time.LocalDate;

/** Datos necesarios para consultar una sesión programada. */
public record Sesion(String codigo, String nombreAprendiz, String cedulaAprendiz,
                     String especialidad, String nombreInstructor,
                     String cedulaInstructor, LocalDate fecha) {
}
