package core.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class EliminarAprendiz extends EliminarRegistro {

    public EliminarAprendiz() {
        super("Eliminar aprendiz", "Cédula del aprendiz");

    }

    @Override
    public AnchorPane construirPantalla() {
        formatoBase();

        AnchorPane.setLeftAnchor(ventanaNotificacion, 297.0);
        AnchorPane.setTopAnchor(ventanaNotificacion, 104.0);

        Label desc = new Label("Ingrese la cédula del aprendiz para eliminarlo:");
        desc.setTextFill(Color.web("#545454"));
        desc.setFont(Font.font(Fuente.REGULAR.getName(), 30));

        AnchorPane.setLeftAnchor(desc, 371.0);
        AnchorPane.setTopAnchor(desc, 330.0);

        pantalla.getChildren().addAll(
            ventanaNotificacion,
            desc,
            botonAtras,
            botonMenu
        );
        
        return pantalla;
    }
    
}
