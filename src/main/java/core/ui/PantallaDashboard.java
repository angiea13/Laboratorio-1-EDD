package core.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.Line;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;


public class PantallaDashboard {

    Button botonBack, botonSesion, botonInstructor, botonAprendiz;

    public PantallaDashboard() {
        this.botonBack = new Button();
        this.botonSesion = new Button();
        this.botonInstructor = new Button();
        this.botonAprendiz = new Button();
    }

    public AnchorPane construirPantalla() {

        StackPane preguntaConsulta = new StackPane();
        
        Background bg = new Background((new BackgroundFill(Color.web("#e7e7e7"),null, null)));

        //ANCHORPANE PERMITE CALCULAR POSICIONES CON OFFSETS CON RESPECTO A LOS BORDES DEL PANE
        //AQUÍ SE ESTÁN DESCRIBIENDO LOS ELEMENTOS DE LA PANTALLA DE INCICIO/BIENVENIDA
        AnchorPane pantallaDashboard = new AnchorPane();
        pantallaDashboard.setBackground(bg);

        ImageView esquinaIzq = new ImageView(
            new Image(getClass().getResourceAsStream("/corner1.png"))
        );

        ImageView esquinaDer = new ImageView(
            new Image(getClass().getResourceAsStream("/corner2.png"))
        );

        AnchorPane.setLeftAnchor(esquinaIzq, 0.0);
        AnchorPane.setTopAnchor(esquinaIzq, 0.0);

        AnchorPane.setRightAnchor(esquinaDer, 0.0);
        AnchorPane.setBottomAnchor(esquinaDer, 0.0);

        //AQUÍ ESTÁ EL RECTANGULITO DEL que deseas consultar
        Rectangle textoRect = new Rectangle();

        textoRect.setWidth(618);
        textoRect.setHeight(69);

        textoRect.setFill(Color.web("#fbeaff"));

        textoRect.setArcWidth(44);
        textoRect.setArcHeight(44);

        //AQUÍ ESTÁ EL texto consulta
        Label textoPrompt = new Label();

        textoPrompt.setText("¿Qué desea consultar?");
        textoPrompt.setFont(Font.font(Fuente.COND_ITALIC.getFamily(), 44));
        textoPrompt.setTextFill(Color.web("#000000"));

        preguntaConsulta.getChildren().addAll(textoRect, textoPrompt);

        AnchorPane.setLeftAnchor(preguntaConsulta, 411.0);
        AnchorPane.setTopAnchor(preguntaConsulta, 191.0);

        StackPane sesion = new StackPane();
        StackPane instructor = new StackPane();
        StackPane aprendiz = new StackPane();

        ImageView sesionImg = new ImageView(
            new Image(getClass().getResourceAsStream("/sesion.png"))
        );

        ImageView instructorImg = new ImageView(
            new Image(getClass().getResourceAsStream("/instructor.png"))
        );

        ImageView aprendizImg = new ImageView(
            new Image(getClass().getResourceAsStream("/aprendiz.png"))
        );

        sesion = generarBoton(sesionImg, botonSesion, 169, 391);
        instructor = generarBoton(instructorImg, botonInstructor, 571, 391);
        aprendiz = generarBoton(aprendizImg, botonAprendiz, 974, 391);

        Line linea = new Line(81, 170, 1359, 170);
        linea.setStroke(Color.BLACK);
        linea.setStrokeWidth(4);

        Label mensajeBienvenida = new Label("Bienvenid@, Usuario");
        mensajeBienvenida.setTextFill(Color.BLACK);
        mensajeBienvenida.setFont(Font.font(Fuente.COND_BOLD.getFamily(), 44));

        AnchorPane.setLeftAnchor(mensajeBienvenida, 81.0);
        AnchorPane.setTopAnchor(mensajeBienvenida, 69.0);


        pantallaDashboard.getChildren().addAll(
            esquinaIzq, 
            esquinaDer, 
            preguntaConsulta, 
            sesion, 
            instructor, 
            aprendiz, 
            linea, 
            mensajeBienvenida
        );

        return pantallaDashboard;
    }

    public Button getBotonSesion() {
        return botonSesion;
    }

    public Button getBotonInstructor() {
        return botonInstructor;
    }

    public Button getBotonAprendiz() {
        return botonAprendiz;
    }

    public Button getBotonBack() {
        return botonBack;
    }

    private StackPane generarBoton(ImageView im, Button bu, double x, double y) {
        StackPane boton = new StackPane();

        bu.getStyleClass().add("boton-grande-azul");

        Rectangle fondoHover = new Rectangle(302, 302);
        fondoHover.setFill(Color.web("#ff751f"));
        fondoHover.setArcWidth(44);
        fondoHover.setArcHeight(44);

        fondoHover.setVisible(false);

        Rectangle fondo = new Rectangle(297, 297);
        fondo.setFill(Color.web("#3662ff"));
        fondo.setArcWidth(44);
        fondo.setArcHeight(44);

        bu.setOnMouseEntered(e -> {
            fondoHover.setVisible(true);
        });
        
        bu.setOnMouseExited(e -> {
            fondoHover.setVisible(false);
        });

        boton.getChildren().addAll(fondoHover, fondo, im, bu);

        AnchorPane.setLeftAnchor(boton, x);
        AnchorPane.setTopAnchor(boton, y);

        return boton;
    }
    
}
