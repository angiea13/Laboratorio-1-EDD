package core.main;

/** Error de datos que puede mostrarse directamente al administrador. */
public class ValidacionException extends Exception {
    public ValidacionException(String mensaje) { super(mensaje); }
}
