package me.piitex.renjava.gui.about;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.gui.ScreenView;
import me.piitex.renjava.gui.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;

public class AboutScreenView extends ScreenView {
    private final ImageLoader backgroundImage;

    public AboutScreenView(ImageLoader backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void build() {
        Group root = new Group();
        //backgroundImage = new ImageLoader("/gui/" + RenJava.getInstance().getStyle().name().toLowerCase() + "/overlay/game_menu.png");
        try {
            ImageView imageView = new ImageView(backgroundImage.build());
            root.getChildren().add(imageView);
        } catch (ImageNotFoundException e) {
            e.printStackTrace();
        }
        Text gameTitle = new Text(RenJava.getInstance().getName());
        gameTitle.setX(400);
        gameTitle.setY(200);
        gameTitle.setFill(Color.BLUE);

    }
}
