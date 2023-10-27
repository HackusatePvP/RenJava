package me.piitex.renjava.gui.prefernces;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.gui.ScreenView;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.api.builders.ButtonBuilder;
import me.piitex.renjava.api.builders.FontLoader;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.gui.title.DefaultMainTitleScreenView;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreferenceScreenView extends ScreenView {
    private final ImageLoader backgroundImage;

    public PreferenceScreenView(ImageLoader backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public void build(Stage stage, boolean ui) {
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

        // TODO: 8/1/2023 Call custom layout if it exists
        // I can just call the default view.
        new DefaultMainTitleScreenView(this);

        // Display preferences in big font top right
        Text preferences = new Text("Preferences");
        preferences.setFont(new FontLoader(defaultFont.getName(), 64).getFont());
        preferences.setFill(Color.BLUE);
        preferences.setX(configuration.getTopLeft().getKey() + 50);
        preferences.setY(configuration.getTopLeft().getValue() + 100);
        root.getChildren().add(preferences);

        Text displayText = new Text("Display");
        displayText.setFont(menuFont.getFont());
        displayText.setX(configuration.getTopLeft().getKey() + 700);
        displayText.setY(configuration.getTopLeft().getValue() + 300);
        // TODO: 8/1/2023 Make this work with styles
        displayText.setFill(Color.BLUE);
        root.getChildren().add(displayText);

        // Underneath Display add the option for Window or Fullscreen (buttons)
        int displayX = (int) displayText.getX();
        int displayY = (int) displayText.getY();
        ButtonBuilder windowButton = new ButtonBuilder("window-option","Window", Color.LIGHTGRAY, displayX + 10, displayY +  10,1 ,1);
        windowButton.setFont(menuFontSmall.getFont());
        root.getChildren().add(windowButton.build());

        ButtonBuilder fullscreenButton = new ButtonBuilder("fullscreen-option","Fullscreen", Color.LIGHTGRAY, displayX + 10, windowButton.getY() + 50,1 ,1);
        fullscreenButton.setFont(menuFontSmall.getFont());
        root.getChildren().add(fullscreenButton.build());

        // Add skip settings to the right of display
        Text skipText = new Text("Skip");
        skipText.setFont(menuFont.getFont());
        skipText.setX(displayX + 300);
        skipText.setY(displayY);
        skipText.setFill(Color.BLUE);
        root.getChildren().add(skipText);

        int skipX = (int) skipText.getX();
        int skipY = (int) skipText.getY();
        // Add the options for skipping (buttons)
        ButtonBuilder unseenText = new ButtonBuilder("unseen-text-option", "Unseen Text", Color.GRAY,skipX + 25, skipY + 10, 1, 1);
        unseenText.setFont(menuFontSmall.getFont());
        root.getChildren().add(unseenText.build());

        ButtonBuilder afterChoices = new ButtonBuilder("after-choices-option", "After Choices", Color.GRAY,skipX + 25, unseenText.getY() + 50, 1, 1);
        afterChoices.setFont(menuFontSmall.getFont());
        root.getChildren().add(afterChoices.build());

        ButtonBuilder transitions = new ButtonBuilder("transitions-option", "Transitions", Color.GRAY,skipX + 25, afterChoices.getY() + 50, 1, 1);
        transitions.setFont(menuFontSmall.getFont());
        root.getChildren().add(transitions.build());

        // Add slider options and text labels
        Text textSpeedText = new Text("Text Speed");
        textSpeedText.setFont(menuFont.getFont());
        textSpeedText.setX(displayX);
        textSpeedText.setY(displayY + 300);
        textSpeedText.setFill(Color.BLUE);
        root.getChildren().add(textSpeedText);

        Slider textSpeed = new Slider();
        textSpeed.setId("text-speed-options");
        textSpeed.setMax(100);
        textSpeed.setMin(0);
        textSpeed.setValue(100);
        textSpeed.setPrefSize(500, 10);

        textSpeed.setTranslateX(textSpeedText.getX());
        textSpeed.setTranslateY(textSpeedText.getY() + 10);
        root.getChildren().add(textSpeed);

        Text autoForwardTimeText = new Text("Auto-Forward Time");
        autoForwardTimeText.setFont(menuFont.getFont());
        autoForwardTimeText.setX(textSpeed.getTranslateX());
        autoForwardTimeText.setY(textSpeed.getTranslateY() + 50);
        autoForwardTimeText.setFill(Color.BLUE);
        root.getChildren().add(autoForwardTimeText);

        Slider autoForwardTime = new Slider();
        autoForwardTime.setId("auto-forward-time-options");
        autoForwardTime.setMax(100);
        autoForwardTime.setMin(0);
        autoForwardTime.setValue(50);
        autoForwardTime.setTranslateX(autoForwardTimeText.getTranslateX());
        autoForwardTime.setTranslateX(autoForwardTimeText.getY() + 50);
        root.getChildren().add(autoForwardTime);

        VBox vBox = getButtonVbox();
        List<ButtonBuilder> list = new ArrayList<>(getButtons());
        Collections.reverse(list);
        for (ButtonBuilder builder : list) {
            vBox.getChildren().add(builder.build());
        }
        root.getChildren().add(vBox);


        Scene scene = new Scene(root);
        try {
            scene.getStylesheets().add(new File(System.getProperty("user.dir") + "/game/css/button.css").toURI().toURL().toExternalForm());
            scene.getStylesheets().add(new File(System.getProperty("user.dir") + "/game/css/slider.css").toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        RenJava.getInstance().setStage(stage, StageType.OPTIONS_MENU);
    }
}
