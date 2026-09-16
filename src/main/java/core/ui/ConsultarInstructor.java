package core.ui;

import javafx.scene.layout.AnchorPane;

public class ConsultarInstructor extends ConsultarRegistro {

    public ConsultarInstructor() {
        super("Consulta de instructores", 20, new String[]{"Nombre", "Cédula", "Especialidades", "Teléfono", "# Sesiones"});
    }

    @Override
    public AnchorPane construirPantalla() {
        formatoBase();

        pantalla.getChildren().addAll(
            titulo,
            linea,
            botonAtras,
            botonMenu,
            campoBuscar,
            campoFiltro,
            scroll
        );

        return pantalla;
    }
}