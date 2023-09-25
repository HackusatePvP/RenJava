package me.piitex.renjava.events.defaults;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.player.Player;
import me.piitex.renjava.api.scenes.RenScene;
import me.piitex.renjava.api.scenes.types.choices.Choice;
import me.piitex.renjava.api.scenes.types.choices.ChoiceScene;
import me.piitex.renjava.api.scenes.types.input.InputScene;
import me.piitex.renjava.events.EventListener;
import me.piitex.renjava.events.Listener;
import me.piitex.renjava.events.types.*;

public class ScenesEventListener implements EventListener {

    @Listener
    public void onSceneStart(SceneStartEvent event) {
        RenScene scene = event.getScene();
        Player player = RenJava.getInstance().getPlayer();
        player.setCurrentScene(scene.getId());
    }

    @Listener
    public void onSceneEnd(SceneEndEvent event) {
        Player player = RenJava.getInstance().getPlayer();
        if (event.getScene() instanceof InputScene scene) {
            TextField field = scene.getInputField();
            InputSceneEndEvent endEvent = new InputSceneEndEvent(scene, field.getText());
            RenJava.callEvent(endEvent);
        }
    }

    @Listener
    public void onChoiceButtonClick(ButtonClickEvent event) {
        Button button = event.getButton();

        RenJava.getInstance().getLogger().info("Testing choice stuff");
        RenScene scene = event.getScene();
        if (scene instanceof ChoiceScene choiceScene) {
            RenJava.getInstance().getLogger().info("Button clicked on choice scene");
            Choice choice = choiceScene.getChoice(button.getId());
            if (choice != null) {
                RenJava.getInstance().getLogger().info("choice not null");
                ChoiceSelectEvent selectEvent = new ChoiceSelectEvent(choice, scene.getStory(), scene);
                RenJava.callEvent(selectEvent);

                if (choiceScene.getSelectInterface() != null) {
                    choiceScene.getSelectInterface().onChoiceSelect(selectEvent);
                }
                RenJava.getInstance().getLogger().info("choice done");
            }
        }
    }

    @Listener
    public void onInputEndEvent(InputSceneEndEvent event) {
        InputScene scene = event.getScene();
        if (scene.getSetInterface() != null) {
            scene.getSetInterface().onInput(event);
        }
    }
}
