package core.main;

/** Validaciones sencillas compartidas: ciclos y comparaciones de caracteres. */
public final class ValidacionDatos {
    private ValidacionDatos() { }

    public static String texto(String valor, String campo, int maximo) throws ValidacionException {
        if (valor == null || valor.trim().isEmpty())
            throw new ValidacionException("El campo " + campo + " es obligatorio.");
        String limpio = valor.trim();
        if (limpio.length() > maximo)
            throw new ValidacionException("El campo " + campo + " admite máximo " + maximo + " caracteres.");
        for (int i = 0; i < limpio.length(); i++) {
            if (limpio.charAt(i) < ' ')
                throw new ValidacionException("El campo " + campo + " contiene caracteres de control.");
        }
        return limpio;
    }

    public static String digitos(String valor, String campo, int minimo, int maximo) throws ValidacionException {
        String limpio = texto(valor, campo, maximo);
        boolean positivo = false;
        for (int i = 0; i < limpio.length(); i++) {
            char c = limpio.charAt(i);
            if (c < '0' || c > '9') throw new ValidacionException("El campo " + campo + " debe contener solo dígitos.");
            if (c != '0') positivo = true;
        }
        if (limpio.length() < minimo || !positivo)
            throw new ValidacionException("El campo " + campo + " debe contener entre " + minimo + " y " + maximo + " dígitos y ser mayor que cero.");
        return limpio;
    }
}
