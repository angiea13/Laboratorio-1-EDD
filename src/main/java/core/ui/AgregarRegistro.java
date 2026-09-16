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

public abstract class AgregarRegistro {
    protected AnchorPane pantalla;
    protected Label textoAgregar;
    protected ImageView esquinaIzq;
    protected ImageView esquinaDer;
    protected Line linea;
    protected StackPane botonConfirmar;
    protected Label textoConfirmar;
    protected Button confirmar;
    protected StackPane botonAtras;
    protected Button back;
    protected StackPane botonMenu;
    protected Button home;

    public AgregarRegistro(String textoAgregar, String textoConfirmar) {
        this.pantalla = new AnchorPane();
        this.textoAgregar = new Label(textoAgregar);
        this.esquinaIzq = new ImageView(Asset.esquinaIzq);
        this.esquinaDer = new ImageView(Asset.esquinaDer);
        this.linea = new Line(81, 156, 1359, 156);
        this.botonConfirmar = new StackPane();
        this.textoConfirmar = new Label(textoConfirmar);
        this.confirmar = new Button();
        this.botonAtras = new StackPane();
        this.back = new Button();
        this.botonMenu = new StackPane();
        this.home = new Button();
    }

    protected void formatoBase() {
        pantalla.setBackground(Asset.BACKGROUND);
        
        textoAgregar.setTextFill(Color.BLACK);
        textoAgregar.setFont(Font.font(Fuente.BOLD.getName(), 50));

        AnchorPane.setLeftAnchor(textoAgregar, 81.0);
        AnchorPane.setTopAnchor(textoAgregar, 69.0);

        AnchorPane.setLeftAnchor(esquinaIzq, 0.0);
        AnchorPane.setTopAnchor(esquinaIzq, 0.0);

        AnchorPane.setRightAnchor(esquinaDer, 0.0);
        AnchorPane.setBottomAnchor(esquinaDer, 0.0);

        Rectangle fondoConfirmar = new Rectangle(495, 90);
        Rectangle hoverConfirmar = new Rectangle(510, 105);

        textoConfirmar.setTextFill(Color.web("#fbeaff"));
        textoConfirmar.setFont(Font.font(Fuente.COND_BOLD.getName(), 50));

        fondoConfirmar.setFill(Color.web("#4954ff"));
        fondoConfirmar.setArcWidth(40);
        fondoConfirmar.setArcHeight(40);

        hoverConfirmar.setFill(Color.web("#ff751f"));
        hoverConfirmar.setArcWidth(40);
        hoverConfirmar.setArcHeight(40);

        hoverConfirmar.setVisible(false);

        confirmar.setPrefWidth(495);
        confirmar.setPrefHeight(90);
        confirmar.getStyleClass().add("boton-confirmar");

        confirmar.setOnMouseEntered(e -> {
            hoverConfirmar.setVisible(true);
        });

        // Salir
        confirmar.setOnMouseExited(e -> {
            hoverConfirmar.setVisible(false);
        });

        botonConfirmar.setPrefWidth(495);
        botonConfirmar.setPrefHeight(90);

        botonConfirmar.getChildren().addAll(hoverConfirmar, fondoConfirmar, textoConfirmar, confirmar);

        AnchorPane.setLeftAnchor(botonConfirmar, 135.0);
        AnchorPane.setTopAnchor(botonConfirmar, 643.0);

        back.getStyleClass().add("boton-mini-default");
        ImageView bck = new ImageView(Asset.back);
        ImageView bckHover = new ImageView(Asset.backHover);

        bckHover.setVisible(false);

        back.setOnMouseEntered(e -> {
            bckHover.setVisible(true);
            bck.setVisible(false);
        });

        // Salir
        back.setOnMouseExited(e -> {
            bckHover.setVisible(false);
            bck.setVisible(true);
        });

        botonAtras.getChildren().addAll(bckHover, bck, back);

        home.getStyleClass().add("boton-mini-default");
        ImageView hm = new ImageView(Asset.home);
        ImageView hmHov = new ImageView(Asset.homeHover);

        hmHov.setVisible(false);

        home.setOnMouseEntered(e -> {
            hmHov.setVisible(true);
            hm.setVisible(false);
        });

        home.setOnMouseExited(e -> {
            hmHov.setVisible(false);
            hm.setVisible(true);
        });

        botonMenu.getChildren().addAll(hm, hmHov, home);

        AnchorPane.setRightAnchor(botonMenu, 20.0);
        AnchorPane.setBottomAnchor(botonMenu, 20.0);
    }

    public Button getBotonConfirmar() {
        return confirmar;
    }

    public Button getBotonMenu() {
        return home;
    }
    public Button getBotonAtras() {
        return back;
    }

    public abstract AnchorPane construirPantalla();

    protected StackPane crearCampo(String nombreCampo, TextField inputCampo, double x, double y) {
        StackPane campo = new StackPane();

        Rectangle fondo = new Rectangle(578, 59);
        fondo.setFill(Color.web("#fbeaff"));
        fondo.setArcWidth(40);
        fondo.setArcHeight(40);

        inputCampo = new TextField();
        inputCampo.setPromptText(nombreCampo);
        inputCampo.setFont(Font.font(Fuente.REGULAR.getName(), 30));
        inputCampo.setMaxWidth(548);
        inputCampo.getStyleClass().add("campo-agregar");

        campo.getChildren().addAll(fondo, inputCampo);

        AnchorPane.setLeftAnchor(campo, x);
        AnchorPane.setTopAnchor(campo, y);

        return campo;
    }
}
