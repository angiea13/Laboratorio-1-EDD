package core.test;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label label;

    @FXML
    public void initialize() {
        label.setText("¡Bienvenido al Sistema de Información STRUCTART!");
    }
        
}