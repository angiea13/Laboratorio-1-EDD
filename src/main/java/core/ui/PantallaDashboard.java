package core.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;


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

        ImageView esquinaIzq = new ImageView(Asset.esquinaIzq);
        ImageView esquinaDer = new ImageView(Asset.esquinaDer);

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
        textoPrompt.setFont(Font.font(Fuente.COND_BOLD_ITALIC.getName(), 44));
        textoPrompt.setTextFill(Color.web("#000000"));

        preguntaConsulta.getChildren().addAll(textoRect, textoPrompt);

        AnchorPane.setLeftAnchor(preguntaConsulta, 411.0);
        AnchorPane.setTopAnchor(preguntaConsulta, 191.0);

        VBox sesion = new VBox();
        VBox instructor = new VBox();
        VBox aprendiz = new VBox();

        ImageView sesionImg = new ImageView(Asset.sesionImg);

        ImageView instructorImg = new ImageView(Asset.instructorImg);

        ImageView aprendizImg = new ImageView(Asset.aprendizImg);

        sesion = generarBoton(sesionImg, botonSesion, "Sesiones", 169, 315);
        instructor = generarBoton(instructorImg, botonInstructor, "Instructores", 571, 315);
        aprendiz = generarBoton(aprendizImg, botonAprendiz, "Aprendices", 974, 315);

        Line linea = new Line(81, 170, 1359, 170);
        linea.setStroke(Color.BLACK);
        linea.setStrokeWidth(4);

        Label mensajeBienvenida = new Label("Bienvenid@, Usuario");
        mensajeBienvenida.setTextFill(Color.BLACK);
        mensajeBienvenida.setFont(Font.font(Fuente.BOLD.getName(), 55));

        AnchorPane.setLeftAnchor(mensajeBienvenida, 81.0);
        AnchorPane.setTopAnchor(mensajeBienvenida, 69.0);

        StackPane botonAtras = new StackPane();

        this.botonBack.getStyleClass().add("boton-pequeño-default");
        ImageView bck = new ImageView(Asset.back);
        ImageView bckHov = new ImageView(Asset.back);

        bckHov.setVisible(false);

        botonBack.setOnMouseEntered(e -> {
            bckHov.setVisible(true);
            bck.setVisible(false);
        });

        botonBack.setOnMouseExited(e -> {
            bckHov.setVisible(false);
            bck.setVisible(true);
        });

        botonAtras.getChildren().addAll(bck, bckHov, botonBack);

        AnchorPane.setLeftAnchor(botonAtras, 776.0);
        AnchorPane.setTopAnchor(botonAtras, 69.0);


        pantallaDashboard.getChildren().addAll(
            esquinaIzq, 
            esquinaDer, 
            preguntaConsulta, 
            sesion, 
            instructor, 
            aprendiz, 
            linea, 
            mensajeBienvenida,
            botonAtras
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

    private VBox generarBoton(ImageView im, Button bu, String text, double x, double y) {
        VBox contenedor = new VBox();
        
        contenedor.setSpacing(27.0);
        contenedor.setAlignment(Pos.CENTER);

        Label desc = new Label(text);
        desc.setTextFill(Color.BLACK);
        desc.setFont(Font.font(Fuente.REGULAR.getName(), 30));

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

        contenedor.getChildren().addAll(desc, boton);

        AnchorPane.setLeftAnchor(contenedor, x);
        AnchorPane.setTopAnchor(contenedor, y);

        return contenedor;
    }
    
}
