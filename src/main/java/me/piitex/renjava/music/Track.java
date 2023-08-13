package me.piitex.renjava.music;

import me.piitex.renjava.RenJava;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Class used for loading and playing audio tracks. Tracks support the following forms; WAV, AIFC, AIFF, AU, SND. The most common is wav. If you have a mp3 file use a program like audacity to convert to it to wav.
 */
public class Track {
    private final File file;

    private Clip clip;


    public Track(String name) {
        this.file = new File(System.getProperty("user.dir") + "/game/audio/" + name);
    }

    public void play() {
        // play audio.
        if (RenJava.getInstance().getTrack() != null) {
            RenJava.getInstance().getTrack().stop();
        }
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            this.clip = clip;
            RenJava.getInstance().setTrack(this); // Set the track when playing.
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.close();
            RenJava.getInstance().setTrack(null);
        }
    }
}
