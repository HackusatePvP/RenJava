package me.piitex.renjava;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import me.piitex.renjava.api.Game;
import me.piitex.renjava.api.loaders.ImageLoader;
import me.piitex.renjava.configuration.Configuration;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.gui.GuiLoader;
import me.piitex.renjava.loggers.ApplicationLogger;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.Scanner;
import java.util.stream.Collectors;

public class Launch extends Application {

    public static void main(String[] args) {
        File file = new File(System.getProperty("user.dir") + "/renjava/build.info");
        boolean failed = true;

        if (file.exists()) {
            try (InputStream inputStream = new FileInputStream(file);
                 Scanner scanner = new Scanner(inputStream)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.toLowerCase().startsWith("main=")) {
                        String[] split = line.split("=");
                        String clazzName = split[1];
                        Class<?> clazz = Class.forName(clazzName);
                        loadClass(clazz, args);
                        failed = false;
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Failed to load class: " + e.getMessage());
            }
        }

        if (failed) {
            System.err.println("Build info not found. Scanning for RenJava class. This will have noticeable performance impact on low end computers.");
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
                loadClass(c, args);
                return;
            }
        }
    }

    private static void loadClass(Class<?> clazz, String[] args) {
        //FIXME: For performance save the path of the main class to reduce loading time
        try {
            File file = new File(System.getProperty("user.dir") + "/renjava/build.info");
            if (!file.exists()) {
                file.createNewFile();
            }

            String jarPath = Launch.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();

            System.out.println("Path: " + jarPath);

            String[] split = jarPath.split("/");
            String fileName = split[split.length - 1];

            FileWriter writer = new FileWriter(file);
            writer.append("main=").append(clazz.getName());
            writer.append("\n");
            writer.write("file=" + fileName);
            writer.close();
        } catch (IOException e) {
            System.err.println("Could not create the 'build.info' file. This might be a first time setup. Once the application opens please exit and relaunch.");
        } catch (URISyntaxException e) {
            System.err.println("Could retrieve runtime information.");
        }


        try {
            Object o = clazz.getDeclaredConstructor().newInstance();
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
                RenJavaConfiguration configuration = new RenJavaConfiguration(conf.title().replace("{version}", renJava.version).replace("{name}", renJava.name).replace("{author}", renJava.author), conf.width(), conf.height(), new ImageLoader(conf.windowIconPath()));
                renJava.setConfiguration(configuration);

            } else {
                System.err.println("ERROR: Configuration annotation not found. Please annotate your main class with 'Configuration'");
                RenJavaConfiguration configuration = new RenJavaConfiguration("Error", 1920, 1080, new ImageLoader("gui/window_icon.png"));
                renJava.setConfiguration(configuration);
            }

            // Initialize the application logger
            ApplicationLogger applicationLogger = new ApplicationLogger(clazz);
            renJava.setLogger(applicationLogger.LOGGER);

            renJava.getLogger().info("Initialized logger...");

            renJava.init(); // Initialize game
            launch(args);
            //c.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.err.println("ERROR: Could initialize the RenJava framework: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) {
        // When launched, load the gui stuff.
        new GuiLoader(stage, RenJava.getInstance(), getHostServices());
    }
}
