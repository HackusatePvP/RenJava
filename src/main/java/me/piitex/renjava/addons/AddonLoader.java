package me.piitex.renjava.addons;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.configuration.InfoFile;
import me.piitex.renjava.loggers.RenLogger;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.slf4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
                RenLogger.LOGGER.error("Invalid or corrupted addon jar file.", e);
                RenJava.writeStackTrace(e);
                return;
            }
            ZipEntry entry = zipFile.getEntry("build.info");
            if (entry == null) {
                logger.error("Could not find build.info for " + file.getName() + " Please include a build.info file for proper addon loading. If this addon is running an old version of the RenJava framework it could fail.");
                nonDependants.add(file);
                continue;
            }

            // Convert entry to file then load info file
            try {
                File buildFile = new File(System.getProperty("user.dir") + "/addons/build.info");
                Files.copy(zipFile.getInputStream(entry), Path.of(buildFile.getPath()), StandardCopyOption.REPLACE_EXISTING);

                InfoFile build = new InfoFile(buildFile, false);

                boolean invalidVersion = false;

                if (build.containsKey("ren.version") && !build.getString("ren.version").isEmpty()) {
                    String ver = build.getString("ren.version");
                    logger.info("Addon Ren Version: {}", ver);

                    ComparableVersion requiredVersion = new ComparableVersion(ver);
                    ComparableVersion currentVersion = new ComparableVersion(RenJava.getInstance().getBuildVersion());
                    if (requiredVersion.compareTo(currentVersion) < 0) {
                        logger.error(file.getName() + " was built with an older RenJava version. Please advise author to update the addon to support the current running version. The addon will not load until it is upgraded to the proper version.");
                        invalidVersion = true;
                    } else if (requiredVersion.compareTo(currentVersion) > 0) {
                        logger.error(file.getName() + " was built with a new version of RenJava than the application. Please advise author to downgrade the addon to support the current running version or download a newly updated version of the game. The addon will not load until it the issue is fixed.");
                        invalidVersion = true;
                    }
                } else {
                    logger.error("Addon does not have a provided RenJava version. In future releases the addon will not load without a proper version.");
                }


                if (build.containsKey("dependencies") && !build.getString("dependencies").isEmpty()) {
                    String dep = build.getString("dependencies");
                    if (!invalidVersion) {
                        lateLoaders.put(file, dep);
                    }
                } else {
                    if (!invalidVersion) {
                        nonDependants.add(file);
                    }
                }

                // Delete after
                buildFile.delete();

                extractResources(zipFile);

            } catch (IOException e) {
                throw new RuntimeException(e);
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
        // Cleanup (Not clearing these will result in increase resource usage)
        passed.clear();
        lateLoaders.clear();
        nonDependants.clear();
        validations.clear();
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
                    // This can easily allow malicious code to be executed. I will not be adding any form on 'anti malware' checks. Don't download something you don't trust.
                    // Also be aware of the licence renjava uses. Authors are required to provide source to code per the GPL 3.0 license.
                    Class<?> clazz = cl.loadClass(clazzName);
                    if (Addon.class.isAssignableFrom(clazz)) {
                        Object object = clazz.getDeclaredConstructor().newInstance();
                        Addon addon = (Addon) object;
                        if (dependencies != null) {
                            addon.getDependencies().addAll(dependencies);
                        }
                        addons.add(addon);
                        //clazz.getMethod("onLoad").invoke(object, null);
                        boolean failed = false;
                        try {
                            addon.onLoad(); // Loads addon
                        } catch (Exception e) {
                            RenJava.writeStackTrace(e);
                            RenLogger.LOGGER.error("An error occurred while loading addon '{}'.", addon.getName());
                            failed = true;
                        }
                        if (!failed) {addons.add(addon);
                        logger.info("Loaded: {}", addon.getName());
                        }
                    }
                }
            }
        }
    }

    private void extractResources(ZipFile zipFile) {
        // Extract game content from jar file resources to game folder.
        RenLogger.LOGGER.info("Extracting addon contents...");
        Enumeration<?> enumeration = zipFile.entries();
        while (enumeration.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
            String nanme = zipEntry.getName();
            if (nanme.startsWith("game/")) {
                File file = new File(nanme);
                if (nanme.endsWith("/")) {
                    file.mkdirs();
                    continue;
                }

                try {
                    Files.copy(zipFile.getInputStream(zipEntry), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    RenLogger.LOGGER.info("Extracted '{}' to '{}'", nanme, file.getAbsolutePath());
                } catch (IOException e) {
                    RenLogger.LOGGER.error(e.getMessage(), e);
                    RenJava.writeStackTrace(e);
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
