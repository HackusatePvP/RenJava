package me.piitex.renjava.gui.load;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.gui.ScreenView;
import me.piitex.renjava.gui.builders.FontLoader;
import me.piitex.renjava.gui.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.gui.title.DefaultMainTitleScreenView;

public class LoadScreenView extends ScreenView {
    private final ImageLoader backgroundImage;

    public LoadScreenView(ImageLoader backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void build(int page) {
        Group root = new Group();
        RenJavaConfiguration configuration = RenJava.getInstance().getConfiguration();
        FontLoader defaultFont = RenJava.getInstance().getDefaultFont();
        FontLoader menuFont = new FontLoader("Arial");
        menuFont.setSize(48);

        FontLoader menuFontSmall = new FontLoader(menuFont.getName());
        menuFontSmall.setSize(36);
        try {
            ImageView imageView = new ImageView(backgroundImage.build());
            root.getChildren().add(imageView);
        } catch (ImageNotFoundException e) {
            throw new RuntimeException(e);
        }
        new DefaultMainTitleScreenView(this);

        // Display preferences in big font top right
        Text preferences = new Text("Load");
        preferences.setFont(new FontLoader(defaultFont.getName(), 64).getFont());
        preferences.setFill(Color.BLUE);
        preferences.setX(configuration.getTopLeft().getKey() + 50);
        preferences.setY(configuration.getTopLeft().getValue() + 100);
        root.getChildren().add(preferences);

        // Automate some of this idk how as of right now.
        // TODO: 8/9/2023 Pagination is needed no idea how im going to do that efficiently without static abuse but we can try.
        VBox mainBox = new VBox(); // Verticle box will contain 2 hboxes which stack on top of eachother (hopefully)
        HBox topBox = new HBox();
        HBox bottomBox = new HBox();


        for (int i = 0; i < 6; i++) {
            if (i < 3) {

            }
        }
    }

}
