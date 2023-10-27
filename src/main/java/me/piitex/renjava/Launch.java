package me.piitex.renjava;

import javafx.application.Application;
import javafx.stage.Stage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import me.piitex.renjava.gui.GuiLoader;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.HashSet;

public class Launch extends Application {

    public static void main(String[] args) {
        // Scans for all classes in all packages. (We need to do all packages because this allows the author the freedom to do their own package scheme.)
        Collection<URL> allPackagePrefixes = Arrays.stream(Package.getPackages()).map(p -> p.getName())
                .map(s -> s.split("\\.")[0]).distinct().map(s -> ClasspathHelper.forPackage(s)).reduce((c1, c2) -> {
                    Collection<URL> c3 = new HashSet<>();
                    c3.addAll(c1);
                    c3.addAll(c2);
                    return c3;
                }).get();
        ConfigurationBuilder config = new ConfigurationBuilder().addUrls(allPackagePrefixes)
                .addScanners(Scanners.SubTypes);
        Reflections reflections = new Reflections(config);

        // Detect any classes that extend RenJava
        Class<?> renJavaClass = null;
        for (Class<?> c : reflections.getSubTypesOf(RenJava.class)) {
            if (c.getName().contains("Example")) {
                renJavaClass = c;
            } else {
                try {
                    c.getDeclaredConstructor().newInstance();
                    launch(args);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                         InvocationTargetException e) {
                    e.printStackTrace();
                }
                return;
            }
        }

        try {
            if (renJavaClass != null) {
                renJavaClass.getDeclaredConstructor().newInstance();
                launch(args);
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) {
        // When launched load the gui stuff.
        new GuiLoader(stage, RenJava.getInstance());
    }
}
