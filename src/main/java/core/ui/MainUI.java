package core.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainUI extends Application {

    @Override
    public void start(Stage stage) {
        
        String VERSION = "V0.0.81";

        PantallaInicio pini = new PantallaInicio(VERSION);
        PantallaMenu pm = new PantallaMenu(VERSION);
        PantallaDashboard pd = new PantallaDashboard();
        PantallaSesiones ps = new PantallaSesiones();
        PantallaInstructores pi = new PantallaInstructores();
        PantallaAprendices pa = new PantallaAprendices();
        
        AgregarSesion as = new AgregarSesion();
        AgregarInstructor is = new AgregarInstructor();
        AgregarAprendiz aa = new AgregarAprendiz();

        EliminarSesion es = new EliminarSesion();
        EliminarInstructor ei = new EliminarInstructor();
        EliminarAprendiz ea = new EliminarAprendiz();


        AnchorPane root = pini.construirPantalla();
        AnchorPane menu = pm.construirPantalla();
        AnchorPane dashboard = pd.construirPantalla();
        AnchorPane session = ps.construirPantalla();
        AnchorPane instructor = pi.construirPantalla();
        AnchorPane apprentice = pa.construirPantalla();

        AnchorPane sessionAgg = as.construirPantalla();
        AnchorPane instructorAgg = is.construirPantalla();
        AnchorPane apprenticeAgg = aa.construirPantalla();

        AnchorPane sessionRm = es.construirPantalla();
        AnchorPane instructorRm = ei.construirPantalla();
        AnchorPane apprenticeRm = ea.construirPantalla();
        
        Scene scene = new Scene(root);

        scene.getStylesheets().add(
            getClass().getResource("/styles.css").toExternalForm()
        );

        //Al hacer click en login la scene cambia al dashboard
        pini.getBotonLogin().setOnAction(e -> {
            scene.setRoot(dashboard);
        });

        //ACCION BOTONES PANTALLA DASHBOARD
        pd.getBotonSesion().setOnAction(e -> {
            scene.setRoot(session);
        });

        pd.getBotonInstructor().setOnAction(e -> {
            scene.setRoot(instructor);
        });

        pd.getBotonAprendiz().setOnAction(e -> {
            scene.setRoot(apprentice);
        });

        pd.getBotonBack().setOnAction(e -> {
            scene.setRoot(menu);
        });

        //ACCIONES BOTONES PANTALLA MENU
        pm.getBotonLogout().setOnAction(e -> {
            scene.setRoot(root);
        });

        pm.getBotonLogin().setOnAction(e -> {
            scene.setRoot(dashboard);
        });

        //ACCIONES BOTONES PANTALLA SESIONES
        ps.getBotonAgregar().setOnAction(e -> {
            scene.setRoot(sessionAgg);
        });

        ps.getBotonBack().setOnAction(e -> {
            scene.setRoot(dashboard);
        });

        ps.getBotonHome().setOnAction(e -> {
            scene.setRoot(menu);
        });

        ps.getBotonEliminar().setOnAction(e -> {
            scene.setRoot(sessionRm);
        });

        //ACCIONES BOTONES PANTALLA INSTRUCTORES
        pi.getBotonBack().setOnAction(e -> {
            scene.setRoot(dashboard);
        });

        pi.getBotonHome().setOnAction(e -> {
            scene.setRoot(menu);
        });

        pi.getBotonAgregar().setOnAction(e -> {
            scene.setRoot(instructorAgg);
        });

        pi.getBotonEliminar().setOnAction(e -> {
            scene.setRoot(instructorRm);
        });

        //ACCIONES BOTONES PANTALLA APRENDICES
        pa.getBotonBack().setOnAction(e -> {
            scene.setRoot(dashboard);
        });

        pa.getBotonHome().setOnAction(e -> {
            scene.setRoot(menu);
        });

        pa.getBotonAgregar().setOnAction(e -> {
            scene.setRoot(apprenticeAgg);
        });

        pa.getBotonEliminar().setOnAction(e -> {
            scene.setRoot(apprenticeRm);
        });

        //ACCIONES BOTONES AGREGAR SESION
        as.getBotonAtras().setOnAction(e -> {
            scene.setRoot(session);
        });

        as.getBotonMenu().setOnAction(e -> {
            scene.setRoot(menu);
        });

        as.getBotonConfirmar().setOnAction(e -> {
            //lógica de agregar registro aquí
        });

        //ACCIONES BOTONES AGREGAR INSTRUCTOR
        is.getBotonAtras().setOnAction(e -> {
            scene.setRoot(instructor);
        });

        is.getBotonMenu().setOnAction(e -> {
            scene.setRoot(menu);
        });
        
        is.getBotonConfirmar().setOnAction(e -> {
            //lógica de agregar registro aquí
        });

        //ACCION DE BOTONES DE AGREGAR APRENDIZ
        aa.getBotonAtras().setOnAction(e -> {
            scene.setRoot(instructor);
        });

        aa.getBotonMenu().setOnAction(e -> {
            scene.setRoot(menu);
        });

        aa.getBotonConfirmar().setOnAction(e -> {
            //lógica de agregar registro aquí
        });

        // ACCIONES BOTONES ELIMINAR SESION
        es.getBotonAtras().setOnAction(e -> {
            scene.setRoot(session);
        });

        es.getBotonMenu().setOnAction(e -> {
            scene.setRoot(menu);
        });

        es.getBotonEliminar().setOnAction(e -> {
            // lógica de eliminar registro aquí
        });

        // ACCIONES BOTONES ELIMINAR INSTRUCTOR
        ei.getBotonAtras().setOnAction(e -> {
            scene.setRoot(instructor);
        });

        ei.getBotonMenu().setOnAction(e -> {
            scene.setRoot(menu);
        });

        ei.getBotonEliminar().setOnAction(e -> {
            // lógica de eliminar instructor aquí
        });

        // ACCIONES BOTONES ELIMINAR APRENDIZ
        ea.getBotonAtras().setOnAction(e -> {
            scene.setRoot(apprentice);
        });

        ea.getBotonMenu().setOnAction(e -> {
            scene.setRoot(menu);
        });

        ea.getBotonEliminar().setOnAction(e -> {
            // lógica de eliminar aprendiz aquí
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