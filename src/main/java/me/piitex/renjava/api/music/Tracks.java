package me.piitex.renjava.api.music;

import me.piitex.renjava.api.saves.data.Data;
import me.piitex.renjava.api.saves.data.PersistentData;

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
        return tracks.get(trackID);
    }

    public Track getCurrentTrack() {
        return getTrack(currentTrack);
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

    public void play(String fileName, boolean loop) {
        play(getTrack(fileName), loop);
    }

    public void play(Track track, boolean loop) {
        if (currentTrack != null) {
            getCurrentTrack().stop();
        }
        setPlaying(true);
        currentTrack = track.getId();
        setLoop(loop);
        track.play(loop);
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
