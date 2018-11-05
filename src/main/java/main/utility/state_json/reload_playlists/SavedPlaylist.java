package main.utility.state_json.reload_playlists;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.List;

public class SavedPlaylist {
    String stringId;
    long longId;

    List<AudioTrack> queue;

    String note;
}