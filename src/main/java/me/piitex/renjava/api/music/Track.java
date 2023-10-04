package me.piitex.renjava.api.music;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Track {
    private final File file;

    private Clip clip;

    public Track(File file) {
        this.file = file;
    }

    public void play() {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            this.clip = clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.close();
        }
    }

    public File getFile() {
        return file;
    }
}