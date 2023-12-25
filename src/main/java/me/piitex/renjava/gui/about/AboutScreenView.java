package me.piitex.renjava.gui.about;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.builders.ButtonBuilder;
import me.piitex.renjava.gui.ScreenView;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AboutScreenView extends ScreenView {
    private final ImageLoader backgroundImage;

    public AboutScreenView(ImageLoader backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void build(Stage stage, boolean ui) {
        Group root = new Group();
        //backgroundImage = new ImageLoader("/gui/" + RenJava.getInstance().getStyle().name().toLowerCase() + "/overlay/game_menu.png");
        try {
            ImageView imageView = new ImageView(backgroundImage.build());
            root.getChildren().add(imageView);
        } catch (ImageNotFoundException e) {
            e.printStackTrace();
        }

        VBox vBox = getButtonVbox();
        List<ButtonBuilder> list = new ArrayList<>(RenJava.getInstance().getCustomTitleScreen().getTitleScreenView().getButtons());
        Collections.reverse(list);
        for (ButtonBuilder builder : list) {
            vBox.getChildren().add(builder.build());
        }
        root.getChildren().add(vBox);

        Text gameTitle = new Text(RenJava.getInstance().getName());
        gameTitle.setX(400);
        gameTitle.setY(200);
        gameTitle.setFill(Color.BLUE);
        Text version = new Text("Version: " + RenJava.getInstance().getVersion());
        version.setX(gameTitle.getX());
        version.setY(gameTitle.getY() + 15);
        Hyperlink hyperlink = new Hyperlink("RenJava");
        hyperlink.setTooltip(new Tooltip("https://github.com/HackusatePvP/RenJava"));
        hyperlink.setOnAction(event -> RenJava.getInstance().getHost().showDocument(hyperlink.getText()));
        Hyperlink renpy = new Hyperlink("RenPy");
        renpy.setTooltip(new Tooltip("https://www.renpy.org/"));
        renpy.setOnAction(event -> RenJava.getInstance().getHost().showDocument(renpy.getText()));
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
