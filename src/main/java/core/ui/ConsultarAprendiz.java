package core.ui;

import javafx.scene.layout.AnchorPane;

public class ConsultarAprendiz extends ConsultarRegistro {

    public ConsultarAprendiz() {
        super("Consulta de aprendices", 10, new String[]{"Nombre", "Cédula", "Especialidades", "Teléfono", "# Sesiones"});
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