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

public class MainUI extends Application {

    @Override
    public void start(Stage stage) {
        
        String VERSION = "V0.0.3";

        PantallaInicio pi = new PantallaInicio(VERSION);
        PantallaDashboard pd = new PantallaDashboard();

        AnchorPane root = pi.construirPantalla();
        
        Scene scene = new Scene(root);

        scene.getStylesheets().add(
            getClass().getResource("/styles.css").toExternalForm()
        );

        pi.getBotonLogin().setOnAction(e -> {
            scene.setRoot(pd.construirPantalla());
        });

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