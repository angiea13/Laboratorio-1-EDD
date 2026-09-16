package core.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class EliminarSesion extends EliminarRegistro{

    public EliminarSesion() {
        super("Eliminar sesión", "Código de la sesión");

    }

    @Override
    public AnchorPane construirPantalla() {
        formatoBase();

        AnchorPane.setLeftAnchor(ventanaNotificacion, 297.0);
        AnchorPane.setTopAnchor(ventanaNotificacion, 104.0);

        Label desc = new Label("Ingrese el código de la sesión para eliminarla:");
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
