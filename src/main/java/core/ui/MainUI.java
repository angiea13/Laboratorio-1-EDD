package core.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainUI extends Application {

    @Override
    public void start(Stage stage) {
        
        String VERSION = "V0.0.6";

        PantallaInicio pini = new PantallaInicio(VERSION);
        PantallaMenu pm = new PantallaMenu(VERSION);
        PantallaDashboard pd = new PantallaDashboard();
        PantallaSesiones ps = new PantallaSesiones();
        PantallaInstructores pi = new PantallaInstructores();
        PantallaAprendices pa = new PantallaAprendices();

        AnchorPane root = pini.construirPantalla();
        
        Scene scene = new Scene(root);

        scene.getStylesheets().add(
            getClass().getResource("/styles.css").toExternalForm()
        );

        //Al hacer click en login la scene cambia al dashboard
        pini.getBotonLogin().setOnAction(e -> {
            scene.setRoot(pd.construirPantalla());
        });

        //ACCION BOTONES PANTALLA DASHBOARD
        pd.getBotonSesion().setOnAction(e -> {
            scene.setRoot(ps.construirPantalla());
        });

        pd.getBotonInstructor().setOnAction(e -> {
            scene.setRoot(pi.construirPantalla());
        });

        pd.getBotonAprendiz().setOnAction(e -> {
            scene.setRoot(pa.construirPantalla());
        });

        pd.getBotonBack().setOnAction(e -> {
            scene.setRoot(pm.construirPantalla());
        });

        //ACCIONES BOTONES PANTALLA MENU
        pm.getBotonLogout().setOnAction(e -> {
            scene.setRoot(root);
        });

        pm.getBotonLogin().setOnAction(e -> {
            scene.setRoot(pd.construirPantalla());
        });

        //ACCIONES BOTONES PANTALLA SESIONES
        ps.getBotonBack().setOnAction(e -> {
            scene.setRoot(pd.construirPantalla());
        });

        ps.getBotonHome().setOnAction(e -> {
            scene.setRoot(pm.construirPantalla());
        });

        //ACCIONES BOTONES PANTALLA INSTRUCTORES
        pi.getBotonBack().setOnAction(e -> {
            scene.setRoot(pd.construirPantalla());
        });

        pi.getBotonHome().setOnAction(e -> {
            scene.setRoot(pm.construirPantalla());
        });

        //ACCIONES BOTONES PANTALLA APRENDICES
        pa.getBotonBack().setOnAction(e -> {
            scene.setRoot(pd.construirPantalla());
        });

        pa.getBotonHome().setOnAction(e -> {
            scene.setRoot(pm.construirPantalla());
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