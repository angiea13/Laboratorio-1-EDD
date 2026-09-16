package core.main;

/** Catálogo inicial del enunciado. Agregar aquí otras especialidades aprobadas. */
public final class CatalogoEspecialidades {
    private static final String[] NOMBRES = {"Música", "Pintura", "Escritura", "Baile"};
    private CatalogoEspecialidades() { }

    public static String validar(String nombre) throws ValidacionException {
        String limpio = ValidacionDatos.texto(nombre, "especialidad", 40);
        if (limpio.equalsIgnoreCase("musica")) limpio = "Música";
        for (int i = 0; i < NOMBRES.length; i++) {
            if (NOMBRES[i].equalsIgnoreCase(limpio)) return NOMBRES[i];
        }
        throw new ValidacionException("Especialidad no registrada en el catálogo: " + limpio
                + ". Opciones: Música, Pintura, Escritura, Baile.");
    }
}
