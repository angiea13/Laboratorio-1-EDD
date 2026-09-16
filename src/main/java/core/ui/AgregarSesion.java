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

public class AgregarSesion extends AgregarRegistro{

    public TextField campoNombreAprendiz;
    public TextField campoEspecialidad;
    public TextField campoNombreInstructor;
    public TextField campoFecha;

    public AgregarSesion() {
        super("Agregar sesión", "Agendar");
        campoNombreAprendiz = new TextField();
        campoEspecialidad = new TextField();
        campoNombreInstructor = new TextField();
        campoFecha = new TextField();
    }

    @Override 
    public AnchorPane construirPantalla() {
        formatoBase();
        
        AnchorPane.setLeftAnchor(botonAtras, 516.0);
        AnchorPane.setTopAnchor(botonAtras, 53.0);

        StackPane aprendiz = crearCampo("Aprendiz", campoNombreAprendiz, 94.0, 238.0);
        StackPane especialidad = crearCampo("Especialidad", campoNombreAprendiz, 94.0, 335.0);
        StackPane instructor = crearCampo("Instructor", campoNombreInstructor, 94.0, 432.0);
        StackPane fecha = crearCampo("Fecha", campoFecha, 94.0, 530.0);

        pantalla.getChildren().addAll(
            esquinaIzq, 
            esquinaDer,
            textoAgregar,
            linea,
            botonConfirmar,
            botonMenu,
            botonAtras,
            aprendiz,
            especialidad,
            instructor,
            fecha
        );
        
        return pantalla;

    }
}
