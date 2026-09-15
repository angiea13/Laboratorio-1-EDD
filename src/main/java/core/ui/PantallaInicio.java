package core.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;


public class PantallaInicio {

    public String version;
    Button login;

    public PantallaInicio(String version) {
        this.version = version;
    }

    public AnchorPane construirPantalla() {
        Background bg = new Background((new BackgroundFill(Color.web("#e7e7e7"),null, null)));

        //ANCHORPANE PERMITE CALCULAR POSICIONES CON OFFSETS CON RESPECTO A LOS BORDES DEL PANE
        //AQUÍ SE ESTÁN DESCRIBIENDO LOS ELEMENTOS DE LA PANTALLA DE INCICIO/BIENVENIDA
        AnchorPane pantallaInicio = new AnchorPane();
        pantallaInicio.setBackground(bg);

        ImageView esquinaIzq = new ImageView(Asset.esquinaIzq);

        ImageView esquinaDer = new ImageView(Asset.esquinaDer);

        AnchorPane.setLeftAnchor(esquinaIzq, 0.0);
        AnchorPane.setTopAnchor(esquinaIzq, 0.0);

        AnchorPane.setRightAnchor(esquinaDer, 0.0);
        AnchorPane.setBottomAnchor(esquinaDer, 0.0);

        //LABEL HORAFECHA INDICA LA FECHA/HORA EN EL BORDE SUPERIOR DERECHO DE LA PANTALLA
        Label horaFecha = new Label();

        DateTimeFormatter formatoFechaHora =
            DateTimeFormatter.ofPattern("dd MMMM 'de' yyyy, HH:mm:ss");

        horaFecha.setTextFill(Color.web("#A6A6A6"));
        horaFecha.setFont(Font.font(Fuente.REGULAR.getName(), 20));

        Timeline reloj = new Timeline(
            new KeyFrame(Duration.ZERO, e -> {
                horaFecha.setText(
                    LocalDateTime.now().format(formatoFechaHora)
                );
            }),
            new KeyFrame(Duration.seconds(1))
        );

        reloj.setCycleCount(Timeline.INDEFINITE);
        reloj.play();

        AnchorPane.setTopAnchor(horaFecha, 17.0);
        AnchorPane.setRightAnchor(horaFecha, 20.0);

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

        //OTRO STACKPANE PERO PARA LOS COSITOS DE LOGIN
        
        StackPane fieldUser = new StackPane();
        StackPane fieldPswrd = new StackPane();

        //AQUÍ ESTÁ EL RECTANGULITO DEL USER/PASSWORD
        Rectangle fondoField = new Rectangle();

        fondoField.setWidth(352);
        fondoField.setHeight(69);

        fondoField.setFill(Color.web("#fbeaff"));

        fondoField.setArcWidth(44);
        fondoField.setArcHeight(44);

        //AQUÍ ESTÁ EL CAMPO DE USUARIO
        TextField usuario = new TextField();

        usuario.setPromptText("Usuario");
        usuario.setFont(Font.font(Fuente.REGULAR.getName(), 30));
        usuario.getStyleClass().add("campo-inicio");

        usuario.setMaxWidth(312);

        fieldUser.getChildren().addAll(fondoField, usuario);

        AnchorPane.setLeftAnchor(fieldUser, 544.0);
        AnchorPane.setTopAnchor(fieldUser, 370.0);

        PasswordField clave = new PasswordField();

        Rectangle fondoFieldPswrd = new Rectangle();
        
        fondoFieldPswrd.setWidth(352);
        fondoFieldPswrd.setHeight(69);

        fondoFieldPswrd.setFill(Color.web("#fbeaff"));

        fondoFieldPswrd.setArcWidth(44);
        fondoFieldPswrd.setArcHeight(44);

        clave.setPromptText("Contraseña");
        clave.setFont(Font.font(Fuente.REGULAR.getName(), 30));
        clave.getStyleClass().add("campo-inicio");
        //me cansé de comentar perdon
        clave.setMaxWidth(312);

        fieldPswrd.getChildren().addAll(fondoFieldPswrd, clave);

        AnchorPane.setLeftAnchor(fieldPswrd, 544.0);
        AnchorPane.setTopAnchor(fieldPswrd, 474.0);

        StackPane loginField = new StackPane();
        
        login = new Button("INGRESAR");

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
        AnchorPane.setTopAnchor(loginField, 599.0);

        pantallaInicio.getChildren().addAll(
            esquinaIzq, 
            esquinaDer, 
            horaFecha, 
            version, 
            logo, 
            fieldUser, 
            fieldPswrd, 
            loginField
        );

        return pantallaInicio;
    }

    public Button getBotonLogin() {
        return login;
    }
    
}
