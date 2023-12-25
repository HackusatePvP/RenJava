package me.piitex.renjava.addons;

import me.piitex.renjava.RenJava;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public class AddonLoader {
    private final List<Addon> addons = new ArrayList<>();

    public void load() {
        Logger logger = RenJava.getInstance().getLogger();
        File directory = new File(System.getProperty("user.dir") + "/addons/");

        logger.info("Initializing Addon Loader...");
        int size = directory.listFiles().length;
        if (size == 0)
            logger.info("No addons to load.");
        else
            logger.info("Loading " + size + " addon(s)...");

        for (File file : directory.listFiles()) {
            if (file.getName().endsWith(".jar")) {
                logger.info("Loading " + file.getName());
                try (JarFile jarFile = new JarFile(file)) {
                    Enumeration<JarEntry> entries = jarFile.entries();
                    URL[] urls = { new URL("jar:file:" + file.getPath() +"!/") };
                    URLClassLoader cl = URLClassLoader.newInstance(urls);
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String clazzString = entry.getName();

                        if (clazzString.endsWith(".class")) {
                            String clazzName = clazzString.replace('/', '.').substring(0, clazzString.length() - 6);

                            // FIXME: 12/24/2023 This has some serious security concerns. This executes code from any jar inside of addons and offers no real detection for malicious code.
                            // Authors should warn users about using pirated versions or getting addons from unknown sources.
                            Class<?> clazz = cl.loadClass(clazzName);
                            if (Addon.class.isAssignableFrom(clazz)) {
                                logger.info("Executing addon...");
                                Object object = clazz.getDeclaredConstructor().newInstance();
                                clazz.getMethod("onLoad").invoke(object, null);

                                Addon addon = (Addon) object;
                                addons.add(addon);
                            }
                        }
                    }

                } catch (IOException e) {
                    logger.severe("Failed to read " + file.getName());
                } catch (ClassNotFoundException e) {
                    logger.severe("Unable to find class for " + file.getName());
                } catch (InvocationTargetException e) {
                    logger.severe("Failed to load " + file.getName());
                } catch (InstantiationException e) {
                    logger.severe("Cannot load abstract class!" + file.getName());
                } catch (IllegalAccessException e) {
                    logger.severe("Unable to access \"onLoad\" method." + file.getName());
                } catch (NoSuchMethodException e) {
                    logger.severe("\"onLoad\" does not exist. " + file.getName());
                }
            }
        }
    }

    public void disable() {
        for (Addon addon : addons) {
            addon.onDisable();
        }
    }
}
