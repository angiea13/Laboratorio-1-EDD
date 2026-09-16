package core.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public abstract class EliminarRegistro {
    protected AnchorPane pantalla;
    protected AnchorPane ventanaNotificacion;
    protected Label tituloNotif;
    protected StackPane botonAtras;
    protected Button back;
    protected StackPane botonMenu;
    protected Button home;
    protected StackPane botonEliminar;
    protected Button delete;
    protected TextField keyDelete;

    protected String prompt;
    
    
    public EliminarRegistro(String titulo, String promptFieldText) {
        this.pantalla = new AnchorPane();
        this.ventanaNotificacion = new AnchorPane();
        this.tituloNotif = new Label(titulo);
        this.botonAtras = new StackPane();
        this.back = new Button();
        this.botonMenu = new StackPane();
        this.home = new Button();
        this.botonEliminar = new StackPane();
        this.delete = new Button();
        this.keyDelete = new TextField();
        this.prompt = promptFieldText;
    }

    protected void formatoBase() {
        pantalla.setBackground(Asset.BACKGROUND);
        
        ventanaNotificacion.setPrefWidth(845);
        ventanaNotificacion.setPrefHeight(602);

        Rectangle window = new Rectangle(845, 602);
        window.setFill(Color.web("#fbeaff"));
        window.setArcWidth(40);
        window.setArcHeight(40);

        StackPane header = new StackPane();

        Rectangle headerGradiente = new Rectangle(845, 127);
        headerGradiente.getStyleClass().add("gradiente-header");

        HBox elementosHeader = new HBox(40);

        tituloNotif.setTextFill(Color.BLACK);
        tituloNotif.setFont(Font.font(Fuente.COND_BOLD.getName(), 50));

        ImageView cruz = new ImageView(Asset.rmNotif);

        elementosHeader.setAlignment(Pos.CENTER);

        elementosHeader.getChildren().addAll(tituloNotif, cruz);

        header.getChildren().addAll(headerGradiente, elementosHeader);

        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setTopAnchor(header, 0.0);

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

        AnchorPane.setLeftAnchor(botonAtras, 20.0);
        AnchorPane.setTopAnchor(botonAtras, 20.0);

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

        StackPane campoKey = new StackPane();

        Rectangle fondoKey = new Rectangle(528, 73);
        fondoKey.setFill(Color.web("#fdf4ff"));
        fondoKey.setArcWidth(40);
        fondoKey.setArcHeight(40);

        keyDelete.getStyleClass().add("campo-eliminar");
        keyDelete.setPromptText(prompt);
        keyDelete.setFont(Font.font(Fuente.REGULAR.getName(), 24));

        campoKey.getChildren().addAll(fondoKey, keyDelete);

        AnchorPane.setLeftAnchor(campoKey, 159.0);
        AnchorPane.setTopAnchor(campoKey, 310.0);

        Rectangle fondoEliminar = new Rectangle(429, 78);
        Rectangle hoverEliminar = new Rectangle(449, 98);

        Label textoEliminar = new Label("Eliminar");
        textoEliminar.setTextFill(Color.web("#fbeaff"));
        textoEliminar.setFont(Font.font(Fuente.REGULAR.getName(), 45));

        fondoEliminar.setFill(Color.web("#4954ff"));
        fondoEliminar.setArcWidth(40);
        fondoEliminar.setArcHeight(40);

        hoverEliminar.setFill(Color.web("#ff751f"));
        hoverEliminar.setArcWidth(60);
        hoverEliminar.setArcHeight(40);

        hoverEliminar.setVisible(false);

        delete.setPrefWidth(429);
        delete.setPrefHeight(78);
        delete.getStyleClass().add("boton-eliminar");

        delete.setOnMouseEntered(e -> {
            hoverEliminar.setVisible(true);
        });

        // Salir
        delete.setOnMouseExited(e -> {
            hoverEliminar.setVisible(false);
        });

        botonEliminar.setPrefWidth(495);
        botonEliminar.setPrefHeight(90);

        botonEliminar.getChildren().addAll(hoverEliminar, fondoEliminar, textoEliminar, delete);

        AnchorPane.setLeftAnchor(botonEliminar, 190.0);
        AnchorPane.setTopAnchor(botonEliminar, 488.0);

        

        ventanaNotificacion.getChildren().addAll(
            window,
            header,
            botonEliminar,
            campoKey 
        );


    }

    public Button getBotonEliminar() {
        return delete;
    }

    public Button getBotonMenu() {
        return home;
    }
    public Button getBotonAtras() {
        return back;
    }

    public abstract AnchorPane construirPantalla();

}
