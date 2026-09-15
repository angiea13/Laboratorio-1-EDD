package core.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestUI extends Application {

    @Override
    public void start(Stage stage) {
        
        Pane pantallaInicio = new Pane();
        Label horaFecha = new Label();

        LocalDateTime fechaHora = LocalDateTime.now();
        DateTimeFormatter formatoFechaHora = DateTimeFormatter.ofPattern("dd MMM de yyyy, HH:mm:ss");
        String textoFecha = fechaHora.format(formatoFechaHora);
        
        horaFecha.setText(textoFecha);

        horaFecha.setLayoutX(967);
        horaFecha.setLayoutY(17);

        pantallaInicio.getChildren().add(horaFecha);


        Color bgColor = Color.web("#E7E7E7");

        Label label = new Label("¡Bienvenido al Sistema de Información STRUCTART!");

        VBox root = new VBox(10);
        root.setAlignment(javafx.geometry.Pos.CENTER);

        root.setBackground(
            new Background(
            new BackgroundFill(bgColor, null, null)
            )
        );

        root.getChildren().addAll(label, pantallaInicio);

        Scene scene = new Scene(root);

        stage.setTitle("STRUCTART Integrated Systems (SARIS)");
        stage.setResizable(false);

        stage.setWidth(1440);
        stage.setHeight(810);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}