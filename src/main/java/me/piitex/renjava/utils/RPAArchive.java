package me.piitex.renjava.utils;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.loggers.RenLogger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import java.io.File;
import java.util.Collection;

/**
 * For archive files I want it to be similar to how RenPY works so I decided to just use the .rpa format.
 * This makes it easier for anyone who wants to migrate over to this system.
 * TODO This is not finished.
 * @author piitex
 */
public class RPAArchive {

    public RPAArchive(RenJava renJava) {
        // Load all archive files.
        Collection<File> files = FileUtils.listFiles(new File(System.getProperty("user.dir") + "/game/"),
                new RegexFileFilter("^(.*?)"),
                DirectoryFileFilter.DIRECTORY);
        for (File file : files) {
            if (file.getName().endsWith(".rpa")) {
                RenLogger.LOGGER.info("Loading archive: " + file.getName());

            }
        }
    }
}
