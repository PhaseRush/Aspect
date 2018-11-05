package main.utility.state_json.json_container.reload_playlists;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.List;

public class SavedPlaylist {
    //channel info
    String stringId;
    long longId;

    //queue
    List<AudioTrack> queue;

    //if necessary use this note. Default to following message
    String note = "Aspect has been restarted and this playlist will be restored.";
}