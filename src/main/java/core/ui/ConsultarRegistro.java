package core.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public abstract class ConsultarRegistro {

    //ALTURA FIJA POR FILA Y ENCABEZADO, SE USAN PARA TOPAR EL SCROLLPANE A 9 FILAS VISIBLES (1 ENCABEZADO + 8 DE DATOS)
    private static final double ALTO_FILA = 1294.0 / 9;
    private static final double ALTO_ENCABEZADO = 1294.0 / 9 + 3;
    private static final int FILAS_VISIBLES_MAX = 8;
    //ANCHO DE CADA COLUMNA: (1403 - 37, ANCHO TOTAL ÚTIL DE LA PANTALLA) / 5 COLUMNAS
    private static final double ANCHO_COLUMNA = (1403.0 - 37.0) / 5;

    protected AnchorPane pantalla;
    protected Label titulo;
    protected Line linea;

    protected StackPane botonAtras;
    protected Button back;
    protected StackPane botonMenu;
    protected Button home;

    protected StackPane campoBuscar;
    protected TextField campoBusqueda;

    protected StackPane campoFiltro;
    protected ComboBox<String> filtro;

    //GRIDPANE ES LA TABLA EN SI, HECHA A MANO CON LABELS; SCROLLPANE LA ENVUELVE Y DA EL SCROLL DE RUEDA GRATIS
    protected GridPane grid;
    protected ScrollPane scroll;

    protected int filas;
    protected String[] encabezados;
    private int filaActual;

    public ConsultarRegistro(String textoTitulo, int filas, String[] encabezados) {
        this.pantalla = new AnchorPane();
        this.titulo = new Label(textoTitulo);
        this.linea = new Line(37, 98, 1403, 98);
        this.botonAtras = new StackPane();
        this.back = new Button();
        this.botonMenu = new StackPane();
        this.home = new Button();
        this.campoBuscar = new StackPane();
        this.campoBusqueda = new TextField();
        this.campoFiltro = new StackPane();
        this.filtro = new ComboBox<>();
        this.grid = new GridPane();
        this.scroll = new ScrollPane();
        this.filas = filas;
        this.filaActual = 1;

        //SI VIENEN MENOS DE 5 ENCABEZADOS SE RELLENA CON NOMBRES GENERICOS, SI VIENEN DE MAS SE IGNORAN LOS SOBRANTES
        this.encabezados = new String[5];
        for (int i = 0; i < 5; i++) {
            this.encabezados[i] = (encabezados != null && i < encabezados.length) ? encabezados[i] : "Columna " + (i + 1);
        }
    }

    protected void formatoBase() {
        pantalla.setBackground(Asset.BACKGROUND);

        titulo.setTextFill(Color.BLACK);
        titulo.setFont(Font.font(Fuente.BOLD.getName(), 40));

        AnchorPane.setLeftAnchor(titulo, 38.0);
        AnchorPane.setTopAnchor(titulo, 30.0);

        linea.setStroke(Color.BLACK);
        linea.setStrokeWidth(4);

        //BOTON ATRAS, MISMO PATRON QUE EL RESTO DE PANTALLAS
        back.getStyleClass().add("boton-mini-default");
        ImageView bck = new ImageView(Asset.back);
        ImageView bckHover = new ImageView(Asset.backHover);
        bckHover.setVisible(false);

        back.setOnMouseEntered(e -> {
            bckHover.setVisible(true);
            bck.setVisible(false);
        });

        back.setOnMouseExited(e -> {
            bckHover.setVisible(false);
            bck.setVisible(true);
        });

        botonAtras.getChildren().addAll(bckHover, bck, back);

        AnchorPane.setRightAnchor(botonAtras, 100.0);
        AnchorPane.setTopAnchor(botonAtras, 10.0);

        //BOTON MENU/HOME
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

        AnchorPane.setRightAnchor(botonMenu, 10.0);
        AnchorPane.setTopAnchor(botonMenu, 10.0);

        //CAMPO DE BUSQUEDA: RECTANGULO DE FONDO + TEXTFIELD CON PROMPT "Buscar..."
        Rectangle fondoBuscar = new Rectangle(827, 55);
        fondoBuscar.setFill(Color.web("#fbeaff"));
        fondoBuscar.setArcWidth(40);
        fondoBuscar.setArcHeight(40);

        campoBusqueda.setPromptText("Buscar...");
        campoBusqueda.setFont(Font.font(Fuente.REGULAR.getName(), 24));
        campoBusqueda.setMaxWidth(770);
        campoBusqueda.getStyleClass().add("campo-agregar");

        campoBuscar.getChildren().addAll(fondoBuscar, campoBusqueda);

        AnchorPane.setLeftAnchor(campoBuscar, 37.0);
        AnchorPane.setTopAnchor(campoBuscar, 115.0);

        //CAMPO DE FILTRO: RECTANGULO DE FONDO + COMBOBOX; SUS OPCIONES SE AGREGAN DESDE FUERA CON agregarOpcionFiltro()
        Rectangle fondoFiltro = new Rectangle(454, 55);
        fondoFiltro.setFill(Color.web("#fbeaff"));
        fondoFiltro.setArcWidth(40);
        fondoFiltro.setArcHeight(40);

        filtro.getStyleClass().add("combo-filtro");
        filtro.setPrefWidth(430);
        filtro.setPrefHeight(50);
        filtro.setPromptText("Filtrar por...");

        filtro.setStyle(
            "-fx-font-family: '" + Fuente.REGULAR.getName() + "';" +
            "-fx-font-size: 24px;"
        );

        campoFiltro.getChildren().addAll(fondoFiltro, filtro);

        AnchorPane.setLeftAnchor(campoFiltro, 880.0);
        AnchorPane.setTopAnchor(campoFiltro, 115.0);

        //LA TABLA (GRID + SCROLL) SE ARMA APARTE PORQUE TIENE SU PROPIA LOGICA DE ENCABEZADO/FILAS/ALTURA
        construirTabla();
        AnchorPane.setLeftAnchor(scroll, 37.0);
        AnchorPane.setTopAnchor(scroll, 185.0);
    }

    //ARMA EL ENCABEZADO Y LAS "filas" FILAS VACIAS A MANO, UNA POR UNA, CON LABELS DIRECTOS
    private void construirTabla() {
        for (int i = 0; i < 5; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(ANCHO_COLUMNA));
        }

        //FILA 0: ENCABEZADO, UN LABEL POR COLUMNA, EN NEGRITA
        for (int i = 0; i < 5; i++) {
            Label celda = new Label(encabezados[i]);
            celda.setFont(Font.font(Fuente.BOLD.getName(), 16));
            celda.setTextFill(Color.BLACK);
            celda.setPrefSize(ANCHO_COLUMNA, ALTO_ENCABEZADO);
            celda.setAlignment(Pos.CENTER_LEFT);
            grid.add(celda, i, 0);
        }

        //FILAS DE DATOS INICIALES, TODAS VACIAS; SE LLENAN DESPUES CON agregarFila()
        for (int i = 0; i < filas; i++) {
            agregarFila("", "", "", "", "");
        }

        //EL SCROLLPANE ENVUELVE EL GRID Y TRAE EL SCROLL DE RUEDA DE MOUSE SIN NADA ADICIONAL
        scroll.setContent(grid);
        scroll.setFitToWidth(true);

        //ESTO TOPA LA ALTURA VISUAL A 9 FILAS (1 ENCABEZADO + 8 DE DATOS); MAS FILAS ACTIVAN LA BARRA/RUEDA
        int filasVisibles = Math.min(filas, FILAS_VISIBLES_MAX);
        scroll.setPrefViewportHeight(ALTO_ENCABEZADO + filasVisibles * ALTO_FILA);
        scroll.setPrefViewportWidth(5 * ANCHO_COLUMNA);
    }

    //PERMITE AGREGAR OPCIONES AL COMBOBOX DE FILTRO DESDE FUERA
    public void agregarOpcionFiltro(String opcion) {
        filtro.getItems().add(opcion);
    }

    //AGREGA UNA FILA DE DATOS AL FINAL DE LA TABLA, UN LABEL POR CADA UNO DE LOS 5 VALORES
    public void agregarFila(String c1, String c2, String c3, String c4, String c5) {
        String[] valores = { c1, c2, c3, c4, c5 };
        for (int i = 0; i < 5; i++) {
            Label celda = new Label(valores[i]);
            celda.setFont(Font.font(Fuente.REGULAR.getName(), 14));
            celda.setTextFill(Color.BLACK);
            celda.setPrefSize(ANCHO_COLUMNA, ALTO_FILA);
            celda.setAlignment(Pos.CENTER_LEFT);
            grid.add(celda, i, filaActual);
        }
        filaActual++;
    }

    public Button getBotonAtras() { return back; }
    public Button getBotonMenu() { return home; }
    public TextField getCampoBusqueda() { return campoBusqueda; }
    public ComboBox<String> getFiltro() { return filtro; }
    public GridPane getGrid() { return grid; }

    public abstract AnchorPane construirPantalla();
}