package core.ui;

import javafx.scene.image.Image;

public class Asset {
    
    private Asset() {} 

    public static final Image esquinaIzq = new Image(Asset.class.getResourceAsStream("/icons/corner1.png"));
    public static final Image esquinaDer = new Image(Asset.class.getResourceAsStream("/icons/corner2.png"));

    public static final Image sesionImg = new Image(Asset.class.getResourceAsStream("/icons/sesion.png"));
    public static final Image instructorImg = new Image(Asset.class.getResourceAsStream("/icons/instructor.png"));
    public static final Image aprendizImg = new Image(Asset.class.getResourceAsStream("/icons/aprendiz.png"));

    public static final Image add = new Image(Asset.class.getResourceAsStream("/icons/add.png"));
    public static final Image remove = new Image(Asset.class.getResourceAsStream("/icons/remove.png"));
    public static final Image edit = new Image(Asset.class.getResourceAsStream("/icons/edit.png"));
    public static final Image search = new Image(Asset.class.getResourceAsStream("/icons/searchFour.png"));

    public static final Image editNotif = new Image(Asset.class.getResourceAsStream("/icons/editNotification.png"));
    public static final Image rmNotif = new Image(Asset.class.getResourceAsStream("/icons/deleteNotification.png"));
    public static final Image home = new Image(Asset.class.getResourceAsStream("/icons/home.png"));
    public static final Image back = new Image(Asset.class.getResourceAsStream("/icons/back.png"));

    public static final Image detail = new Image(Asset.class.getResourceAsStream("/icons/detalle.png"));
    public static final Image lupita = new Image(Asset.class.getResourceAsStream("/icons/searchIcon.png"));

    public static final Image homeHover = new Image(Asset.class.getResourceAsStream("/icons/home_Hover.png"));
    public static final Image backHover = new Image(Asset.class.getResourceAsStream("/icons/back_Hover.png"));

}
