package me.piitex.renjava;

import javafx.application.Application;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import me.piitex.renjava.api.builders.ImageLoader;
import me.piitex.renjava.configuration.Configuration;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.gui.GuiLoader;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.HashSet;
import java.util.stream.Collectors;

public class Launch extends Application {

    public static void main(String[] args) {
        // Scans for all classes in all packages. (We need to do all packages because this allows the author the freedom to do their own package scheme.)
        Collection<URL> allPackagePrefixes = Arrays.stream(Package.getPackages())
                .map(Package::getName)
                .map(s -> s.split("\\.")[0])
                .distinct()
                .map(ClasspathHelper::forPackage)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        ConfigurationBuilder config = new ConfigurationBuilder().addUrls(allPackagePrefixes)
                .addScanners(Scanners.SubTypes);
        Reflections reflections = new Reflections(config);

        // Detect any classes that extend RenJava
        for (Class<?> c : reflections.getSubTypesOf(RenJava.class)) {
            try {
                Object o = c.getDeclaredConstructor().newInstance();
                RenJava renJava = (RenJava) o;
                if (renJava.getClass().isAnnotationPresent(Game.class)) {
                    Game game = renJava.getClass().getAnnotation(Game.class);
                    renJava.name = game.name();
                    renJava.author = game.author();
                    renJava.version = game.version();
                } else {
                    System.err.println("ERROR: Please annotate your main class with 'Game'.");
                    renJava.name = "Error";
                    renJava.author = "Error";
                    renJava.version = "Error";
                }

                // Build configuration
                if (renJava.getClass().isAnnotationPresent(Configuration.class)) {
                    Configuration conf = renJava.getClass().getAnnotation(Configuration.class);
                    RenJavaConfiguration configuration = new RenJavaConfiguration(conf.title().replace("{version}", renJava.version).replace("{name}", renJava.name), conf.width(), conf.height(), new ImageLoader(conf.windowIconPath()));
                    renJava.setConfiguration(configuration);
                } else {
                    System.err.println("ERROR: Configuration annotation not found. Please annotate your main class with 'Configuration'");
                    RenJavaConfiguration configuration = new RenJavaConfiguration("Error", 1920, 1080, new ImageLoader("gui/window_icon.png"));
                    renJava.setConfiguration(configuration);
                }

                renJava.init(); // Initialize game
                launch(args);
                //c.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                System.err.println("ERROR: Could initialize the RenJava framework: " + e.getMessage());
            }
            return;
        }

        System.err.println("Could not initialize RenJava. Please make a class which extends 'RenJava'.");
    }

    @Override
    public void start(Stage stage) {
        // When launched, load the gui stuff.
        new GuiLoader(stage, RenJava.getInstance(), getHostServices());
    }
}
