package me.piitex.renjava.gui.about;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.gui.ScreenView;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.gui.title.DefaultMainTitleScreenView;

import java.io.File;
import java.net.MalformedURLException;

public class AboutScreenView extends ScreenView {
    private final ImageLoader backgroundImage;

    public AboutScreenView(ImageLoader backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void build(Stage stage) {
        Group root = new Group();
        //backgroundImage = new ImageLoader("/gui/" + RenJava.getInstance().getStyle().name().toLowerCase() + "/overlay/game_menu.png");
        try {
            ImageView imageView = new ImageView(backgroundImage.build());
            root.getChildren().add(imageView);
        } catch (ImageNotFoundException e) {
            e.printStackTrace();
            return;
        }
        new DefaultMainTitleScreenView(this);
        Text gameTitle = new Text(RenJava.getInstance().getName());
        gameTitle.setX(400);
        gameTitle.setY(200);
        gameTitle.setFill(Color.BLUE);
        Text version = new Text("Version: " + RenJava.getInstance().getVersion());
        version.setX(gameTitle.getX());
        version.setY(gameTitle.getY() + 15);
        Hyperlink hyperlink = new Hyperlink("RenJava");
        hyperlink.setTooltip(new Tooltip("https://github.com/HackusatePvP/RenJava"));
        Hyperlink renpy = new Hyperlink("RenPy");
        renpy.setTooltip(new Tooltip("https://www.renpy.org/"));
        TextFlow textFlow = new TextFlow(
                new Text("Made with "),
                hyperlink,
                new Text(RenJava.getInstance().getBuildVersion()),
                new Text(System.lineSeparator()),
                new Text("RenJava is a free open sourced project inspired by "),
                renpy,
                new Text(".")
        );
        textFlow.setTranslateX(version.getX());
        textFlow.setTranslateY(gameTitle.getY() + 20);
        root.getChildren().add(textFlow);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        RenJava.getInstance().setStage(stage, StageType.OPTIONS_MENU);
    }
}
