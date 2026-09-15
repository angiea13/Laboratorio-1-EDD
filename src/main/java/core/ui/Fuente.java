package core.ui;

import javafx.scene.text.Font;

public class Fuente {
    
    private Fuente() {} 

    public static final Font REGULAR = Font.loadFont(
        Fuente.class.getResourceAsStream("/fonts/VerdanaPro-Regular.ttf"), 20
    );

    public static final Font SEMIBOLD = Font.loadFont(
        Fuente.class.getResourceAsStream("/fonts/VerdanaPro-SemiBold.ttf"), 20
    );

    public static final Font BOLD = Font.loadFont(
        Fuente.class.getResourceAsStream("/fonts/VerdanaPro-Bold.ttf"), 20
    );

    public static final Font BOLD_ITALIC = Font.loadFont(
        Fuente.class.getResourceAsStream("/fonts/VerdanaPro-BoldItalic.ttf"), 20
    );

    public static final Font COND_BLACK = Font.loadFont(
        Fuente.class.getResourceAsStream("/fonts/VerdanaPro-CondBlack.ttf"), 20
    );

    public static final Font COND_BOLD = Font.loadFont(
        Fuente.class.getResourceAsStream("/fonts/VerdanaPro-CondBold.ttf"), 20
    );

    public static final Font COND_BOLD_ITALIC = Font.loadFont(
        Fuente.class.getResourceAsStream("/fonts/VerdanaPro-CondBoldItalic.ttf"), 20
    );

    public static final Font COND_ITALIC = Font.loadFont(
        Fuente.class.getResourceAsStream("/fonts/VerdanaPro-CondItalic.ttf"), 20
    );
    
}
