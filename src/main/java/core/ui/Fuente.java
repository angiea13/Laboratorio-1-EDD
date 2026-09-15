package core.ui;

import javafx.scene.text.Font;

public class Fuente {
    
    private Fuente() {} 

    public static final Font REGULAR = Font.loadFont(
        Fuente.class.getResourceAsStream("/VerdanaPro-Regular.ttf"), 20
    );

    public static final Font SEMIBOLD = Font.loadFont(
        Fuente.class.getResourceAsStream("/VerdanaPro-SemiBold.ttf"), 20
    );

    public static final Font BOLD = Font.loadFont(
        Fuente.class.getResourceAsStream("/VerdanaPro-Bold.ttf"), 20
    );

    public static final Font COND_BLACK = Font.loadFont(
        Fuente.class.getResourceAsStream("/VerdanaPro-CondBlack.ttf"), 20
    );

    public static final Font COND_BOLD = Font.loadFont(
        Fuente.class.getResourceAsStream("/VerdanaPro-CondBold.ttf"), 20
    );

    public static final Font COND_ITALIC = Font.loadFont(
        Fuente.class.getResourceAsStream("/VerdanaPro-CondItalic.ttf"), 20
    );
}
