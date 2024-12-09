package me.piitex.renjava.gui.menus;

import me.piitex.renjava.api.saves.Save;
import me.piitex.renjava.gui.Container;
import me.piitex.renjava.gui.Window;
import me.piitex.renjava.gui.containers.EmptyContainer;
import me.piitex.renjava.gui.overlays.ButtonOverlay;

public interface MainMenu {

    /**
     * This function is used to create the main menu screen.
     *
     * <p>
     * The main menu is rendered to the game {@link Window} which is managed by the framework. This function is used to make the base {@link  Container} which is then added to the window.
     * <p>
     *     Note, the side menu will be automatically rendered and does not be to be added to this container.
     * </p>
     * </p>
     * <p>
     * <pre>
     *     {@code
     *     public void Container mainMenu(boolean rightClick) {
     *         Container container = new EmptyContainer(1920, 1080) // Width and height of the container
     *
     *         // Add overlays to the main menu
     *         ImageOverlay backgroundImage = new ImageOverlay("gui/main_menu.png");
     *         backgroundImage.setOrder(DisplayOrder.LOW); // Sends the background image to the back of the container.
     *         container.addOverlay(backgroundImage);
     *
     *         return container;
     *     }
     *     }
     * </pre>
     * </p>
     * @see Container
     * @see Window
     * @see EmptyContainer
     * @param rightClick If it is the right-clicked main menu.
     * @return Container to be used for the main menu.
     */
    Container mainMenu(boolean rightClick);

    Container sideMenu(boolean rightClick);

    Container loadMenu(boolean rightClick, int page, boolean loadMenu);

    Container settingMenu(boolean rightClick);

    Container aboutMenu(boolean rightClick);

    ButtonOverlay savePreview(Save save, int page, int index);
}
