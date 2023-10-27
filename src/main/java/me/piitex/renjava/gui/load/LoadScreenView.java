package me.piitex.renjava.gui.load;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.builders.ButtonBuilder;
import me.piitex.renjava.api.saves.Save;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.gui.ScreenView;
import me.piitex.renjava.api.builders.FontLoader;
import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.gui.StageType;
import me.piitex.renjava.gui.exceptions.ImageNotFoundException;
import me.piitex.renjava.gui.title.DefaultMainTitleScreenView;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoadScreenView extends ScreenView {
    private final ImageLoader backgroundImage;

    private int page;

    public LoadScreenView(ImageLoader backgroundImage) {
        this.backgroundImage = backgroundImage;
        page = 1;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
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
            e.printStackTrace();
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
        topBox.setSpacing(20);
        HBox bottomBox = new HBox();
        bottomBox.setSpacing(20);


        for (int i = page - 1; i < 6 * page; i++) {
            if (i < 3 * page) {
                // Get the selected Save at the page index.
                // Return Empty Save if null
                RenJava.getInstance().getLogger().info("Creating save " + i);
                Button button = new Button("save-" + i);
                button.setMaxSize(50, 50);
                /*try {
                    button.setGraphic(new ImageView(new ImageLoader("gui/frame.png").build()));

                } catch (ImageNotFoundException e) {
                    throw new RuntimeException(e);
                }*/
                button.setText("Save " + i);
                button.setContentDisplay(ContentDisplay.BOTTOM);
                topBox.getChildren().add(button);
            } else {
                RenJava.getInstance().getLogger().info("Creating save " + i);
                Button button = new Button("save-" + i);
                button.setPrefSize(50, 50);
                try {
                    button.setGraphic(new ImageView(new ImageLoader("gui/frame.png").build()));
                } catch (ImageNotFoundException e) {
                    throw new RuntimeException(e);
                }
                button.setText("Save " + i);
                button.setContentDisplay(ContentDisplay.BOTTOM);
                bottomBox.getChildren().add(button);
            }
        }

        mainBox.getChildren().add(topBox);
        mainBox.getChildren().add(bottomBox);
        mainBox.setTranslateX(400);
        mainBox.setTranslateY(400);
        root.getChildren().add(mainBox);

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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        RenJava.getInstance().setStage(stage, StageType.OPTIONS_MENU);
    }

}
