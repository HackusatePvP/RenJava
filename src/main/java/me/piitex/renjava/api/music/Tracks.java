package me.piitex.renjava.api.music;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Tracks {
    private final Map<String, Track> tracks = new HashMap<>();

    private Track currentTrack = null;

    public void addTrack(String trackID, Track track) {
        tracks.put(trackID, track);
    }

    public Track getTrack(String trackID) {
        return tracks.get(trackID);
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public void play(String fileName, boolean loop) {
        play(getTrack(fileName), loop);
    }

    public void play(Track track, boolean loop) {
        if (currentTrack != null) {
            currentTrack.stop();
        }
        currentTrack = track;
        track.play(loop);
    }

    public void stop() {
        if (currentTrack != null) {
            currentTrack.stop();
        }
    }

    public void setCurrentTrack(Track currentTrack) {
        this.currentTrack = currentTrack;
    }

    public Collection<Track> getTracks() {
        return tracks.values();
    }
}
