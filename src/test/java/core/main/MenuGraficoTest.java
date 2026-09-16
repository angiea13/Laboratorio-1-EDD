package core.main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import javax.swing.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class MenuGraficoTest {
    @TempDir Path carpeta;

    @Test void abreVentanaYConsultaLosArchivosDesdeLosBotones() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Se necesita un entorno de escritorio para probar Swing.");
        var instructores = ArchivoInstructoresIndexado.abrirParaMenu(carpeta);
        assertTrue(instructores.guardar("123456", "Ana", "Música", "3001234567", (short) 0, false));
        SwingUtilities.invokeAndWait(() -> {
            MenuGrafico ventana = null;
            try {
                ventana = new MenuGrafico(carpeta);
                JButton listar = buscar(ventana.getContentPane(), JButton.class, "Listar instructores");
                assertNotNull(listar);
                listar.doClick();
                JTextArea salida = buscar(ventana.getContentPane(), JTextArea.class, null);
                assertNotNull(salida);
                assertTrue(salida.getText().contains("Ana"));
                assertNotNull(buscar(ventana.getContentPane(), JButton.class, "Asignar sesión"));
                assertNotNull(buscar(ventana.getContentPane(), JButton.class, "Cancelar sesión"));
                assertNotNull(buscar(ventana.getContentPane(), JButton.class, "Reiniciar instructores"));
                Container panel = ventana.getContentPane();
                BufferedImage imagen = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D dibujo = imagen.createGraphics();
                panel.printAll(dibujo);
                dibujo.dispose();
                ImageIO.write(imagen, "png", Path.of("target", "gui-verificacion.png").toFile());
            } catch (Exception error) {
                throw new AssertionError(error);
            } finally {
                if (ventana != null) ventana.dispose();
            }
        });
    }

    private <T extends Component> T buscar(Container contenedor, Class<T> tipo, String texto) {
        for (Component componente : contenedor.getComponents()) {
            if (tipo.isInstance(componente) && (texto == null || componente instanceof JButton boton && texto.equals(boton.getText())))
                return tipo.cast(componente);
            if (componente instanceof Container hijo) {
                T encontrado = buscar(hijo, tipo, texto);
                if (encontrado != null) return encontrado;
            }
        }
        return null;
    }
}
