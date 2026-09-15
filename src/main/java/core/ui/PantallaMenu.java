package core.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class PantallaMenu {

    public String version;
    Button login;
    Button logout;

    public PantallaMenu(String version) {
        this.version = version;
        this.login = new Button();
        this.logout = new Button();
    }

    public AnchorPane construirPantalla() {

        //ANCHORPANE PERMITE CALCULAR POSICIONES CON OFFSETS CON RESPECTO A LOS BORDES DEL PANE
        //AQUÍ SE ESTÁN DESCRIBIENDO LOS ELEMENTOS DE LA PANTALLA DE INCICIO/BIENVENIDA
        AnchorPane pantallaMenu = new AnchorPane();
        pantallaMenu.setBackground(Asset.BACKGROUND);

        ImageView esquinaIzq = new ImageView(Asset.esquinaIzq);

        ImageView esquinaDer = new ImageView(Asset.esquinaDer);

        AnchorPane.setLeftAnchor(esquinaIzq, 0.0);
        AnchorPane.setTopAnchor(esquinaIzq, 0.0);

        AnchorPane.setRightAnchor(esquinaDer, 0.0);
        AnchorPane.setBottomAnchor(esquinaDer, 0.0);    

        //VERSION
        Label version = new Label(this.version);
        version.setTextFill(Color.web("#A6A6A6"));
        version.setFont(Font.font(Fuente.SEMIBOLD.getName(), 20));

        AnchorPane.setBottomAnchor(version, 17.0);
        AnchorPane.setLeftAnchor(version, 20.0);

        //STACKPANE PARA QUE NO SE TAPEN LAS COSAASSSSS
        StackPane logo = new StackPane();

        //AQUÍ ESTÁ EL RECTANGULITO DEL LOGO
        Rectangle logoRect = new Rectangle();

        logoRect.setWidth(1040);
        logoRect.setHeight(134);

        logoRect.setFill(Color.web("#fbeaff"));

        logoRect.setArcWidth(44);
        logoRect.setArcHeight(44);

        //AQUÍ ESTÁ EL LOGO
        Label structart = new Label();

        structart.setText("STRUCTART SYSTEMS ©");
        structart.setFont(Font.font(Fuente.COND_BOLD.getName(), 65));
        structart.getStyleClass().add("gradiente-logo");

        logo.getChildren().addAll(logoRect, structart);

        AnchorPane.setTopAnchor(logo, 172.0);
        AnchorPane.setLeftAnchor(logo, 200.0);                

        StackPane loginField = new StackPane();
        
        login.setText("INGRESAR");

        Rectangle hoverLogin = new Rectangle(510, 105);
        hoverLogin.setFill(Color.web("#ff751f"));

        hoverLogin.setArcWidth(45);
        hoverLogin.setArcHeight(45);

        hoverLogin.setVisible(false);

        login.setPrefWidth(495);
        login.setPrefHeight(90);
        login.setFont(Font.font(Fuente.COND_BOLD.getName(), 44));
        login.getStyleClass().add("boton-login");

        login.setOnMouseEntered(e -> {
            hoverLogin.setVisible(true);
        });

        // Salir
        login.setOnMouseExited(e -> {
            hoverLogin.setVisible(false);
        });

        loginField.getChildren().addAll(hoverLogin, login);

        AnchorPane.setLeftAnchor(loginField, 472.0);
        AnchorPane.setTopAnchor(loginField, 375.0);

        StackPane logoutField = new StackPane();
        
        logout.setText("CERRAR SESIÓN");

        Rectangle hoverLogout = new Rectangle(510, 105);
        hoverLogout.setFill(Color.web("#ff751f"));

        hoverLogout.setArcWidth(45);
        hoverLogout.setArcHeight(45);

        hoverLogout.setVisible(false);

        logout.setPrefWidth(495);
        logout.setPrefHeight(90);
        logout.setFont(Font.font(Fuente.COND_BOLD.getName(), 44));
        logout.getStyleClass().add("boton-login");

        logout.setOnMouseEntered(e -> {
            hoverLogout.setVisible(true);
        });

        // Salir
        logout.setOnMouseExited(e -> {
            hoverLogout.setVisible(false);
        });

        logoutField.getChildren().addAll(hoverLogout, logout);

        AnchorPane.setLeftAnchor(logoutField, 472.0);
        AnchorPane.setTopAnchor(logoutField, 487.0);

        pantallaMenu.getChildren().addAll(
            esquinaIzq, 
            esquinaDer, 
            version, 
            logo, 
            loginField,
            logoutField
        );

        return pantallaMenu;
    }

    public Button getBotonLogin() {
        return login;
    }

    public Button getBotonLogout() {
        return logout;
    }
    
}
