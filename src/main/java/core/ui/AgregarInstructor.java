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

public class AgregarInstructor extends AgregarRegistro{

    public TextField campoNombre;
    public TextField campoCedula;
    public TextField campoEspecialidad;
    public TextField campoTelefono;

    public AgregarInstructor() {
        super("Agregar instructor", "Registrar");
        campoNombre = new TextField();
        campoCedula = new TextField();
        campoEspecialidad = new TextField();
        campoTelefono = new TextField();
    }

    @Override 
    public AnchorPane construirPantalla() {
        formatoBase();
        
        AnchorPane.setLeftAnchor(botonAtras, 610.0);
        AnchorPane.setTopAnchor(botonAtras, 53.0);

        StackPane nombre = crearCampo("Nombre", campoNombre, 94.0, 238.0);
        StackPane cedula = crearCampo("Cédula", campoCedula, 94.0, 335.0);
        StackPane especialidad = crearCampo("Especialidad", campoEspecialidad, 94.0, 432.0);
        StackPane telefono = crearCampo("Teléfono", campoTelefono, 94.0, 530.0);

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
            especialidad,
            telefono
        );
        
        return pantalla;

    }
}
