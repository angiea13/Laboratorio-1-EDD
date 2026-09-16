package core.ui;


import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class AgregarAprendiz extends AgregarRegistro{

    public TextField campoNombre;
    public TextField campoCedula;
    public TextField campoEspecialidades;

    public AgregarAprendiz() {
        super("Agregar aprendiz", "Registrar");
        campoNombre = new TextField();
        campoCedula = new TextField();
        campoEspecialidades = new TextField();
    }

    @Override 
    public AnchorPane construirPantalla() {
        formatoBase();
        
        AnchorPane.setLeftAnchor(botonAtras, 575.0);
        AnchorPane.setTopAnchor(botonAtras, 53.0);

        StackPane nombre = crearCampo("Nombre", campoNombre, 94.0, 273.0);
        StackPane cedula = crearCampo("Cédula", campoCedula, 94.0, 370.0);
        StackPane especialidades = crearCampo("Especialidades", campoEspecialidades, 94.0, 468.0);

        pantalla.getChildren().addAll(
            esquinaIzq, 
            esquinaDer,
            textoAgregar,
            linea,
            botonConfirmar,
            botonMenu,
            botonAtras,
            nombre,
            cedula,
            especialidades
        );
        
        return pantalla;

    }
}
