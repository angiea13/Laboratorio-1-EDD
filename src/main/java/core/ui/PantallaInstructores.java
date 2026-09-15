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


public class PantallaInstructores {

    Button botonBack, botonHome, botonAgregar, botonEliminar, botonEditar, botonConsultar;

    public PantallaInstructores() {
        this.botonBack = new Button();
        this.botonHome = new Button();
        this.botonAgregar = new Button();
        this.botonEliminar = new Button();
        this.botonEditar = new Button();
        this.botonConsultar = new Button();
    }

    public AnchorPane construirPantalla() {

        StackPane promptUsuario = new StackPane();

        //ANCHORPANE PERMITE CALCULAR POSICIONES CON OFFSETS CON RESPECTO A LOS BORDES DEL PANE
        //AQUÍ SE ESTÁN DESCRIBIENDO LOS ELEMENTOS DE LA PANTALLA DE INCICIO/BIENVENIDA
        AnchorPane pantallaInstructores = new AnchorPane();
        pantallaInstructores.setBackground(Asset.BACKGROUND);

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

        textoPrompt.setText("Seleccione una operación:");
        textoPrompt.setFont(Font.font(Fuente.COND_BOLD_ITALIC.getName(), 44));
        textoPrompt.setTextFill(Color.web("#000000"));

        promptUsuario.getChildren().addAll(textoRect, textoPrompt);

        AnchorPane.setLeftAnchor(promptUsuario, 411.0);
        AnchorPane.setTopAnchor(promptUsuario, 191.0);

        VBox agg = new VBox();
        VBox rmv = new VBox();
        VBox srch = new VBox();
        VBox edit = new VBox();

        ImageView agregarImg = new ImageView(Asset.add);
        ImageView removerImg = new ImageView(Asset.remove);
        ImageView buscarImg = new ImageView(Asset.search);
        ImageView editImg = new ImageView(Asset.edit);

        agg = generarBoton(agregarImg, botonAgregar, "Agregar instructor", 81, 340);
        rmv = generarBoton(removerImg, botonEliminar, "Eliminar instructor", 411, 340);
        edit = generarBoton(editImg, botonEditar, "Editar instructor", 753, 340);
        srch = generarBoton(buscarImg, botonConsultar, "Consultar instructores", 1057, 340);

        Line linea = new Line(81, 170, 1359, 170);
        linea.setStroke(Color.BLACK);
        linea.setStrokeWidth(4);

        Label titulo = new Label("Instructores");
        titulo.setTextFill(Color.BLACK);
        titulo.setFont(Font.font(Fuente.BOLD.getName(), 55));

        AnchorPane.setLeftAnchor(titulo, 81.0);
        AnchorPane.setTopAnchor(titulo, 69.0);

        StackPane botonAtras = new StackPane();

        botonBack.getStyleClass().add("boton-mini-default");
        ImageView bck = new ImageView(Asset.back);
        ImageView bckHov = new ImageView(Asset.backHover);

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

        AnchorPane.setLeftAnchor(botonAtras, 475.0);
        AnchorPane.setTopAnchor(botonAtras, 59.0);

        StackPane botonMenu = new StackPane();

        botonHome.getStyleClass().add("boton-mini-default");
        ImageView hm = new ImageView(Asset.home);
        ImageView hmHov = new ImageView(Asset.homeHover);

        hmHov.setVisible(false);

        botonHome.setOnMouseEntered(e -> {
            hmHov.setVisible(true);
            hm.setVisible(false);
        });

        botonHome.setOnMouseExited(e -> {
            hmHov.setVisible(false);
            hm.setVisible(true);
        });

        botonMenu.getChildren().addAll(hm, hmHov, botonHome);

        AnchorPane.setRightAnchor(botonMenu, 20.0);
        AnchorPane.setBottomAnchor(botonMenu, 20.0);


        pantallaInstructores.getChildren().addAll(
            esquinaIzq, 
            esquinaDer, 
            promptUsuario, 
            agg, 
            rmv, 
            edit,
            srch, 
            linea, 
            titulo,
            botonAtras,
            botonMenu
        );

        return pantallaInstructores;
    }

    public Button getBotonBack() {
        return botonBack;
    }

    public Button getBotonHome() {
        return botonHome;
    }

    public Button getBotonAgregar() {
        return botonAgregar;
    }

    public Button getBotonEliminar() {
        return botonEliminar;
    }

    public Button getBotonEditar() {
        return botonEditar;
    }

    public Button getBotonConsultar() {
        return botonConsultar;
    }

    private VBox generarBoton(ImageView im, Button bu, String text, double x, double y) {
        VBox contenedor = new VBox();
        
        contenedor.setSpacing(27.0);
        contenedor.setAlignment(Pos.CENTER);

        Label desc = new Label(text);
        desc.setTextFill(Color.BLACK);
        desc.setFont(Font.font(Fuente.REGULAR.getName(), 30));

        StackPane boton = new StackPane();

        bu.getStyleClass().add("boton-mediano-azul");

        Rectangle fondoHover = new Rectangle(250, 250);
        fondoHover.setFill(Color.web("#ff751f"));
        fondoHover.setArcWidth(44);
        fondoHover.setArcHeight(44);

        fondoHover.setVisible(false);

        Rectangle fondo = new Rectangle(245, 245);
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
