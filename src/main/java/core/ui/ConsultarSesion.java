package core.ui;

import javafx.scene.layout.AnchorPane;

public class ConsultarSesion extends ConsultarRegistro {

    public ConsultarSesion() {
        super("Consulta de sesiones", 20, new String[]{"Código", "Aprendiz", "Especialidad", "Instructor", "Fecha"});
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