package me.piitex.renjava.api.builders;

import me.piitex.renjava.api.music.Track;

import java.io.File;

public class TrackBuilder {
    private final File file;

    public TrackBuilder(String pathToTrack) {
        this.file = new File(System.getProperty("user.dir") + "/game/audio/" + pathToTrack);
    }

    public Track build() {
        return new Track(file);
    }
}
