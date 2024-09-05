package me.piitex.renjava.api.music;

import me.piitex.renjava.RenJava;
import me.piitex.renjava.api.exceptions.MusicFileNotFound;
import me.piitex.renjava.api.saves.data.Data;
import me.piitex.renjava.api.saves.data.PersistentData;
import me.piitex.renjava.loggers.RenLogger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Tracks implements PersistentData {
    private final Map<String, Track> tracks = new HashMap<>();

    @Data private String currentTrack = null;

    @Data private boolean isPlaying = false;

    @Data private boolean isLooping = false;

    public void addTrack(Track track) {
        tracks.put(track.getId(), track);
    }

    public Track getTrack(String trackID) {
        if (tracks.get(trackID) == null) {
            MusicFileNotFound musicFileNotFound = new MusicFileNotFound(trackID);
            RenLogger.LOGGER.error("Could not load track '" + trackID + "'", musicFileNotFound);
            RenJava.writeStackTrace(musicFileNotFound);
            return null;
        }
        return tracks.get(trackID);
    }

    public Track getCurrentTrack() {
        if (currentTrack != null) {
            return getTrack(currentTrack);
        }
        return null;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isLoop() {
        return isLooping;
    }

    public void setLoop(boolean loop) {
        this.isLooping = loop;
    }

    public void playMusic(String fileName, boolean loop) {
        playMusic(getTrack(fileName), loop);
    }

    public void playSound(String fileName, boolean loop) {
        playSound(getTrack(fileName), loop);
    }

    public void playVoice(String fileName, boolean loop) {
        playVoice(getTrack(fileName), loop);
    }


    public void playMusic(Track track, boolean loop) {
        if (getCurrentTrack() != null) {
            getCurrentTrack().stop();
        }
        setPlaying(true);
        currentTrack = track.getId();
        setLoop(loop);
        track.playMusic(loop);
    }

    public void playSound(Track track, boolean loop) {
        if (getCurrentTrack() != null) {
            getCurrentTrack().stop();
        }
        setPlaying(true);
        currentTrack = track.getId();
        setLoop(loop);
        track.playSound(loop);
    }

    public void playVoice(Track track, boolean loop) {
        if (getCurrentTrack() != null) {
            getCurrentTrack().stop();
        }
        setPlaying(true);
        currentTrack = track.getId();
        setLoop(loop);
        track.playVoice(loop);
    }

    public void stop() {
        if (currentTrack != null) {
            setPlaying(false);
            getCurrentTrack().stop();
        }
    }

    public void setCurrentTrack(Track currentTrack) {
        this.currentTrack = currentTrack.getId();
    }

    public Collection<Track> getTracks() {
        return tracks.values();
    }
}
