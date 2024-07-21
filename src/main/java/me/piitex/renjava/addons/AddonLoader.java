package me.piitex.renjava.addons;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.loggers.RenLogger;
import org.slf4j.Logger;;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class AddonLoader {
    private final List<Addon> addons = new ArrayList<>();

    private final Logger logger;
    public AddonLoader() {
        logger = RenLogger.LOGGER;
    }

    public void load() {
        File directory = new File(System.getProperty("user.dir") + "/addons/");
        if (directory.mkdir()) {
            logger.warn("Created directory '" + "addons" + "'.");
        }
        logger.info("Initializing Addon Loader...");
        int size = directory.listFiles().length;
        if (size == 0) {
            logger.info("No addons to load.");
            return; // No need to load if there are no addons.
        } else {
            logger.info("Loading " + size + " addon(s)...");
        }

        Map<File, String> lateLoaders = new HashMap<>();
        Collection<File> nonDependants = new HashSet<>();

        // Reads build.info and gathers dependency information.
        for (File file : directory.listFiles()) {
            logger.info("Found " + file.getName());
            ZipFile zipFile;
            try {
                zipFile = new ZipFile(file);
            } catch (IOException e) {
                RenLogger.LOGGER.error("Invalid or corrupted  addon jar file.", e);
                RenJava.writeStackTrace(e);
                return;
            }
            ZipEntry entry = zipFile.getEntry("build.info");
            if (entry == null) {
                logger.error("Could not find build.info for " + file.getName() + " Addon will load with the presumption there are no dependencies.");
                nonDependants.add(file);
                continue;
            }
            try (InputStream inputStream = zipFile.getInputStream(entry);
                Scanner scanner = new Scanner(inputStream)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.contains("dependencies")) {
                        String[] split = line.split(":");
                        if (split.length == 1) {
                            nonDependants.add(file);
                        } else {
                            String dependency = split[1];
                            if (dependency.equalsIgnoreCase(" \"\"") || dependency.isEmpty()) {
                                nonDependants.add(file);
                                continue;
                            }
                            dependency = dependency.replace("\"", "");
                            lateLoaders.put(file, dependency);
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                RenLogger.LOGGER.error("Invalid or corrupted  addon jar file.", e);
                RenJava.writeStackTrace(e);
            }
        }

        // Execute addons that have no dependencies
        logger.info("Loading non-dependant addons...");
        nonDependants.forEach(file -> {
            try {
                initAddon(file, null);
            } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                     InstantiationException | IllegalAccessException e) {
                RenLogger.LOGGER.error("Error occurred when initializing addon!", e);
                RenJava.writeStackTrace(e);
            }
        });

        Map<File, String> validations = new HashMap<>(lateLoaders);
        Collection<String> passed = new HashSet<>();
        AtomicReference<String> lastValidated = new AtomicReference<>("");

        while (!validations.isEmpty()) {
            lateLoaders.forEach((file, string) -> {
                if (lastValidated.get().equalsIgnoreCase(file.getName())) {
                    logger.error("Could not initialize " + file.getName() + ": May be the result of a missing dependency.");
                    validations.remove(file);
                    return;
                }
                if (passed.contains(file.getName())) {
                    return;
                }
                String dependency = string.trim();
                if (dependency.contains(",")) {
                    boolean canExecute = true;
                    Collection<Addon> dep = new HashSet<>();
                    for (String depend : dependency.split(",")) {
                        Addon addon = addons.stream().filter(addon1 -> addon1.getName().equalsIgnoreCase(depend)).findAny().orElse(null);
                        if (addon == null) {
                            canExecute = false;
                            continue;
                        }
                        dep.add(addon);
                    }
                    if (canExecute) {
                        try {
                            initAddon(file, dep);
                            validations.remove(file); // Remove from validations
                            passed.add(file.getName());
                        } catch (IOException | ClassNotFoundException | InvocationTargetException |
                                 NoSuchMethodException |
                                 InstantiationException | IllegalAccessException e) {
                            RenLogger.LOGGER.error("Error occurred when initializing addon!", e);
                            RenJava.writeStackTrace(e);
                        }
                    }
                } else {
                    Addon addon = addons.stream().filter(addon1 -> addon1.getName().equalsIgnoreCase(dependency)).findAny().orElse(null);
                    if (addon != null) {
                        try {
                            initAddon(file, Collections.singleton(addon));
                            validations.remove(file); // Remove from validations
                            passed.add(file.getName());
                        } catch (IOException | ClassNotFoundException | InvocationTargetException |
                                 NoSuchMethodException |
                                 InstantiationException | IllegalAccessException e) {
                            RenLogger.LOGGER.error("Error occurred when initializing addon!", e);
                            RenJava.writeStackTrace(e);
                        }
                    }
                }

                lastValidated.set(file.getName());
            });
        }
        // Cleanup
        passed.clear();
        lateLoaders.clear();
    }

    private void initAddon(File file, @Nullable Collection<Addon> dependencies) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            URL[] urls = {new URL("jar:file:" + file.getPath() + "!/")};
            URLClassLoader cl = URLClassLoader.newInstance(urls);
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String clazzString = entry.getName();
                if (clazzString.endsWith(".class")) {
                    String clazzName = clazzString.replace('/', '.').substring(0, clazzString.length() - 6); // removes the .class at the end

                    // Authors should warn users about using pirated versions or getting addons from unknown sources.
                    Class<?> clazz = cl.loadClass(clazzName);
                    if (Addon.class.isAssignableFrom(clazz)) {
                        logger.info("Executing addon...");
                        Object object = clazz.getDeclaredConstructor().newInstance();
                        Addon addon = (Addon) object;
                        if (dependencies != null) {
                            addon.getDependencies().addAll(dependencies);
                        }
                        addons.add(addon);
                        //clazz.getMethod("onLoad").invoke(object, null);
                        addon.onLoad(); // Loads addon
                        logger.info("Loaded: " + addon.getName());
                    }
                }
            }
        }
    }

    public void disable() {
        for (Addon addon : addons) {
            addon.onDisable();
        }
    }

    public List<Addon> getAddons() {
        return addons;
    }
}
