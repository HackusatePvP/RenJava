package me.piitex.renjava;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;

import me.piitex.renjava.api.Game;
import me.piitex.renjava.api.loaders.ImageLoader;
import me.piitex.renjava.configuration.Configuration;
import me.piitex.renjava.configuration.InfoFile;
import me.piitex.renjava.configuration.RenJavaConfiguration;
import me.piitex.renjava.gui.GuiLoader;
import me.piitex.renjava.loggers.ApplicationLogger;
import me.piitex.renjava.loggers.RenLogger;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.Scanner;
import java.util.stream.Collectors;

public class Launch extends Application {
    private static long start;

    public static void main(String[] args) {
        // Rough execution time (not accurate)
        start = System.currentTimeMillis();

        // Initializes the Ren logger which is separated from the application logger.
        RenLogger.init();

        // Prevent renjava folder error
        File renJavaDir = new File(System.getProperty("user.dir") + "/renjava/");
        renJavaDir.mkdir();

        InfoFile buildInfo = new InfoFile(new File(System.getProperty("user.dir") + "/renjava/build.info"), true);
        if (buildInfo.containsKey("main")) {
            String mainClass = buildInfo.getString("main");
            Class<?> clazz;
            try {
                clazz = Class.forName(mainClass);
                loadClass(clazz, args, buildInfo);
            } catch (ClassNotFoundException e) {
                RenLogger.LOGGER.error("Failed to load class: {}", e.getMessage());
            }
        } else {
            RenLogger.LOGGER.error("Build info not found. Scanning for RenJava class. This will have noticeable performance impact on low end computers.");
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
                allPackagePrefixes.clear();
                loadClass(c, args, buildInfo);
                break;
            }
        }
    }

    private static void loadClass(Class<?> clazz, String[] args, InfoFile infoFile) {
        try {
            if (!infoFile.exists()) {
                RenLogger.LOGGER.error("Could not create the 'build.info' file. This might be a first time setup. Once the application opens please exit and relaunch.");
                return; // This will exit the application.
            }
            String jarPath = Launch.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();

            RenLogger.LOGGER.info("Jar path: {}", jarPath);

            String[] split = jarPath.split("/");
            String fileName = split[split.length - 1];

            infoFile.write("main", clazz.getName());
            infoFile.write("file", fileName);

        } catch (URISyntaxException e) {
            RenLogger.LOGGER.error("Could retrieve runtime information.", e);
        }

        try {

            // Creates a new instance of the application and executes the constructor.
            Object o = clazz.getDeclaredConstructor().newInstance();
            RenJava renJava = (RenJava) o;
            if (renJava.getClass().isAnnotationPresent(Game.class)) {
                Game game = renJava.getClass().getAnnotation(Game.class);
                renJava.name = game.name();
                renJava.author = game.author();
                renJava.version = game.version();
            } else {
                RenLogger.LOGGER.error("Please annotate your main class with 'Game'.");
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
                RenLogger.LOGGER.error("Configuration annotation not found. Please annotate your main class with 'Configuration'");
                RenJavaConfiguration configuration = new RenJavaConfiguration("Error", 1920, 1080, new ImageLoader("gui/window_icon.png"));
                renJava.setConfiguration(configuration);
            }

            // Initialize the application logger
            ApplicationLogger applicationLogger = new ApplicationLogger(clazz);
            renJava.setLogger(applicationLogger.LOGGER);

            renJava.getLogger().info("Initialized logger...");

            renJava.init(); // Initialize game
            launch(args);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            RenLogger.LOGGER.error("Could initialize the RenJava framework: {}", e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) {
        // When launched, load the gui stuff.
        new GuiLoader(stage, RenJava.getInstance(), getHostServices());

        long end = System.currentTimeMillis();
        long time = end - start;
        DateFormat format = new SimpleDateFormat("ss.SS");

        String s = format.format(time);
        // I hate that it displays the leading 0: 01.26s
        // Fix
        if (s.startsWith("0")) {
            s = s.replaceFirst("0", "");
        }

        RenJava.getInstance().getLogger().info("Loaded in " + s + "s");
    }
}
