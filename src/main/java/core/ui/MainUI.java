package core.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
            System.out.println("Cambiando de pantalla");
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